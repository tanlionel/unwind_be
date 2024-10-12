package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface PolicyRespository extends JpaRepository<Policy, Integer> {
    List<Policy> findAllByType(String type);
}
