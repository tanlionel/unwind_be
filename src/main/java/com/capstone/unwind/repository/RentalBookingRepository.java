package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RentalBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RentalBookingRepository extends JpaRepository<RentalBooking,Integer> {
    Optional<RentalBooking> findByRenter_Id(Integer renterId);
}
