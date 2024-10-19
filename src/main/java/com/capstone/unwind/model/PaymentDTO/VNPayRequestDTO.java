package com.capstone.unwind.model.PaymentDTO;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
public class VNPayRequestDTO implements Serializable {
    Long amount;
    String orderType;
}
