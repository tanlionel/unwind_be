package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository <Feedback,Integer>{
    Page<Feedback> findAllByResort_IdAndIsActive(Integer resortId,boolean isActive,
                                      Pageable pageable);
    List<Feedback> findAllByResort_IdAndIsActive(Integer resortId, boolean isActive);
    @Query(value = "SELECT f FROM Feedback f WHERE f.resort.id = :resortId AND f.isActive = true ORDER BY f.createdDate DESC")
    Page<Feedback> findByResortIdAndIsActive(@Param("resortId") Integer resortId, Pageable pageable);
    @Query(value = "SELECT f FROM Feedback f WHERE f.resort.id = :resortId AND f.isActive = true AND f.isReport = :isReport")
    Page<Feedback> findByResortIdAndIsActiveAndIsReport(
            @Param("resortId") Integer resortId,
            @Param("isReport") Boolean isReport,
            Pageable pageable);
    @Query(value = "SELECT f FROM Feedback f WHERE f.resort.id = :resortId AND f.isActive = true ORDER BY f.createdDate DESC")
    List<Feedback> findTop8ByResortIdAndIsActive(@Param("resortId") Integer resortId, Pageable pageable);
}
