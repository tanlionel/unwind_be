package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ResortAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResortAmenityRepository extends JpaRepository<ResortAmenity, Integer> {
    List<ResortAmenity> findAllByResortId(Integer resortId);

    List<ResortAmenity> findAllByResortIdAndIsActiveTrue(Integer id);

    @Modifying
    @Query("UPDATE ResortAmenity a SET a.isActive = false WHERE a.resort.id = :resortId")
    void deactivateExistingAmenities(@Param("resortId") Integer resortId);
}