package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.SystemDTO.FaqDTO;

import java.util.List;

public interface FaqService {
    FaqDTO createFaq(FaqDTO faqDTO) throws EntityAlreadyExist;
    List<FaqDTO> getFaqByType(String type) throws EntityDoesNotExistException;
    FaqDTO updateFaq(Integer id, FaqDTO faqDTO) throws EntityDoesNotExistException;
    List<FaqDTO> findAll();
}
