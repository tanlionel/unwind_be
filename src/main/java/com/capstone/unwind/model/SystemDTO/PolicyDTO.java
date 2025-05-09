package com.capstone.unwind.model.SystemDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@Builder
public class PolicyDTO {
    private Integer policyId;
    private String type;
    private String title;
    private String description;
    private Timestamp createdDate;
}
