package com.capstone.unwind.model.FeedbackDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ReportRequestDto {
    private String note;
}
