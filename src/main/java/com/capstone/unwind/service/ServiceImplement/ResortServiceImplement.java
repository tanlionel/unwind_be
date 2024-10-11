package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final UnitTypeMapper unitTypeMapper;
    @Autowired
    private final UnitTypeAmentitiesMapper unitTypeAmentitiesMapper;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private final UnitTypeAmentitiesRepository unitTypeAmentitiesRepository;
    @Autowired
    private final UserService userService;

    @Override
    public ResortDetailResponseDTO createResort(ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {

        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany==null) throw new UserDoesNotHavePermission();

        Resort resort = Resort.builder()
                .resortName(resortDto.getResortName())
                .logo(resortDto.getLogo())
                .minPrice(resortDto.getMinPrice())
                .maxPrice(resortDto.getMaxPrice())
                .status(resortDto.getStatus())
                .address(resortDto.getAddress())
                .timeshareCompany(timeshareCompany)
                .status(resortDto.getStatus())
                .description(resortDto.getDescription())
                .isActive(true)
                .build();

        Resort resortInDb = resortRepository.save(resort);

        try{
            for (ResortRequestDTO.ResortAmenity tmp: resortDto.getResortAmenityList()){
                ResortAmenity resortAmenity = ResortAmenity.builder()
                        .resort(resortInDb)
                        .type(tmp.getType())
                        .name(tmp.getName())
                        .isActive(true)
                        .build();
                resortAmenityRepository.save(resortAmenity);
            }
        }catch (Exception e) {
            throw new ErrMessageException("Error when save amenities");
        };

        List<ResortAmenity> resortAmenities = resortAmenityRepository.findAllByResortId(resortInDb.getId());
        ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortInDb.getId())
                .resortName(resortInDb.getResortName())
                .logo(resortInDb.getLogo())
                .minPrice(resortInDb.getMinPrice())
                .maxPrice(resortInDb.getMaxPrice())
                .status(resortInDb.getStatus())
                .address(resortInDb.getAddress())
                .timeshareCompanyId(resortInDb.getTimeshareCompany().getId())
                .description(resortInDb.getDescription())
                .status(resortInDb.getStatus())
                .resortAmenityList( resortAmenities.stream()
                        .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .build())
                        .toList())
                .isActive(resortInDb.getIsActive())
                .build();
        return resortDetailResponseDTO;
    }

    @Override
    public ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany==null) throw new UserDoesNotHavePermission();

        Optional<Resort> resort = resortRepository.findById(resortId);
        if (!resort.isPresent()) throw new EntityDoesNotExistException();
        Resort resortInDb = resort.get();
        List<ResortAmenity> resortAmenityList = resortAmenityRepository.findAllByResortId(resortId);
        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortId(resortInDb.getId()).stream().map(unitTypeMapper::toDto).toList();

        ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortInDb.getId())
                .resortName(resortInDb.getResortName())
                .logo(resortInDb.getLogo())
                .minPrice(resortInDb.getMinPrice())
                .maxPrice(resortInDb.getMaxPrice())
                .status(resortInDb.getStatus())
                .address(resortInDb.getAddress())
                .timeshareCompanyId(resortInDb.getTimeshareCompany().getId())
                .status(resortInDb.getStatus())
                .description(resortInDb.getDescription())
                .resortAmenityList( resortAmenityList.stream()
                        .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .build())
                        .toList())
                .isActive(resortInDb.getIsActive())
                .unitTypeDtoList(unitTypeDtoListResponse)
                .build();
        return resortDetailResponseDTO;
    }

    @Override
    public Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName) throws UserDoesNotHavePermission {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("id"));
        User tsCompany = userService.getLoginUser();

        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany==null) throw new UserDoesNotHavePermission();

        Page<Resort> resortPage = resortRepository.findAllByResortNameContainingAndIsActiveAndTimeshareCompanyId(resortName,true,timeshareCompany.getId(),pageable);
        Page<ResortDto> resortDtoPage = resortPage.map(resortMapper::toDto);
        return resortDtoPage;
    }

    @Override
    public List<UnitTypeDto> createUnitType(ResortUnitTypeRequestDTO resortUnitTypeRequestDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany==null) throw new UserDoesNotHavePermission();

        Resort resort = resortRepository.findById(resortUnitTypeRequestDTO.getResortId()).orElseThrow(EntityDoesNotExistException::new);
        try{
            List<UnitTypeDto> unitTypeDtoList = resortUnitTypeRequestDTO.getUnitTypeDtoList();
            for (UnitTypeDto tmp:unitTypeDtoList){
                UnitType unitType = unitTypeMapper.toEntity(tmp);
                unitType.setResort(resort);
                unitTypeRepository.save(unitType);
            }
        }catch (Exception e){
            throw new ErrMessageException("Error when save unit type");
        }
        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortId(resort.getId()).stream().map(unitTypeMapper::toDto).toList();
        return unitTypeDtoListResponse;
    }
    @Override
    public List<UnitTypeAmenitiesDTO> addAmenitiesToUnitType(AddUnitTypeAmentiesDTO addUnitTypeAmentiesDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany==null) throw new UserDoesNotHavePermission();

        UnitType unitType = unitTypeRepository.findById(addUnitTypeAmentiesDTO.getUnitTypeId())
                .orElseThrow(() -> new ErrMessageException("Unit type not found with id: " + addUnitTypeAmentiesDTO.getUnitTypeId()));

        try{
            List<UnitTypeAmenitiesDTO> unitTypeAmenitiesDTOList = addUnitTypeAmentiesDTO.getUnitTypeAmenitiesDTOS();
            for (UnitTypeAmenitiesDTO tmp:unitTypeAmenitiesDTOList){
                UnitTypeAmenity unitTypeAmenity = unitTypeAmentitiesMapper.toEntity(tmp);
                unitTypeAmenity.setUnitType(unitType);
                unitTypeAmentitiesRepository.save(unitTypeAmenity);
            }
        }catch (Exception e){
            throw new ErrMessageException("Error when save unit type");
        }
        List<UnitTypeAmenitiesDTO> unitTypeAmenitiesDTOListResponse = unitTypeAmentitiesRepository.findAllByUnitTypeId(unitType.getId()).stream().map(unitTypeAmentitiesMapper::toDto).toList();
        return unitTypeAmenitiesDTOListResponse;
    }

}
