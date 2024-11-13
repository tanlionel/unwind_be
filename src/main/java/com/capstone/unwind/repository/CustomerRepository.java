package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUserId(Integer userId);
    boolean existsByUserId(Integer userId);

}
