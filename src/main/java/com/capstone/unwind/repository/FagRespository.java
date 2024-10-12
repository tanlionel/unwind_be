package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.ResortPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FagRespository extends JpaRepository < Faq, Integer>{
    List<Faq> findAllByType(String type);

}
