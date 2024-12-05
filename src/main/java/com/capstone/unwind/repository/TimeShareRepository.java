package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.entity.Timeshare;
import com.capstone.unwind.model.TimeShareDTO.ListTimeShareDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeShareRepository extends JpaRepository<Timeshare, Integer> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Timeshare t " +
            "WHERE t.roomInfo = :roomInfo AND " +
            "((t.startDate <= :endDate AND t.endDate >= :startDate))")
    boolean existsByRoomInfoAndDateRange(@Param("roomInfo") RoomInfo roomInfo,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
    @EntityGraph(attributePaths = {"roomInfo", "roomInfo.resort", "roomInfo.unitType"})
    List<Timeshare> findAllByOwnerId(Integer ownerId);
    Page<Timeshare> findAllByOwnerIdAndIsActive(Integer ownerId, Pageable pageable,Boolean IsActive);
    @Query("SELECT t FROM Timeshare t WHERE t.owner.id = :ownerId AND t.isActive = :isActive ORDER BY t.createdAt DESC")
    Page<Timeshare> findByOwnerIdAndIsActive(Integer ownerId, Boolean isActive, Pageable pageable);
    @Query("""
        SELECT t FROM Timeshare t
        JOIN t.roomInfo r
        JOIN r.resort rs
        JOIN rs.location l
        WHERE t.owner.id = :ownerId
          AND LOWER(l.displayName) LIKE LOWER(CONCAT('%', :preferLocation, '%'))
          AND (
              (MONTH(t.startDate) < MONTH(:preferCheckoutDate) OR 
               (MONTH(t.startDate) = MONTH(:preferCheckoutDate) AND DAY(t.startDate) <= DAY(:preferCheckoutDate)))
              AND
              (MONTH(t.endDate) > MONTH(:preferCheckinDate) OR 
               (MONTH(t.endDate) = MONTH(:preferCheckinDate) AND DAY(t.endDate) >= DAY(:preferCheckinDate)))
          )
        """)
    Page<Timeshare> findFilteredTimeshares(
            @Param("ownerId") Integer ownerId,
            @Param("preferLocation") String preferLocation,
            @Param("preferCheckinDate") LocalDate preferCheckinDate,
            @Param("preferCheckoutDate") LocalDate preferCheckoutDate,
            Pageable pageable);

}
