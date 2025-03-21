package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.DocumentStoreEnum;
import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.capstone.unwind.config.EmailMessageConfig.*;

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
    private final UnitTypesMapper unitTypesMapper;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private final FeedbackRepository feedbackRepository;
    @Autowired
    private final UnitTypeAmentitiesRepository unitTypeAmentitiesRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final DocumentStoreRepository documentStoreRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final SendinblueService sendinblueService;
    @Autowired
    private final ResortRandomMapper resortRandomMapper;

    @Override
    public ResortDetailResponseDTO createResort(ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {

        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        Resort resort = Resort.builder()
                .resortName(resortDto.getResortName())
                .logo(resortDto.getLogo())
                .minPrice(resortDto.getMinPrice())
                .maxPrice(resortDto.getMaxPrice())
                .status(resortDto.getStatus())
                .location(Location.builder()
                        .name(resortDto.getLocation().getName())
                        .displayName(resortDto.getLocation().getDisplayName())
                        .latitude(resortDto.getLocation().getLatitude())
                        .longitude(resortDto.getLocation().getLongitude())
                        .country(resortDto.getLocation().getCountry())
                        .placeId(resortDto.getLocation().getPlaceId())
                        .build())
                .timeshareCompany(timeshareCompany)
                .status(resortDto.getStatus())
                .description(resortDto.getDescription())
                .isActive(true)
                .build();

        Resort resortInDb = resortRepository.save(resort);

        try {
            for (ResortRequestDTO.ResortAmenity tmp : resortDto.getResortAmenityList()) {
                ResortAmenity resortAmenity = ResortAmenity.builder()
                        .resort(resortInDb)
                        .type(tmp.getType())
                        .name(tmp.getName())
                        .isActive(true)
                        .build();
                resortAmenityRepository.save(resortAmenity);
            }

        } catch (Exception e) {
            throw new ErrMessageException("Error when save amenities");
        }
        try {
            for (String imageUrl : resortDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.Resort.toString());
                document.setEntityId(resortInDb.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        List<ResortAmenity> resortAmenities = resortAmenityRepository.findAllByResortId(resortInDb.getId());
        ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortInDb.getId())
                .resortName(resortInDb.getResortName())
                .logo(resortInDb.getLogo())
                .minPrice(resortInDb.getMinPrice())
                .maxPrice(resortInDb.getMaxPrice())
                .status(resortInDb.getStatus())
                .location(ResortDetailResponseDTO.LocationDTO.builder()
                        .name(resortInDb.getLocation().getName())
                        .displayName(resortInDb.getLocation().getDisplayName())
                        .latitude(resortInDb.getLocation().getLatitude())
                        .longitude(resortInDb.getLocation().getLongitude())
                        .country(resortInDb.getLocation().getCountry())
                        .placeId(resortInDb.getLocation().getPlaceId())
                        .build())
                .timeshareCompanyId(resortInDb.getTimeshareCompany().getId())
                .description(resortInDb.getDescription())
                .status(resortInDb.getStatus())
                .resortAmenityList(resortAmenities.stream()
                        .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .build())
                        .toList())
                .isActive(resortInDb.getIsActive())
                .build();
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject(RESORT_CREATION_SUBJECT);
            emailRequestDto.setContent(RESORT_CREATION_CONTENT);
            sendinblueService.sendEmailWithTemplate(
                    timeshareCompany.getOwner().getEmail(),
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return resortDetailResponseDTO;
    }
    @Transactional
    @Override
    public ResortDetailResponseDTO updateResort(Integer resortId, ResortRequestDTO resortRequestDTO)
            throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {

        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        Resort resort = resortRepository.findById(resortId)
                .orElseThrow(() -> new ErrMessageException("resort with ID " + resortId + " does not exist"));

        if (!resort.getTimeshareCompany().getId().equals(timeshareCompany.getId())) {
            throw new ErrMessageException("resort with ID " + resortId + "is not owned by your company");
        }

        resort.setResortName(resortRequestDTO.getResortName());
        resort.setLogo(resortRequestDTO.getLogo());
        resort.setMinPrice(resortRequestDTO.getMinPrice());
        resort.setMaxPrice(resortRequestDTO.getMaxPrice());
        resort.setStatus(resortRequestDTO.getStatus());
        Location location = resort.getLocation();
        if (location == null) {
            location = new Location();
        }

        ResortRequestDTO.LocationDTO locationDTO = resortRequestDTO.getLocation();
        location.setName(locationDTO.getName());
        location.setDisplayName(locationDTO.getDisplayName());
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setCountry(locationDTO.getCountry());
        location.setPlaceId(locationDTO.getPlaceId());
        resort.setLocation(location);
        resort.setTimeshareCompany(timeshareCompany);
        resort.setDescription(resortRequestDTO.getDescription());
        resort.setIsActive(true);

        Resort updatedResort = resortRepository.save(resort);
        documentStoreRepository.deactivateOldImages(updatedResort.getId(), DocumentStoreEnum.Resort.toString());

        try {
            for (String imageUrl : resortRequestDTO.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.Resort.toString());
                document.setEntityId(updatedResort.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
            resortAmenityRepository.deleteExistingAmenities(updatedResort.getId());
            List<ResortAmenity> newAmenities = resortRequestDTO.getResortAmenityList().stream()
                    .map(tmp -> ResortAmenity.builder()
                            .resort(updatedResort)
                            .name(tmp.getName())
                            .type(tmp.getType())
                            .isActive(true)
                            .build())
                    .toList();
            resortAmenityRepository.saveAll(newAmenities);

            List<ResortAmenity> activeAmenities = resortAmenityRepository.findAllByResortIdAndIsActiveTrue(updatedResort.getId());
            ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                    .id(updatedResort.getId())
                    .resortName(updatedResort.getResortName())
                    .logo(updatedResort.getLogo())
                    .minPrice(updatedResort.getMinPrice())
                    .maxPrice(updatedResort.getMaxPrice())
                    .status(updatedResort.getStatus())
                    .location(ResortDetailResponseDTO.LocationDTO.builder()
                            .name(resort.getLocation().getName())
                            .displayName(resort.getLocation().getDisplayName())
                            .latitude(resort.getLocation().getLatitude())
                            .longitude(resort.getLocation().getLongitude())
                            .country(resort.getLocation().getCountry())
                            .placeId(resort.getLocation().getPlaceId())
                            .build())
                    .timeshareCompanyId(updatedResort.getTimeshareCompany().getId())
                    .description(updatedResort.getDescription())
                    .resortAmenityList(activeAmenities.stream()
                            .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                    .name(p.getName())
                                    .type(p.getType())
                                    .build())
                            .toList())
                    .isActive(updatedResort.getIsActive())
                    .build();

            return resortDetailResponseDTO;
        }
    @Override
    public ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();
        Float averageRating = resortRepository.getAverageRatingByResortId(resortId);
        Long rating = resortRepository.countFeedbacksByResortId(resortId);
        Optional<Resort> resort = resortRepository.findById(resortId);
        if (!resort.isPresent()) throw new EntityDoesNotExistException();
        Resort resortInDb = resort.get();
        List<ResortAmenity> resortAmenityList = resortAmenityRepository.findAllByResortIdAndIsActiveTrue(resortId);
        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortIdAndIsActiveTrue(resortInDb.getId()).stream().map(unitTypeMapper::toDto).toList();
        Pageable pageable = PageRequest.of(0, 8);
        List<Feedback> feedbackList = feedbackRepository.findTop8ByResortIdAndIsActive(resortId,pageable);
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(resortInDb.getId(), DocumentStoreEnum.Resort.toString());
        //mapping unit type amenities
        for (UnitTypeDto tmp : unitTypeDtoListResponse) {
            List<UnitTypeAmenity> unitTypeAmenities = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(tmp.getId());
            tmp.setUnitTypeAmenitiesList(unitTypeAmenities.stream().map(p -> UnitTypeDto.UnitTypeAmenities.builder()
                    .name(p.getName())
                    .type(p.getType())
                    .isActive(p.getIsActive())
                    .build()).toList());
        }

        ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortInDb.getId())
                .resortName(resortInDb.getResortName())
                .logo(resortInDb.getLogo())
                .minPrice(resortInDb.getMinPrice())
                .maxPrice(resortInDb.getMaxPrice())
                .status(resortInDb.getStatus())
                .location(resortInDb.getLocation() != null ?
                        ResortDetailResponseDTO.LocationDTO.builder()
                                .name(resortInDb.getLocation().getName())
                                .displayName(resortInDb.getLocation().getDisplayName())
                                .latitude(resortInDb.getLocation().getLatitude())
                                .longitude(resortInDb.getLocation().getLongitude())
                                .country(resortInDb.getLocation().getCountry())
                                .placeId(resortInDb.getLocation().getPlaceId())
                                .build()
                        : null)
                .timeshareCompanyId(resortInDb.getTimeshareCompany().getId())
                .status(resortInDb.getStatus())
                .imageUrls(imageUrls)
                .averageRating(averageRating)
                .totalRating(rating)
                .description(resortInDb.getDescription())
                .resortAmenityList(resortAmenityList.stream()
                        .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .build())
                        .toList())
                .feedbackList(feedbackList.stream()
                        .map(feedback -> ResortDetailResponseDTO.Feedback.builder()
                                .comment(feedback.getComment())
                                .createdDate(feedback.getCreatedDate())
                                .ratingPoint(feedback.getRatingPoint())
                                .user(ResortDetailResponseDTO.CustomerDto.builder()
                                        .fullName(feedback.getCustomer().getFullName())
                                        .avatar(feedback.getCustomer().getAvatar())
                                        .build())
                                .isActive(feedback.getIsActive())
                                .build())
                        .toList())
                .isActive(resortInDb.getIsActive())
                .unitTypeDtoList(unitTypeDtoListResponse)
                .build();
        return resortDetailResponseDTO;
    }

    @Override
    public Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName) throws UserDoesNotHavePermission {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        User tsCompany = userService.getLoginUser();

        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        Page<Resort> resortPage = resortRepository.findAllByResortNameContainingAndIsActiveAndTimeshareCompanyId(resortName, true, timeshareCompany.getId(), pageable);
        Page<ResortDto> resortDtoPage = resortPage.map(resort -> {
            ResortDto resortDto = resortMapper.toDto(resort);


            Float averageRating = resortRepository.getAverageRatingByResortId(resort.getId());
            Long rating = resortRepository.countFeedbacksByResortId(resort.getId());
            resortDto.setAverageRating(averageRating != null ? averageRating : 0);
            resortDto.setTotalRating(rating != null ? rating : 0);
            return resortDto;
        });
        return resortDtoPage;
    }

    @Override
    public List<ResortRandomDto> getRandomResorts() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Resort> resorts = resortRepository.findTop10ByIsActiveOrderByRandom(pageable);

        List<ResortRandomDto> resortDtoList = resorts.stream().map(resort -> {
            ResortRandomDto resortDto = resortRandomMapper.toDto(resort);

            return resortDto;
        }).collect(Collectors.toList());

        return resortDtoList;
    }

    @Override
    public ResortDetailResponseDTO getPublicResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {

        Optional<Resort> resort = resortRepository.findById(resortId);
        if (!resort.isPresent()) throw new EntityDoesNotExistException();
        Resort resortInDb = resort.get();
        List<ResortAmenity> resortAmenityList = resortAmenityRepository.findAllByResortIdAndIsActiveTrue(resortId);
        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortIdAndIsActiveTrue(resortInDb.getId()).stream().map(unitTypeMapper::toDto).toList();
        Pageable pageable = PageRequest.of(0, 8);
        List<Feedback> feedbackList = feedbackRepository.findTop8ByResortIdAndIsActive(resortId,pageable);
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(resortInDb.getId(), DocumentStoreEnum.Resort.toString());
        //mapping unit type amenities
        Float averageRating = resortRepository.getAverageRatingByResortId(resortId);
        Long rating = resortRepository.countFeedbacksByResortId(resortId);
        for (UnitTypeDto tmp : unitTypeDtoListResponse) {
            List<UnitTypeAmenity> unitTypeAmenities = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(tmp.getId());
            tmp.setUnitTypeAmenitiesList(unitTypeAmenities.stream().map(p -> UnitTypeDto.UnitTypeAmenities.builder()
                    .name(p.getName())
                    .type(p.getType())
                    .isActive(p.getIsActive())
                    .build()).toList());
        }

        ResortDetailResponseDTO resortDetailResponseDTO = ResortDetailResponseDTO.builder()
                .id(resortInDb.getId())
                .resortName(resortInDb.getResortName())
                .logo(resortInDb.getLogo())
                .minPrice(resortInDb.getMinPrice())
                .maxPrice(resortInDb.getMaxPrice())
                .status(resortInDb.getStatus())
                .location(resortInDb.getLocation() != null ?
                        ResortDetailResponseDTO.LocationDTO.builder()
                                .name(resortInDb.getLocation().getName())
                                .displayName(resortInDb.getLocation().getDisplayName())
                                .latitude(resortInDb.getLocation().getLatitude())
                                .longitude(resortInDb.getLocation().getLongitude())
                                .country(resortInDb.getLocation().getCountry())
                                .placeId(resortInDb.getLocation().getPlaceId())
                                .build()
                        : null)
                .timeshareCompanyId(resortInDb.getTimeshareCompany().getId())
                .status(resortInDb.getStatus())
                .description(resortInDb.getDescription())
                .averageRating(averageRating)
                .totalRating(rating)
                .resortAmenityList(resortAmenityList.stream()
                        .map(p -> ResortDetailResponseDTO.ResortAmenity.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .build())
                        .toList())
                .isActive(resortInDb.getIsActive())
                .feedbackList(feedbackList.stream()
                        .map(feedback -> ResortDetailResponseDTO.Feedback.builder()
                                .comment(feedback.getComment())
                                .createdDate(feedback.getCreatedDate())
                                .ratingPoint(feedback.getRatingPoint())
                                .user(ResortDetailResponseDTO.CustomerDto.builder()
                                        .fullName(feedback.getCustomer().getFullName())
                                        .avatar(feedback.getCustomer().getAvatar())
                                        .build())
                                .isActive(feedback.getIsActive())
                                .build())
                        .toList())
                .unitTypeDtoList(unitTypeDtoListResponse)
                .imageUrls(imageUrls)
                .build();
        return resortDetailResponseDTO;
    }

    @Override
    public Page<ResortDto> getPublicPageableResort(Integer pageNo, Integer pageSize, String resortName, Integer resortId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        Page<Resort> resortPage = null;
        if(resortId!=null){
             resortPage= resortRepository.findAllByResortNameContainingAndIsActiveAndTimeshareCompanyId(
                    resortName, true,resortId, pageable );
        }else{
            resortPage = resortRepository.findAllByResortNameContainingAndIsActive(resortName, true, pageable);
        }
        Page<ResortDto> resortDtoPage = resortPage.map(resort -> {
            ResortDto resortDto = resortMapper.toDto(resort);


            Float averageRating = resortRepository.getAverageRatingByResortId(resort.getId());
            Long rating = resortRepository.countFeedbacksByResortId(resort.getId());
            resortDto.setAverageRating(averageRating != null ? averageRating : 0);
            resortDto.setTotalRating(rating != null ? rating : 0);
            return resortDto;
        });
        return resortDtoPage;
    }

    @Override
    public UnitTypeResponseDTO createUnitType(UnitTypeRequestDTO unitTypeRequestDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        Resort resort = resortRepository.findById(unitTypeRequestDTO.getResortId()).orElseThrow(EntityDoesNotExistException::new);

        UnitType unitType = UnitType.builder()
                .area(unitTypeRequestDTO.getArea())
                .bedrooms(unitTypeRequestDTO.getBedrooms())
                .bedsKing(unitTypeRequestDTO.getBedsKing())
                .bedsFull(unitTypeRequestDTO.getBedsFull())
                .bedsMurphy(unitTypeRequestDTO.getBedsMurphy())
                .bedsQueen(unitTypeRequestDTO.getBedsQueen())
                .title(unitTypeRequestDTO.getTitle())
                .bedsSofa(unitTypeRequestDTO.getBedsSofa())
                .description(unitTypeRequestDTO.getDescription())
                .resort(resort)
                .bathrooms(unitTypeRequestDTO.getBathrooms())
                .bedsTwin(unitTypeRequestDTO.getBedsTwin())
                .price(unitTypeRequestDTO.getPrice())
                .view(unitTypeRequestDTO.getView())
                .kitchen(unitTypeRequestDTO.getKitchen())
                .photos(unitTypeRequestDTO.getPhotos())
                .sleeps(unitTypeRequestDTO.getSleeps())
                .buildingsOption(unitTypeRequestDTO.getBuildingsOption())
                .isActive(true)
                .build();

        UnitType unitTypeInDb = unitTypeRepository.save(unitType);

        try {
            for (UnitTypeRequestDTO.UnitTypeAmenitiesDTO tmp : unitTypeRequestDTO.getUnitTypeAmenitiesDTOS()) {
                UnitTypeAmenity amenity = UnitTypeAmenity.builder()
                        .unitType(unitTypeInDb)
                        .name(tmp.getName())
                        .type(tmp.getType())
                        .isActive(true)
                        .build();
                unitTypeAmentitiesRepository.save(amenity);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when save amenities");
        }
        ;

        List<UnitTypeAmenity> amenities = unitTypeAmentitiesRepository.findAllByUnitTypeId(unitTypeInDb.getId());

        UnitTypeResponseDTO unitTypeResponseDTO = UnitTypeResponseDTO.builder()
                .id(unitTypeInDb.getId())
                .area(unitTypeInDb.getArea())
                .bedsFull(unitTypeInDb.getBedsFull())
                .bedsKing(unitTypeInDb.getBedsKing())
                .bedrooms(unitTypeInDb.getBedrooms())
                .bedsQueen(unitTypeInDb.getBedsQueen())
                .bedsSofa(unitTypeInDb.getBedsSofa())
                .bedsTwin(unitTypeInDb.getBedsTwin())
                .description(unitTypeInDb.getDescription())
                .bathrooms(unitTypeInDb.getBathrooms())
                .bedsMurphy(unitTypeInDb.getBedsMurphy())
                .price(unitTypeInDb.getPrice())
                .kitchen(unitTypeInDb.getKitchen())
                .photos(unitTypeInDb.getPhotos())
                .title(unitTypeInDb.getTitle())
                .resortId(unitTypeInDb.getResort().getId())
                .buildingsOption(unitTypeInDb.getBuildingsOption())
                .sleeps(unitTypeInDb.getSleeps())
                .view(unitTypeInDb.getView())
                .unitTypeAmenitiesDTOS(amenities.stream()
                        .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .isActive(p.getIsActive())
                                .build())
                        .toList())
                .isActive(unitTypeInDb.getIsActive())
                .build();
        return unitTypeResponseDTO;
    }

    @Override
    public UnitTypeResponseDTO updateUnitType(Integer unitTypeId, UnitTypeRequestDTO unitTypeRequestDTO)
            throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {

        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        Resort resort = resortRepository.findById(unitTypeRequestDTO.getResortId())
                .orElseThrow(() -> new ErrMessageException("Resort with ID " + unitTypeRequestDTO.getResortId() + " does not exist"));

        if (!resort.getTimeshareCompany().getId().equals(timeshareCompany.getId())) {
            throw new ErrMessageException("Resort with ID " + unitTypeRequestDTO.getResortId() + "is not owned by your company");
        }

        UnitType unitType = unitTypeRepository.findById(unitTypeId)
                .orElseThrow(() -> new ErrMessageException("unitType with ID " + unitTypeId + " does not exist"));

        if (!unitType.getResort().getTimeshareCompany().getId().equals(timeshareCompany.getId())) {
            throw new ErrMessageException("unitType with ID " + unitTypeId + "is not owned by your company");
        }
        unitType.setArea(unitTypeRequestDTO.getArea());
        unitType.setBedrooms(unitTypeRequestDTO.getBedrooms());
        unitType.setBedsKing(unitTypeRequestDTO.getBedsKing());
        unitType.setBedsFull(unitTypeRequestDTO.getBedsFull());
        unitType.setBedsMurphy(unitTypeRequestDTO.getBedsMurphy());
        unitType.setBedsQueen(unitTypeRequestDTO.getBedsQueen());
        unitType.setTitle(unitTypeRequestDTO.getTitle());
        unitType.setBedsSofa(unitTypeRequestDTO.getBedsSofa());
        unitType.setDescription(unitTypeRequestDTO.getDescription());
        unitType.setBathrooms(unitTypeRequestDTO.getBathrooms());
        unitType.setBedsTwin(unitTypeRequestDTO.getBedsTwin());
        unitType.setResort(resort);
        unitType.setPrice(unitTypeRequestDTO.getPrice());
        unitType.setView(unitTypeRequestDTO.getView());
        unitType.setKitchen(unitTypeRequestDTO.getKitchen());
        unitType.setPhotos(unitTypeRequestDTO.getPhotos());
        unitType.setSleeps(unitTypeRequestDTO.getSleeps());
        unitType.setBuildingsOption(unitTypeRequestDTO.getBuildingsOption());
        unitType.setIsActive(true);

        UnitType updatedUnitType = unitTypeRepository.save(unitType);
        unitTypeAmentitiesRepository.deleteAmenitiesByUnitTypeId(updatedUnitType.getId());
        try {
            for (UnitTypeRequestDTO.UnitTypeAmenitiesDTO tmp : unitTypeRequestDTO.getUnitTypeAmenitiesDTOS()) {
                UnitTypeAmenity amenity = UnitTypeAmenity.builder()
                        .unitType(updatedUnitType )
                        .name(tmp.getName())
                        .type(tmp.getType())
                        .isActive(true)
                        .build();
                unitTypeAmentitiesRepository.save(amenity);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving amenities");
        }

        List<UnitTypeAmenity> amenities = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(updatedUnitType.getId());


        UnitTypeResponseDTO unitTypeResponseDTO = UnitTypeResponseDTO.builder()
                .id(updatedUnitType.getId())
                .area(updatedUnitType.getArea())
                .bedsFull(updatedUnitType.getBedsFull())
                .bedsKing(updatedUnitType.getBedsKing())
                .bedrooms(updatedUnitType.getBedrooms())
                .bedsQueen(updatedUnitType.getBedsQueen())
                .bedsSofa(updatedUnitType.getBedsSofa())
                .bedsTwin(updatedUnitType.getBedsTwin())
                .description(updatedUnitType.getDescription())
                .bathrooms(updatedUnitType.getBathrooms())
                .bedsMurphy(updatedUnitType.getBedsMurphy())
                .price(updatedUnitType.getPrice())
                .kitchen(updatedUnitType.getKitchen())
                .photos(updatedUnitType.getPhotos())
                .title(updatedUnitType.getTitle())
                .resortId(updatedUnitType.getResort().getId())
                .buildingsOption(updatedUnitType.getBuildingsOption())
                .sleeps(updatedUnitType.getSleeps())
                .view(updatedUnitType.getView())
                .unitTypeAmenitiesDTOS(amenities.stream()
                        .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .isActive(p.getIsActive())
                                .build())
                        .toList())
                .isActive(updatedUnitType.getIsActive())
                .build();

        return unitTypeResponseDTO;
    }
    @Override
    public UnitTypeResponseDTO deActiveUnitType(Integer unitTypeId)
            throws ErrMessageException, UserDoesNotHavePermission {

        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) throw new UserDoesNotHavePermission();

        UnitType unitType = unitTypeRepository.findById(unitTypeId)
                .orElseThrow(() -> new ErrMessageException("unitType with ID " + unitTypeId + " does not exist"));

        if (!unitType.getResort().getTimeshareCompany().getId().equals(timeshareCompany.getId())) {
            throw new ErrMessageException("unitType with ID " + unitTypeId + " is not owned by your company");
        }
        unitType.setIsActive(false);

        UnitType updatedUnitType = unitTypeRepository.save(unitType);

        List<UnitTypeAmenity> existingAmenities = unitTypeAmentitiesRepository.findAllByUnitTypeId(updatedUnitType.getId());
        for (UnitTypeAmenity amenity : existingAmenities) {
            amenity.setIsActive(false);
            unitTypeAmentitiesRepository.save(amenity);
        }
        UnitTypeResponseDTO unitTypeResponseDTO = UnitTypeResponseDTO.builder()
                .id(updatedUnitType.getId())
                .area(updatedUnitType.getArea())
                .bedsFull(updatedUnitType.getBedsFull())
                .bedsKing(updatedUnitType.getBedsKing())
                .bedrooms(updatedUnitType.getBedrooms())
                .bedsQueen(updatedUnitType.getBedsQueen())
                .bedsSofa(updatedUnitType.getBedsSofa())
                .bedsTwin(updatedUnitType.getBedsTwin())
                .description(updatedUnitType.getDescription())
                .bathrooms(updatedUnitType.getBathrooms())
                .bedsMurphy(updatedUnitType.getBedsMurphy())
                .price(updatedUnitType.getPrice())
                .kitchen(updatedUnitType.getKitchen())
                .photos(updatedUnitType.getPhotos())
                .title(updatedUnitType.getTitle())
                .resortId(updatedUnitType.getResort().getId())
                .buildingsOption(updatedUnitType.getBuildingsOption())
                .sleeps(updatedUnitType.getSleeps())
                .view(updatedUnitType.getView())
                .unitTypeAmenitiesDTOS(existingAmenities.stream()
                        .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .isActive(p.getIsActive())
                                .build())
                        .toList())
                .isActive(updatedUnitType.getIsActive())
                .build();

        return unitTypeResponseDTO;
    }
    @Override
    public UnitTypeResponseDTO getUnitTypeById(Integer unitTypeId)
            throws EntityDoesNotExistException, UserDoesNotHavePermission {
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) {
            throw new UserDoesNotHavePermission();
        }

        Optional<UnitType> unitType = unitTypeRepository.findById(unitTypeId);
        if (!unitType.isPresent()) {
            throw new EntityDoesNotExistException();
        }

        UnitType unitTypeInDb = unitType.get();
        List<UnitTypeAmenity> unitAmenityList = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(unitTypeId);

        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortId(unitTypeInDb.getResort().getId())
                .stream().map(unitTypeMapper::toDto).toList();

        for (UnitTypeDto tmp : unitTypeDtoListResponse) {
            List<UnitTypeAmenity> unitTypeAmenities = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(tmp.getId());
            tmp.setUnitTypeAmenitiesList(unitTypeAmenities.stream().map(p ->
                    UnitTypeDto.UnitTypeAmenities.builder()
                            .name(p.getName())
                            .type(p.getType())
                            .isActive(p.getIsActive())
                            .build()).toList());
        }

        UnitTypeResponseDTO responseDTO = UnitTypeResponseDTO.builder()
                .id(unitTypeInDb.getId())
                .title(unitTypeInDb.getTitle())
                .area(unitTypeInDb.getArea())
                .bedrooms(unitTypeInDb.getBedrooms())
                .bedsKing(unitTypeInDb.getBedsKing())
                .bedsFull(unitTypeInDb.getBedsFull())
                .bedsMurphy(unitTypeInDb.getBedsMurphy())
                .bedsQueen(unitTypeInDb.getBedsQueen())
                .bedsSofa(unitTypeInDb.getBedsSofa())
                .bedsTwin(unitTypeInDb.getBedsTwin())
                .description(unitTypeInDb.getDescription())
                .bathrooms(unitTypeInDb.getBathrooms())
                .price(unitTypeInDb.getPrice())
                .kitchen(unitTypeInDb.getKitchen())
                .photos(unitTypeInDb.getPhotos())
                .view(unitTypeInDb.getView())
                .sleeps(unitTypeInDb.getSleeps())
                .resortId(unitTypeInDb.getResort().getId())
                .unitTypeAmenitiesDTOS(unitAmenityList.stream()
                        .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .isActive(p.getIsActive())
                                .build())
                        .toList())
                .isActive(unitTypeInDb.getIsActive())
                .build();

        return responseDTO;
    }

    @Override
    public UnitTypeResponseDTO getUnitTypeByIdPublic(Integer unitTypeId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        Optional<UnitType> unitType = unitTypeRepository.findById(unitTypeId);
        if (!unitType.isPresent()) {
            throw new EntityDoesNotExistException();
        }

        UnitType unitTypeInDb = unitType.get();
        List<UnitTypeAmenity> unitAmenityList = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(unitTypeId);

        List<UnitTypeDto> unitTypeDtoListResponse = unitTypeRepository.findAllByResortId(unitTypeInDb.getResort().getId())
                .stream().map(unitTypeMapper::toDto).toList();

        for (UnitTypeDto tmp : unitTypeDtoListResponse) {
            List<UnitTypeAmenity> unitTypeAmenities = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(tmp.getId());
            tmp.setUnitTypeAmenitiesList(unitTypeAmenities.stream().map(p ->
                    UnitTypeDto.UnitTypeAmenities.builder()
                            .name(p.getName())
                            .type(p.getType())
                            .isActive(p.getIsActive())
                            .build()).toList());
        }

        UnitTypeResponseDTO responseDTO = UnitTypeResponseDTO.builder()
                .id(unitTypeInDb.getId())
                .title(unitTypeInDb.getTitle())
                .area(unitTypeInDb.getArea())
                .bedrooms(unitTypeInDb.getBedrooms())
                .bedsKing(unitTypeInDb.getBedsKing())
                .bedsFull(unitTypeInDb.getBedsFull())
                .bedsMurphy(unitTypeInDb.getBedsMurphy())
                .bedsQueen(unitTypeInDb.getBedsQueen())
                .bedsSofa(unitTypeInDb.getBedsSofa())
                .bedsTwin(unitTypeInDb.getBedsTwin())
                .description(unitTypeInDb.getDescription())
                .bathrooms(unitTypeInDb.getBathrooms())
                .price(unitTypeInDb.getPrice())
                .kitchen(unitTypeInDb.getKitchen())
                .photos(unitTypeInDb.getPhotos())
                .view(unitTypeInDb.getView())
                .sleeps(unitTypeInDb.getSleeps())
                .resortId(unitTypeInDb.getResort().getId())
                .unitTypeAmenitiesDTOS(unitAmenityList.stream()
                        .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                                .name(p.getName())
                                .type(p.getType())
                                .isActive(p.getIsActive())
                                .build())
                        .toList())
                .isActive(unitTypeInDb.getIsActive())
                .build();

        return responseDTO;
    }

    @Override
    public List<UnitTypeResponseDTO> getUnitTypeByResortId(Integer resortId)
            throws EntityDoesNotExistException, UserDoesNotHavePermission, ErrMessageException {
        User tsCompany = userService.getLoginUser();

        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) {
            throw new UserDoesNotHavePermission();
        }
        Resort resort = resortRepository.findById(resortId)
                .orElseThrow(() -> new ErrMessageException("Resort with ID " + resortId + " does not exist"));
        if (!resort.getTimeshareCompany().getId().equals(timeshareCompany.getId())) {
            throw new UserDoesNotHavePermission();
        }
        List<UnitType> unitTypeList = unitTypeRepository.findAllByResortIdAndIsActiveTrue(resortId);
        if (unitTypeList.isEmpty()) {
            throw new EntityDoesNotExistException();
        }
        List<UnitTypeResponseDTO> responseDTOs = new ArrayList<>();

        for (UnitType unitTypeInDb : unitTypeList) {
            List<UnitTypeAmenity> unitAmenityList = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(unitTypeInDb.getId());
            UnitTypeResponseDTO responseDTO = unitTypesMapper.toDto(unitTypeInDb);
            List<UnitTypeResponseDTO.UnitTypeAmenitiesDTO> amenitiesDTOList = unitAmenityList.stream()
                    .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                            .name(p.getName())
                            .type(p.getType())
                            .isActive(p.getIsActive())
                            .build())
                    .toList();

            responseDTO.setUnitTypeAmenitiesDTOS(amenitiesDTOList);

            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    @Override
    public List<UnitTypeResponseDTO> getUnitTypeByResortIdPublic(Integer resortId) throws ErrMessageException, EntityDoesNotExistException {

        Resort resort = resortRepository.findById(resortId)
                .orElseThrow(() -> new ErrMessageException("Resort with ID " + resortId + " does not exist"));
        List<UnitType> unitTypeList = unitTypeRepository.findAllByResortIdAndIsActiveTrue(resortId);
        if (unitTypeList.isEmpty()) {
            throw new EntityDoesNotExistException();
        }
        List<UnitTypeResponseDTO> responseDTOs = new ArrayList<>();

        for (UnitType unitTypeInDb : unitTypeList) {
            List<UnitTypeAmenity> unitAmenityList = unitTypeAmentitiesRepository.findAllByUnitTypeIdAndIsActiveTrue(unitTypeInDb.getId());
            UnitTypeResponseDTO responseDTO = unitTypesMapper.toDto(unitTypeInDb);
            List<UnitTypeResponseDTO.UnitTypeAmenitiesDTO> amenitiesDTOList = unitAmenityList.stream()
                    .map(p -> UnitTypeResponseDTO.UnitTypeAmenitiesDTO.builder()
                            .name(p.getName())
                            .type(p.getType())
                            .isActive(p.getIsActive())
                            .build())
                    .toList();

            responseDTO.setUnitTypeAmenitiesDTOS(amenitiesDTOList);

            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }
}
