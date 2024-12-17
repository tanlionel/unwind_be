package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangePostingRepository extends JpaRepository<ExchangePosting, Integer>, JpaSpecificationExecutor<ExchangePosting> {
    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndExchangePackage_Id(boolean isActive,
                                                                                                    String resortName, Integer packageID, Pageable pageable);
    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(boolean isActive,
                                                                                                          String RoomCode,String status, Integer resortId ,Pageable pageable);
    @Query("SELECT YEAR(r.checkinDate) FROM ExchangePosting r " +
            "LEFT JOIN ExchangeBooking rb ON r.id = rb.exchangePosting.id " +
            "WHERE r.timeshare.id = :timeshareId " +
            "AND (r.isActive = true " +
            "AND (rb.status IN ('Booked', 'NoShow', 'CheckIn', 'CheckOut') " +
            "   OR (r.status not IN ('Closed')))"+
            "   OR(r.exchangePackage.id = 1 and r.status = 'Completed'))")
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

    @Modifying
    @Transactional
    @Query("UPDATE ExchangePosting rp SET rp.status = 'ClosedBySystem' " +
            "WHERE rp.status = 'Processing' AND rp.owner.id = :ownerId " +
            "AND FUNCTION('YEAR', rp.checkinDate) = :year")
    void closeProcessingExchangePostingsByOwner(@Param("ownerId") Integer ownerId, @Param("year") Integer year);
    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatus(boolean isActive,
                                                                                          String resortName, String status, Pageable pageable);
    @Query("SELECT e FROM ExchangePosting e " +
            "WHERE e.isActive = :isActive " +
            "AND e.roomInfo.resort.resortName LIKE %:resortName% " +
            "AND e.status = :status " +
            "AND (:nights IS NULL OR e.nights = :nights)" +
            "ORDER BY e.createdDate DESC")
    Page<ExchangePosting> findAllByFilters(
            @Param("isActive") boolean isActive,
            @Param("resortName") String resortName,
            @Param("nights") Integer nights,
            @Param("status") String status,
            Pageable pageable);

    Page<ExchangePosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatusAndRoomInfo_Resort_Id(boolean isActive,
                                                                                            String resortName, String status, Pageable pageable,Integer resortId);
    @Query("SELECT e FROM ExchangePosting e " +
            "WHERE e.isActive = :isActive " +
            "AND e.roomInfo.resort.resortName LIKE %:resortName% " +
            "AND e.status = :status " +
            "AND (:nights IS NULL OR e.nights = :nights) "+
            "AND e.roomInfo.resort.id = :resortId " +
            "ORDER BY e.createdDate DESC")
    Page<ExchangePosting> findAllByFiltersWithResortId(
            @Param("isActive") boolean isActive,
            @Param("resortName") String resortName,
            @Param("status") String status,
            @Param("nights") Integer nights,
            @Param("resortId") Integer resortId,
            Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE ExchangePosting ep SET ep.status = :status WHERE ep.id = :exchangePostingId")
    void updateStatusByExchangePostingId(@Param("exchangePostingId") Integer exchangePostingId, @Param("status") String status);


    @Query("SELECT COUNT(r) FROM ExchangePosting r WHERE r.exchangePackage.isActive = true ")
    Long getExchangePackage();
    @Query("SELECT COUNT(r) FROM ExchangePosting r WHERE DATE(r.createdDate) = :date")
    Long countExchangePackageByDateRange(@Param("date") LocalDate date);


    @Query("SELECT COUNT(r) FROM ExchangePosting r WHERE r.owner.id = :ownerId ")
    Long getExchangePostingByUserId(@Param("ownerId") Integer ownerId);

    @Query("SELECT COUNT(r) FROM ExchangePosting r WHERE r.owner.id = :ownerId AND r.status = 'Completed'")
    Long getExchangeRenterByUserId(@Param("ownerId") Integer ownerId);


}