package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomAmenity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomAmentityRepository extends JpaRepository<RoomAmenity , Integer> {
    List<RoomAmenity> findAllByRoomInfoId(Integer roomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoomAmenity a WHERE a.roomInfo.id = :roomInfoId")
    void deleteAmenitiesByRoomInfoId(@Param("roomInfoId") Integer roomInfoId);
}
