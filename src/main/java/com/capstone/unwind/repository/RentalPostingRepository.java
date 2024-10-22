package com.capstone.unwind.repository;

import com.capstone.unwind.entity.RentalPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalPostingRepository extends JpaRepository<RentalPosting,Integer> {

    List<RentalPosting> findAllByIsActive(boolean isActive);

    List<RentalPosting> findAllByOwnerId(Integer id);

    List<RentalPosting> findAllByOwnerIdAndIsActive(Integer id, boolean isActive);

    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRoomInfo_IsActive(boolean b, String resortName, boolean b1,
                                                                                                     Pageable pageable);

    Page<RentalPosting> findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRoomInfo_IsActiveAndStatus(
            boolean isActive, String resortName, boolean roomInfoIsActive, String status, Pageable pageable);

    Optional<RentalPosting> findByIdAndIsActive(Integer id, Boolean isActive);
}
