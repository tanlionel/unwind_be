package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link RentalPosting}
 */
@Value
public class RentalPostingApprovalDto implements Serializable {
    Float staffRefinementPrice;
    String note;
    Float priceValuation;
    Integer unitTypeId;
}