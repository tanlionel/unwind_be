package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Resort;
import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.entity.ResortPolicy;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.repository.ResortAmenityRepository;
import com.capstone.unwind.repository.ResortPolicyRepository;
import com.capstone.unwind.repository.ResortRepository;
import com.capstone.unwind.repository.TimeshareCompanyRepository;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ResortServiceImplement implements ResortService {
    @Autowired
    private final ResortRepository resortRepository;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;
    @Autowired
    private final ResortMapper resortMapper;
    @Autowired
    private final ResortAmenityRepository resortAmenityRepository;
    @Autowired
    private final ResortPolicyRepository resortPolicyRepository;
    @Override
    public ResortDto createResort(ResortDto resortDto) throws EntityDoesNotExistException {
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyById(resortDto.getTimeshareCompanyId());
        if (timeshareCompany == null) throw new EntityDoesNotExistException();
        Resort resort = resortMapper.toEntity(resortDto);
        Resort resortDb = resortRepository.save(resort);
        return resortMapper.toDto(resortDb);
    }

    @Override
    public ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException {
        Optional<Resort> resort = resortRepository.findById(resortId);
        if (!resort.isPresent()) throw new EntityDoesNotExistException();
        List<ResortAmenity> resortAmenityList = resortAmenityRepository.findAllByResortId(resortId);
        List<ResortPolicy> resortPolicyList = resortPolicyRepository.findAllByResortId(resortId);
        ResortDetailResponseDTO  resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortId)
                .resortName(resort.get().getResortName())
                .logo(resort.get().getResortName())
                .minPrice(resort.get().getMinPrice())
                .maxPrice(resort.get().getMaxPrice())
                .status(resort.get().getStatus())
                .timeshareCompanyId(resort.get().getTimeshareCompany().getId())
                .address(resort.get().getAddress())
                .isActive(resort.get().getIsActive())
                .resortAmenityList(convertToResortAmenities(resortAmenityList))
                .resortPolicyList(convertToResortPolicies(resortPolicyList))
                .build();
        return resortDetailResponseDTO;
    }

    @Override
    public Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("id"));
        Page<Resort> resortPage = resortRepository.findAllByResortNameContainingAndIsActive(resortName,true,pageable);
        Page<ResortDto> resortDtoPage = resortPage.map(resortMapper::toDto);
        return resortDtoPage;
    }

    @Override
    public Boolean createResortAmenities(ResortAmenitiesRequestDTO resortAmenitiesRequestDTO) throws EntityDoesNotExistException {

        Resort resort = resortRepository.findById(resortAmenitiesRequestDTO.resortId()).orElseThrow(EntityDoesNotExistException::new);
        try{
            for (ResortAmenitiesRequestDTO.ResortAmenity tmp : resortAmenitiesRequestDTO.resortAmenityList()){
                ResortAmenity resortAmenity = ResortAmenity.builder()
                        .type(tmp.type())
                        .name(tmp.name())
                        .isActive(true)
                        .resort(resort)
                        .build();
                resortAmenityRepository.save(resortAmenity);
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public Boolean createResortPolicies(ResortPoliciesRequestDto resortPoliciesRequestDto) throws EntityDoesNotExistException {
        Resort resort = resortRepository.findById(resortPoliciesRequestDto.resortId()).orElseThrow(EntityDoesNotExistException::new);
        try{
            for (ResortPoliciesRequestDto.ResortPolicy tmp : resortPoliciesRequestDto.resortPolicyList()){
                ResortPolicy resortPolicy = ResortPolicy.builder()
                        .description(tmp.description())
                        .attachmentUrl(tmp.attachmentURl())
                        .isActive(true)
                        .resort(resort)
                        .build();
                resortPolicyRepository.save(resortPolicy);
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }

    public List<ResortDetailResponseDTO.ResortAmenity> convertToResortAmenities(List<ResortAmenity> resortAmenityList) {
        return resortAmenityList.stream()
                .map(resortAmenity -> new ResortDetailResponseDTO.ResortAmenity(
                        resortAmenity.getName(),
                        resortAmenity.getType()
                ))
                .collect(Collectors.toList());
    }
    public List<ResortDetailResponseDTO.ResortPolicy> convertToResortPolicies(List<ResortPolicy> resortPolicyList) {
        return resortPolicyList.stream()
                .map(resortPolicy -> new ResortDetailResponseDTO.ResortPolicy(
                        resortPolicy.getDescription(),
                        resortPolicy.getAttachmentUrl()
                ))
                .collect(Collectors.toList());
    }

}
