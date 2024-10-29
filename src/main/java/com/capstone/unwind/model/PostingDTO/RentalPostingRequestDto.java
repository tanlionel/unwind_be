package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link RentalPosting}
 */
@Value
public class RentalPostingRequestDto implements Serializable {
    String description;
    Integer nights;
    Float pricePerNights;
    Integer timeshareId;
    Integer cancellationTypeId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    Integer rentalPackageId;
}