package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangePostingRepository extends JpaRepository<ExchangePosting, Integer> {
    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndExchangePackage_Id(boolean isActive,
                                                                                                    String resortName, Integer packageID, Pageable pageable);
    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(boolean isActive,
                                                                                                          String RoomCode,String status, Integer resortId ,Pageable pageable);
    @Query("SELECT YEAR(r.checkinDate) FROM ExchangePosting r " +
            "LEFT JOIN ExchangeBooking rb ON r.id = rb.exchangePosting.id " +
            "WHERE r.timeshare.id = :timeshareId " +
            "AND r.isActive = true " +
            "AND (rb.status IN ('Booked', 'NoShow', 'CheckIn', 'CheckOut') " +
            "   AND r.status not IN ('Closed'))"+
            "   OR(r.exchangePackage.id = 1 and r.status = 'Completed')")
    List<Integer> findAllNotValidYears(@Param("timeshareId") Integer timeshareId);

    @Query("SELECT r FROM ExchangePosting r WHERE r.id = :postingId AND r.isActive = true")
    Optional<ExchangePosting> findByIdAndIsActive(@Param("postingId") Integer postingId);

    @Query("SELECT rp FROM ExchangePosting rp WHERE rp.owner.id = :ownerId AND rp.isActive = true ORDER BY rp.createdDate DESC")
    Page<ExchangePosting> findByOwnerIdAndIsActive(@Param("ownerId") Integer ownerId,Pageable pageable);
    @Query("SELECT rp FROM ExchangePosting rp WHERE rp.owner.id = :ownerId AND rp.isActive = true AND rp.roomInfo.resort.id = :resortId ORDER BY rp.createdDate DESC")
    Page<ExchangePosting> findAllByOwnerIdAndIsActiveAndResortId(
            @Param("ownerId") Integer ownerId,
            @Param("resortId") Integer resortId,Pageable pageable
    );

    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatus(boolean isActive,
                                                                                          String resortName, String status, Pageable pageable);

}