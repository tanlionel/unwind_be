package com.capstone.unwind.repository;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.entity.MergedBookingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MergedBookingRepository extends JpaRepository<MergedBooking, MergedBookingId> {
    Page<MergedBooking> findAllByRenterId(Integer renterId, Pageable pageable);
    Page<MergedBooking> findAllByCheckinDateOrCheckoutDateAndResortId(LocalDate checkInDate,LocalDate  checkOutDate,Integer resortId,Pageable pageable);
    Page<MergedBooking> findAllByCheckinDateBeforeAndCheckoutDateAfterAndStatusAndResortId(LocalDate checkInDate,LocalDate checkOutDate, String status,Integer resortId, Pageable pageable);
    Page<MergedBooking> findAllByCheckinDateAfterAndStatusAndResortId(LocalDate searchDate, String status, Integer resortId,Pageable pageable);

}