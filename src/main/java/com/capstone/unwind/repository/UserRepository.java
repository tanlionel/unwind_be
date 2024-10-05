package com.capstone.unwind.repository;

import com.capstone.unwind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
    User findUserByUserName(String username);
}