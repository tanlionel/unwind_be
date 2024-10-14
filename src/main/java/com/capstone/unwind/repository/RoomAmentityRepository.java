package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomAmentityRepository extends JpaRepository<RoomAmenity , Integer> {
    List<RoomAmenity> findAllByRoomInfoId(Integer roomId);
}
