package com.capstone.unwind.service.ServiceImplement;


import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SendinblueService {

    @Value("${sendinblue.api-key}")
    private String apiKey;

    @Value("${sendinblue.sender-email}")
    private String senderEmail;



    private TransactionalEmailsApi transactionalEmailsApi;

    @PostConstruct
    public void init() {
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKey(apiKey);
        this.transactionalEmailsApi = new TransactionalEmailsApi(apiClient);
    }
    public void sendEmailWithTemplate(String to, EmailEnum emailEnum, EmailRequestDto emailRequestDto) {

        // Người gửi
        SendSmtpEmailSender sender = new SendSmtpEmailSender().email(senderEmail);

        // Người nhận
        SendSmtpEmailTo recipient = new SendSmtpEmailTo().email(to);

        // Tạo params chỉ với các giá trị không null
        Map<String, Object> params = new HashMap<>();
        Optional.ofNullable(emailRequestDto.getName()).ifPresent(value -> params.put("name", value));
        Optional.ofNullable(emailRequestDto.getSubject()).ifPresent(value -> params.put("subject", value));
        Optional.ofNullable(emailRequestDto.getContent()).ifPresent(value -> params.put("content", value));
        Optional.ofNullable(emailRequestDto.getTransactionCode()).ifPresent(value -> params.put("transaction_code", value));
        Optional.ofNullable(emailRequestDto.getTransactionType()).ifPresent(value -> params.put("transaction_type", value));
        Optional.of(emailRequestDto.getMoney()).ifPresent(value -> params.put("amount", value));

        // Tạo đối tượng SendSmtpEmail
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                .sender(sender)
                .to(Arrays.asList(recipient))
                .templateId(emailEnum.getTemplateId())
                .params(params); // Chỉ truyền params đã được xây dựng

        // Gửi email
        try {
            transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
            log.info("Email sent successfully to {}", to);
        } catch (ApiException e) {
            log.error("Error sending email to {}: {}", to, e.getMessage(), e);
        }
    }

}