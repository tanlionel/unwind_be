package com.capstone.unwind.model.EmailRequestDTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDto {
    String name;
    String content;
    String subject;
}
