package com.capstone.unwind.repository;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.entity.MergedBookingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MergedBookingRepository extends JpaRepository<MergedBooking, MergedBookingId> {
    Page<MergedBooking> findAllByRenterId(Integer renterId, Pageable pageable);
}