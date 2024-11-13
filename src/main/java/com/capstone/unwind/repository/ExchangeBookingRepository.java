package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangeBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ExchangeBookingRepository extends JpaRepository<ExchangeBooking,Integer> {
    Optional<ExchangeBooking> findByRenter_Id(Integer id);
}
