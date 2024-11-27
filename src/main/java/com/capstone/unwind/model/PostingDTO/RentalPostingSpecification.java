package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


import java.util.Arrays;
import java.util.List;

public class RentalPostingSpecification {
    public static Specification<RentalPosting> hasOwnerId(Integer ownerId) {
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<RentalPosting> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<RentalPosting> hasPackageAndStatus() {
        return new Specification<RentalPosting>() {
            @Override
            public Predicate toPredicate(Root<RentalPosting> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // Lọc các gói 1, 2, 3, 4
                Predicate packagePredicate = root.get("rentalPackage").get("id").in(1, 2, 3, 4);

                // Trạng thái chỉ áp dụng cho gói 4
                Predicate statusPredicateForPackage4 = cb.and(
                        cb.equal(root.get("rentalPackage").get("id"), 4),
                        root.get("status").in("PendingApproval", "PendingPricing", "AwaitingConfirmation", "RejectPrice")
                );

                // Trạng thái không cần kiểm tra cho gói 1, 2, 3
                Predicate statusPredicateForPackage1to3 = cb.and(
                        root.get("rentalPackage").get("id").in(Arrays.asList(1, 2, 3)), // Đảm bảo sử dụng danh sách
                        cb.or(cb.isNull(root.get("status")), cb.isNotNull(root.get("status"))) // Trạng thái có thể bất kỳ
                );

                return cb.and(packagePredicate, cb.or(statusPredicateForPackage4, statusPredicateForPackage1to3));
            }
        };
    }


    public static Specification<RentalPosting> hasStatuses(List<String> statuses) {
        return (root, query, criteriaBuilder) -> root.get("status").in(statuses);
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