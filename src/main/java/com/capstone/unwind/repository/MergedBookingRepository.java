package com.capstone.unwind.repository;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.entity.MergedBookingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MergedBookingRepository extends JpaRepository<MergedBooking, MergedBookingId> {
    Page<MergedBooking> findAllByRenterId(Integer renterId, Pageable pageable);
    @Query("SELECT mb FROM MergedBooking mb " +
            "WHERE mb.resortId = :resortId AND (mb.checkinDate = :checkInDate OR mb.checkoutDate = :checkOutDate)")
    Page<MergedBooking> findByResortIdAndCheckinOrCheckoutDate(
            @Param("resortId") Integer resortId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            Pageable pageable
    );


    Page<MergedBooking> findByResortIdAndCheckinDateBeforeAndCheckoutDateAfterAndStatus(
            Integer resortId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            String status,
            Pageable pageable
    );

    Page<MergedBooking> findByResortIdAndCheckinDateAfterAndStatus(
            Integer resortId,
            LocalDate searchDate,
            String status,
            Pageable pageable
    );


}