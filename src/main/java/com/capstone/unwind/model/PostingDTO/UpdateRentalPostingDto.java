package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentalPostingDto implements Serializable {
    String description;
    Float pricePerNights;
    Integer cancellationTypeId;
    List<String> ImageUrls;
}