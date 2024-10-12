package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.FaqRequestDTO;
import com.capstone.unwind.model.SystemDTO.PolicyDTO;
import com.capstone.unwind.model.SystemDTO.PolicyRequestDTO;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.service.ServiceInterface.FaqService;
import com.capstone.unwind.service.ServiceInterface.PolicyService;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/system-staff/")
@RequiredArgsConstructor
@CrossOrigin
public class SystemStaffController {
    @Autowired
    private final FaqService faqService;
    @Autowired
    private final PolicyService policyService;
    @PostMapping("faq")
    public FaqDTO createFaq(@RequestBody FaqRequestDTO faqDTO) throws EntityAlreadyExist {
        FaqDTO faqDTOResponse = faqService.createFaq(faqDTO);
        return faqDTOResponse;
    }
    @GetMapping("faq/{type}")
    public List<FaqDTO> getFaqByType(@PathVariable String type) throws EntityDoesNotExistException {
        List<FaqDTO> faqDTO = faqService.getFaqByType(type);
        return faqDTO;
    }
    @PutMapping("/faq/{id}")
    public FaqDTO updateFaq(@PathVariable Integer id, @RequestBody FaqRequestDTO faqDTO) throws EntityDoesNotExistException {
        return faqService.updateFaq(id, faqDTO);
    }
    @GetMapping("/faq/all")
    public List<FaqDTO> getAllFaq() {
        return faqService.findAll();
    }


    @PostMapping("policy")
    public PolicyDTO createPolicy(@RequestBody PolicyRequestDTO policyDTO) throws EntityAlreadyExist {
        PolicyDTO policyDTOResponse = policyService.createPolicy(policyDTO);
        return policyDTOResponse;
    }
    @GetMapping("policy/{type}")
    public List<PolicyDTO > getPolicyByType(@PathVariable String type) throws EntityDoesNotExistException {
        List<PolicyDTO> policyDTO = policyService.getPolicyByType(type);
        return policyDTO;
    }
    @PutMapping("/policy/{id}")
    public PolicyDTO updatePolicy(@PathVariable Integer id, @RequestBody PolicyRequestDTO policyDTO) throws EntityDoesNotExistException {
        return policyService.updatePolicy(id, policyDTO);
    }
    @GetMapping("/policy/all")
    public List<PolicyDTO> getAllPolicy() {
        return policyService.findAll();
    }
}
