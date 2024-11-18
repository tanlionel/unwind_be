package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import org.springframework.data.jpa.domain.Specification;

public class ExchangePostingSpecification {
    public static Specification<ExchangePosting> hasOwnerId(Integer ownerId) {
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<ExchangePosting> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<ExchangePosting> hasResortId(Integer resortId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roomInfo").get("resort").get("id"), resortId);
    }

    public static Specification<ExchangePosting> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
}