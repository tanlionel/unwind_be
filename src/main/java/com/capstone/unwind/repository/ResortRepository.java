package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Resort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortRepository extends JpaRepository<Resort, Integer> {
    Page<Resort> findAllByResortNameContainingAndIsActive(String resortName, Boolean isActive, Pageable pageable);
}