package com.capstone.unwind.repository;

import com.capstone.unwind.entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {
    List<UnitType> findAllByResortId(Integer resortId);
}