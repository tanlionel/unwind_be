package com.capstone.unwind.repository;

import com.capstone.unwind.entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {
    List<UnitType> findAllByResortId(Integer resortId);
}