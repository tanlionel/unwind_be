package com.capstone.unwind.repository;

import com.capstone.unwind.entity.ExchangePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangePackageRepository extends JpaRepository<ExchangePackage, Integer> {
}