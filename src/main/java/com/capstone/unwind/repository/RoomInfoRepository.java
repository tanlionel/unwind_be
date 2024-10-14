package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInfoRepository extends JpaRepository <RoomInfo, Integer>{
}
