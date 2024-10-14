package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.entity.Timeshare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeShareRepository extends JpaRepository<Timeshare, Integer> {
List<Timeshare> findAllByOwnerId(Integer ownerId);
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Timeshare t " +
            "WHERE t.roomInfo = :roomInfo AND " +
            "((t.startDate <= :endDate AND t.endDate >= :startDate))")
    boolean existsByRoomInfoAndDateRange(@Param("roomInfo") RoomInfo roomInfo,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

}
