package com.capstone.unwind.model.SystemDTO;

import lombok.*;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqRequestDTO {
    private String type;
    private String title;
    private String description;
}
