package com.capstone.unwind.model.PaymentDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class VNPayTransactionDetailDTO implements Serializable {
    Long amount;
    String responseCode;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime transactionTime;
    String orderDetail;
}
