package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.ExchangeRequest;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangeRequestBasicDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {
    Page<ExchangeRequest> findAllByIsActiveAndOwnerId(boolean isActive, Integer ownerId, Pageable pageable);
    Page<ExchangeRequest> findAllByIsActiveAndExchangePostingId(boolean isActive,Integer postingId,Pageable pageable);

    Page<ExchangeRequest> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(boolean b, String roomInfoCode, String s, Integer id, Pageable pageable);

    @Query("SELECT r FROM ExchangeRequest r WHERE r.id = :requestId AND r.isActive = true")
    Optional<ExchangeRequest> findByIdAndIsActive(@Param("requestId") Integer requestId);

    @Query("SELECT COUNT(r) FROM ExchangeRequest r WHERE r.owner.id = :ownerId ")
    Long getExchangeRequestByUserId(@Param("ownerId") Integer ownerId);
    Optional<ExchangeRequest> findByExchangePostingIdAndIsActive(Integer postingId,boolean isActive);

    @Modifying
    @Query("UPDATE ExchangeRequest er SET er.status = 'OwnerReject' WHERE er.exchangePosting.id = :exchangePostingId AND er.id <> :excludedRequestId")
    void updateOtherRequestsStatusByExchangePosting(@Param("exchangePostingId") Integer exchangePostingId,
                                                    @Param("excludedRequestId") Integer excludedRequestId);


    @Query("SELECT YEAR(r.startDate) FROM ExchangeRequest r " +
            "LEFT JOIN ExchangeBooking rb ON r.id = rb.exchangeRequest.id " +
            "WHERE r.timeshare.id = :timeshareId " +
            "AND (r.isActive = true " +
            "AND (rb.status IN ('Booked', 'NoShow', 'CheckIn', 'CheckOut') " +
            "   OR (r.status not IN ('Closed')))"+
            "  OR (r.status = 'Complete'))")
    List<Integer> findAllNotValidYears(@Param("timeshareId") Integer timeshareId);



}