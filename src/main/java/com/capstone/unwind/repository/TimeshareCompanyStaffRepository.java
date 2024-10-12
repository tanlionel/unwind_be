package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Resort;
import com.capstone.unwind.entity.TimeshareCompanyStaff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeshareCompanyStaffRepository extends JpaRepository<TimeshareCompanyStaff, Integer> {

    boolean existsByUserNameAndTimeshareCompany_Id(String userName, Integer timeshareCompanyId);
    Page<TimeshareCompanyStaff> findAllByUserNameContainingAndTimeshareCompanyId(String userName, Integer tsId, Pageable pageable);

    Page<TimeshareCompanyStaff> findAllByTimeshareCompanyId(Integer id, Pageable pageable);
}