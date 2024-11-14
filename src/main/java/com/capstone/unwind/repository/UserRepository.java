package com.capstone.unwind.repository;

import com.capstone.unwind.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
    User findUserByUserName(String username);
    @Query("SELECT u FROM User u WHERE u.userName LIKE %:userName% AND (:roleId IS NULL OR u.role.id = :roleId)")
    Page<User> findAllByUserNameContainingAndRoleId(@Param("userName") String userName,
                                                    @Param("roleId") Integer roleId,
                                                    Pageable pageable);

    User findUserById(Integer userId);
    List<User> findAllByRoleId(Integer roleId);
    boolean existsByEmail(String email);
}