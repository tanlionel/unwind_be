package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangeRequest;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangeRequestBasicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {
    Page<ExchangeRequest> findAllByIsActiveAndOwnerId(boolean isActive, Integer ownerId, Pageable pageable);
    Page<ExchangeRequest> findAllByIsActiveAndExchangePostingId(boolean isActive,Integer postingId,Pageable pageable);
}