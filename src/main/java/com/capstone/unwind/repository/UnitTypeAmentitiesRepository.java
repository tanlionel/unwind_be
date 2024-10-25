package com.capstone.unwind.repository;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.entity.UnitTypeAmenity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UnitTypeAmentitiesRepository extends JpaRepository<UnitTypeAmenity, Integer> {
    List<UnitTypeAmenity> findAllByUnitTypeId(Integer UnitTypeId);

    void deleteAllByUnitTypeId(Integer id);

    List<UnitTypeAmenity> findAllByUnitTypeIdAndIsActiveTrue(Integer id);
    @Modifying
    @Transactional
    @Query("UPDATE UnitTypeAmenity a SET a.isActive = false WHERE a.unitType.id = :unitTypeId")
    void deactivateAmenitiesByUnitTypeId(@Param("unitTypeId") Integer unitTypeId);

}
