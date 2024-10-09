package com.capstone.unwind.model.ResortDTO;

import java.util.List;

public record ResortPoliciesRequestDto(List<ResortPolicy> resortPolicyList, Integer resortId) {

    public  record ResortPolicy(String description, String attachmentURl) {
    }
}
