package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ResortAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResortAmenityRepository extends JpaRepository<ResortAmenity, Integer> {
    List<ResortAmenity> findAllByResortId(Integer resortId);
}