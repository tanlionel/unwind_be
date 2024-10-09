package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRespository extends JpaRepository<Policy, Integer> {
    List<Policy> findAllByType(String type);
}
