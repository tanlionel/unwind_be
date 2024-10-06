package com.capstone.unwind.repository;

import com.capstone.unwind.entity.TimeshareCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeshareCompanyRepository extends JpaRepository<TimeshareCompany, Integer> {
    Page<TimeshareCompany> findAllByTimeshareCompanyNameContaining(String tsName, Pageable pageable);
    TimeshareCompany findTimeshareCompanyById(Integer tsId);
}