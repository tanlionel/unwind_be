package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RentalBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RentalBookingRepository extends JpaRepository<RentalBooking,Integer> {
    Optional<RentalBooking> findByRenter_Id(Integer renterId);

    @Query("SELECT COUNT(r) FROM RentalBooking r WHERE r.renter.id = :ownerId")
    Long getRentalBookingByUserId(@Param("ownerId") Integer ownerId);




}
