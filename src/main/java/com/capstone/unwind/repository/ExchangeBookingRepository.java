package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangeBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeBookingRepository extends JpaRepository<ExchangeBooking,Integer> {
    Optional<ExchangeBooking> findByRenter_Id(Integer id);
}
