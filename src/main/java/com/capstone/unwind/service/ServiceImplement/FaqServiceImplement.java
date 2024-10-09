package com.capstone.unwind.service.ServiceImplement;


import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.FaqMapper;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyMapper;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FaqServiceImplement implements FaqService {
    @Autowired
    private final FagRespository fagRepository;
    @Autowired
    private final FaqMapper faqMapper;

    @Override
    public FaqDTO createFaq(FaqDTO faqDTO) throws EntityAlreadyExist {
        Faq faqRequest = Faq.builder()
                .type(faqDTO.getType())
                .title(faqDTO.getTitle())
                .description(faqDTO.getDescription())
                .build();
        Faq fagDB = fagRepository.save(faqRequest);
        FaqDTO faqDtoDB = faqMapper.toDto(fagDB);
        return faqDtoDB;
    }

    @Override
    public List<FaqDTO> getFaqByType(String type) throws EntityDoesNotExistException {
        List<Faq> faq = fagRepository.findAllByType(type);
        if (faq == null) throw new EntityDoesNotExistException();
        return faqMapper.toDtoList(faq);
    }

    @Override
    public FaqDTO updateFaq(Integer id, FaqDTO faqDTO) throws EntityDoesNotExistException {
        Faq faq = fagRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException());

        faq.setType(faqDTO.getType());
        faq.setTitle(faqDTO.getTitle());
        faq.setDescription(faqDTO.getDescription());
        Faq updatedFaq = fagRepository.save(faq);
        return faqMapper.toDto(updatedFaq);
    }
    @Override
    public List<FaqDTO> findAll() {
        List<Faq> faqList = fagRepository.findAll();
        return faqMapper.toDtoList(faqList);
    }
}