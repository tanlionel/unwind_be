package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Resort;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResortRepository extends JpaRepository<Resort, Integer> {
    Page<Resort> findAllByResortNameContainingAndIsActiveAndTimeshareCompanyId(String resortName, Boolean isActive,Integer tsId, Pageable pageable);
    Page<Resort> findAllByResortNameContainingAndIsActive(String resortName, Boolean isActive, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ResortAmenity a SET a.isActive = false WHERE a.resort.id = :resortId")
    void deactivateExistingAmenities(@Param("resortId") Integer resortId);

    @Query("SELECT ROUND(AVG(f.ratingPoint), 1) FROM Feedback f WHERE f.resort.id = :resortId AND f.isActive = true")
    Float getAverageRatingByResortId(@Param("resortId") Integer resortId);
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.resort.id = :resortId AND f.isActive = true")
    Long countFeedbacksByResortId(@Param("resortId") Integer resortId);

    @Query("SELECT COUNT(r) FROM Resort r")
    Long getTotalResorts();
    @Query("SELECT COUNT(r) FROM Resort r WHERE r.timeshareCompany.id = :tsId")
    Long getTotalResorts(@Param("tsId") Integer tsId);

    @Query("SELECT r FROM Resort r WHERE r.isActive = true ORDER BY RAND()")
    Page<Resort> findTop10ByIsActiveOrderByRandom(Pageable pageable);

}