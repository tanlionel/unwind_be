package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ResortPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResortPolicyRepository extends JpaRepository<ResortPolicy, Integer> {
    List<ResortPolicy> findAllByResortId(Integer resortId);
}