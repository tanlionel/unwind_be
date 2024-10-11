package com.capstone.unwind.model.ResortDTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUnitTypeAmentiesDTO {
    Integer unitTypeId;
    List<UnitTypeAmenitiesDTO> unitTypeAmenitiesDTOS;
}
