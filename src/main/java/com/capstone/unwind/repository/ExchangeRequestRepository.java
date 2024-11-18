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

import java.util.Optional;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {
    Page<ExchangeRequest> findAllByIsActiveAndOwnerId(boolean isActive, Integer ownerId, Pageable pageable);
    Page<ExchangeRequest> findAllByIsActiveAndExchangePostingId(boolean isActive,Integer postingId,Pageable pageable);

    Page<ExchangeRequest> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(boolean b, String roomInfoCode, String s, Integer id, Pageable pageable);

    @Query("SELECT r FROM ExchangeRequest r WHERE r.id = :requestId AND r.isActive = true")
    Optional<ExchangeRequest> findByIdAndIsActive(@Param("requestId") Integer requestId);


}