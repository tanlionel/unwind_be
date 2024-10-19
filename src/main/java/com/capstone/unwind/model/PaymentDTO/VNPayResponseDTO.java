package com.capstone.unwind.model.PaymentDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
public class VNPayResponseDTO implements Serializable {
    String url;
}
