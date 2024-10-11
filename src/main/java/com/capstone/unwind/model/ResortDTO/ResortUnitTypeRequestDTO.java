package com.capstone.unwind.model.ResortDTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResortUnitTypeRequestDTO {
    Integer resortId;
    List<UnitTypeDto> unitTypeDtoList;
}
