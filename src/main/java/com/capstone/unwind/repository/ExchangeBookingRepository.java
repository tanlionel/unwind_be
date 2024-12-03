package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangeBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ExchangeBookingRepository extends JpaRepository<ExchangeBooking,Integer> {
    Optional<ExchangeBooking> findByRenter_Id(Integer id);
    @Query("SELECT COUNT(r) FROM ExchangeBooking r WHERE r.renter.id = :ownerId")
    Long getExchangeBookingByUserId(@Param("ownerId") Integer ownerId);
}
