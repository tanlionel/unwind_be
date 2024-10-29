package com.capstone.unwind.repository;

import com.capstone.unwind.entity.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Integer> {
}