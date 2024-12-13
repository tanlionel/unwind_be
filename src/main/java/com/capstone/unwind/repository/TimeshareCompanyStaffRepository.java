package com.capstone.unwind.repository;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeshareCompanyStaffRepository extends JpaRepository<TimeshareCompanyStaff, Integer> {

    boolean existsByUserNameAndTimeshareCompany_Id(String userName, Integer timeshareCompanyId);
    Page<TimeshareCompanyStaff> findAllByUserNameContainingAndTimeshareCompanyId(String userName, Integer tsId, Pageable pageable);

    Page<TimeshareCompanyStaff> findAllByTimeshareCompanyId(Integer id, Pageable pageable);
    TimeshareCompanyStaff findTimeshareCompanyStaffByUserNameAndTimeshareCompanyId(String username, Integer tsCompanyId);
    @Query("SELECT COUNT(r) FROM TimeshareCompanyStaff r WHERE r.timeshareCompany.id = :tsId")
    Long getTotalStaffs(@Param("tsId") Integer tsId);
    @Query("SELECT COUNT(r) FROM TimeshareCompanyStaff r WHERE r.isActive =true ")
    Long totalStaffs();
}