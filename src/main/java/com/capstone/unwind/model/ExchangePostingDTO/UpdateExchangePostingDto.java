package com.capstone.unwind.model.ExchangePostingDTO;

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
public class UpdateExchangePostingDto implements Serializable {
    String description;
    List<String> ImageUrls;
}