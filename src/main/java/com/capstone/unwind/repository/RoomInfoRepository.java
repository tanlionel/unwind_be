package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomInfoRepository extends JpaRepository <RoomInfo, Integer>{
    boolean existsByRoomInfoCodeAndResortId(String roomInfoCode, Integer id);
    List<RoomInfo> findAllByResortId(Integer resortId);

    List<RoomInfo> findAllByResortIdAndIsActiveTrue(Integer resortId);
}
