package com.capstone.unwind.repository;

import com.capstone.unwind.entity.DocumentStore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DocumentStoreRepository extends JpaRepository<DocumentStore, Integer> {
    @Query("SELECT d.imageUrl FROM DocumentStore d WHERE d.entityId = :entityId AND d.type = :type AND d.isActive = true")
    List<String> findUrlsByEntityIdAndType(Integer entityId, String type);
    @Transactional
    @Modifying
    @Query("UPDATE DocumentStore d SET d.isActive = false WHERE d.entityId = :entityId AND d.type = :type")
    void deactivateOldImages(@Param("entityId") Integer entityId, @Param("type") String type);

}