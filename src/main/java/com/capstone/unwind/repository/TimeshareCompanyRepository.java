package com.capstone.unwind.repository;

import com.capstone.unwind.entity.TimeshareCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeshareCompanyRepository extends JpaRepository<TimeshareCompany, Integer> {
    Page<TimeshareCompany> findAllByTimeshareCompanyNameContaining(String tsName, Pageable pageable);
    TimeshareCompany findTimeshareCompanyById(Integer tsId);
    TimeshareCompany findTimeshareCompanyByOwnerId(Integer ownerId);
    @Query("SELECT COUNT(r) FROM TimeshareCompany r")
    Long getTotalCompany();
}