package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import org.springframework.data.jpa.domain.Specification;

public class RentalPostingSpecification {
    public static Specification<RentalPosting> hasOwnerId(Integer ownerId) {
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<RentalPosting> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<RentalPosting> hasResortId(Integer resortId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roomInfo").get("resort").get("id"), resortId);
    }

    public static Specification<RentalPosting> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
    public static Specification<RentalPosting> resortNameContains(String resortName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("roomInfo").get("resort").get("resortName"), "%" + resortName + "%");
    }
    public static Specification<RentalPosting> hasPackageId(Integer packageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("rentalPackage").get("id"), packageId);
    }
}