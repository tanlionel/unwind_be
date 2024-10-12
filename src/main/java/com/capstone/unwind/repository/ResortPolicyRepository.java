package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ResortPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResortPolicyRepository extends JpaRepository<ResortPolicy, Integer> {
    List<ResortPolicy> findAllByResortId(Integer resortId);
}