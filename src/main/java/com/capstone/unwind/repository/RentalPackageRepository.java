package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RentalPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalPackageRepository extends JpaRepository<RentalPackage, Integer> {

}