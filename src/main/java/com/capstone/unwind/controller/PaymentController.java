package com.capstone.unwind.controller;

import com.capstone.unwind.config.VNpayConfig;
import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PaymentDTO.VNPayRequestDTO;
import com.capstone.unwind.model.PaymentDTO.VNPayResponseDTO;
import com.capstone.unwind.model.PaymentDTO.VNPayTransactionDetailDTO;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {
    @Autowired
    private final WalletService walletService;


    @GetMapping("url-payment")
    public ResponseEntity<VNPayResponseDTO> createPaymentLink(HttpServletRequest req,
                                                              @RequestParam(value = "amount") Long amount,
                                                              @RequestParam(value = "orderTYpe") String orderType) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNpayConfig.getIpAddress(req);
        String vnp_TmnCode = VNpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr",vnp_IpAddr);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNpayConfig.vnp_ReturnUrl);



        // Lấy thời gian hiện tại theo múi giờ Việt Nam
        ZoneId vnZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime now = LocalDateTime.now(vnZoneId);

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // Tạo thời gian tạo giao dịch
        String vnp_CreateDate = now.format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Tạo thời gian hết hạn (15 phút sau)
        String vnp_ExpireDate = now.plusMinutes(15).format(formatter);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // In ra kết quả để kiểm tra
        System.out.println("Create Date: " + vnp_CreateDate);
        System.out.println("Expire Date: " + vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNpayConfig.hmacSHA512(VNpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNpayConfig.vnp_PayUrl + "?" + queryUrl;
        return ResponseEntity.ok(VNPayResponseDTO.builder().url(paymentUrl).build());
    }
    @GetMapping("payment-infor")
    public ResponseEntity<VNPayTransactionDetailDTO> transaction(@RequestParam(value = "vnp_Amount",required = false) String amount,
                                         @RequestParam(value = "vnp_OrderInfo",required = false) String orderInfo,
                                         @RequestParam(value = "vnp_ResponseCode",required = false) String responseCode,
                                        @RequestParam(value = "vnp_PayDate",required = false) String payDate ) throws OptionalNotFoundException {
        float fee = 0.0f;
        float money =(float) Long.parseLong(amount)/100;
        String paymentMethod = "VNPAY";
        WalletTransaction walletTransaction = walletService.createTransactionVNPAY(fee,money,paymentMethod);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        VNPayTransactionDetailDTO vnPayTransactionDetailDTO = VNPayTransactionDetailDTO.builder()
                .amount(Long.parseLong(amount)/100)
                .transactionTime(LocalDateTime.parse(payDate, formatter))
                .orderDetail(orderInfo)
                .responseCode(responseCode)
                .walletTransactionId(walletTransaction.getId())
                .build();
        return ResponseEntity.ok(vnPayTransactionDetailDTO);
    }

}
