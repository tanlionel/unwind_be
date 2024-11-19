package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.Resort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalPostingRepository extends JpaRepository<RentalPosting,Integer>, JpaSpecificationExecutor<RentalPosting> {
    List<RentalPosting> findAllByIsActive(boolean isActive);
    List<RentalPosting> findAllByOwnerId(Integer id);
    List<RentalPosting> findAllByOwnerIdAndIsActive(Integer id, boolean isActive);
    @Query("SELECT rp FROM RentalPosting rp WHERE rp.owner.id = :ownerId AND rp.isActive = true ORDER BY rp.createdDate DESC")
    Page<RentalPosting> findByOwnerIdAndIsActive(@Param("ownerId") Integer ownerId,Pageable pageable);
    @Query("SELECT rp FROM RentalPosting rp WHERE rp.owner.id = :ownerId AND rp.isActive = true AND rp.roomInfo.resort.id = :resortId ORDER BY rp.createdDate DESC")
    Page<RentalPosting> findAllByOwnerIdAndIsActiveAndResortId(
            @Param("ownerId") Integer ownerId,
            @Param("resortId") Integer resortId,Pageable pageable
    );
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRoomInfo_IsActive(boolean b, String resortName, boolean b1,
                                                                                                     Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRoomInfo_IsActiveAndStatus(
            boolean isActive, String resortName, boolean roomInfoIsActive, String status, Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatus(boolean isActive,
          String resortName, String status, Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatusAndRentalPackage_Id(boolean isActive,
                                                                                                             String resortName, String status,Integer packageID, Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRentalPackage_Id(boolean isActive,
                                                                                                             String resortName,Integer packageID, Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(boolean isActive,
                                                                                                   String RoomCode,String status, Integer resortId ,Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_IdAndRentalPackageId(boolean isActive,
                                                                                                          String RoomCode,String status, Integer resortId ,Integer rentalPackageId,Pageable pageable);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContaining(boolean isActive,
                                                                                                    String resortName, Pageable pageable);
    @Query("SELECT r FROM RentalPosting r WHERE r.id = :postingId AND r.isActive = true")
    Optional<RentalPosting> findByIdAndIsActive(@Param("postingId") Integer postingId);

    Page<RentalPosting> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndRoomInfo_Resort_IdAndRentalPackage_IdIn(
            boolean isActive, String roomInfoCode, Integer resortId, List<Integer> packageIds, Pageable pageable);

    @Query("SELECT YEAR(r.checkinDate) FROM RentalPosting r " +
            "LEFT JOIN RentalBooking rb ON r.id = rb.rentalPosting.id " +
            "WHERE r.timeshare.id = :timeshareId " +
            "AND r.isActive = true " +
            "AND (rb.status IN ('Booked', 'NoShow', 'CheckIn', 'CheckOut', 'Refund', 'PaymentCompleted') " +
            "     OR (r.rentalPackage.id = 4 AND r.status not IN ('Closed')))"+
            "   OR(r.rentalPackage.id = 1 and r.status = 'Completed')")
    List<Integer> findAllNotValidYears(@Param("timeshareId") Integer timeshareId);
    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRentalPackage_IdAndStatus(boolean b, String resortName, int i, Pageable pageable, String status);

}
