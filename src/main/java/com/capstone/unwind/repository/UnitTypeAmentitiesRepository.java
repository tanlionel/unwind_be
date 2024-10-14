package com.capstone.unwind.repository;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.entity.UnitTypeAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UnitTypeAmentitiesRepository extends JpaRepository<UnitTypeAmenity, Integer> {
    List<UnitTypeAmenity> findAllByUnitTypeId(Integer UnitTypeId);

    void deleteAllByUnitTypeId(Integer id);
}
