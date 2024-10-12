package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ResortAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResortAmenityRepository extends JpaRepository<ResortAmenity, Integer> {
    List<ResortAmenity> findAllByResortId(Integer resortId);
}