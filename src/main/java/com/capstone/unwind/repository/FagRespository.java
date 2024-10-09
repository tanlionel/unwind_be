package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.ResortPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FagRespository extends JpaRepository < Faq, Integer>{
    List<Faq> findAllByType(String type);

}
