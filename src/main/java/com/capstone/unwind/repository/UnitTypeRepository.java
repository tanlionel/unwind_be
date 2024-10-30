package com.capstone.unwind.repository;

import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.entity.UnitTypeAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {
    List<UnitType> findAllByResortId(Integer resortId);
    List<UnitType> findAllByResortIdAndIsActiveTrue(Integer resortId);

    Optional<UnitType> findByIdAndIsActiveTrue(Integer id);

}