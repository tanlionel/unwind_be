package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangeRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangeRequest}
 */
@Value
public class ExchangeRequestDto implements Serializable {
    Integer timeshareId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endDate;
    Integer exchangePostingId;
}