package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.FaqRequestDTO;

import java.util.List;

public interface FaqService {
    FaqDTO createFaq(FaqRequestDTO faqDTO) throws EntityAlreadyExist;
    List<FaqDTO> getFaqByType(String type) throws EntityDoesNotExistException;
    FaqDTO updateFaq(Integer id, FaqRequestDTO faqDTO) throws EntityDoesNotExistException;
    List<FaqDTO> findAll();
}
