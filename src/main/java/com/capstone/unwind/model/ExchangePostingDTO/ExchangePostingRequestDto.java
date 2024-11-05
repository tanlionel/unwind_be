package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangePosting}
 */
@Value
public class ExchangePostingRequestDto implements Serializable {
    String description;
    Integer nights;
    Integer exchangePackageId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    Integer timeshareId;
}