package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.Timeshare;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.TimeShareDTO.ListTimeShareDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListRentalPostingMapper {

    ListRentalPostingMapper INSTANCE = Mappers.getMapper(ListRentalPostingMapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "expiredDate", target = "expiredDate")
    @Mapping(source = "rentalPackage.id", target = "rentalPackageId")
    @Mapping(source = "rentalPackage.rentalPackageName", target = "rentalPackageName")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoName", target = "roomName")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "timeshare.roomInfo.resort.address", target = "address")
    @Mapping(source = "isVerify", target = "isVerify")
    @Mapping(source = "nights", target = "nights")
    @Mapping(source = "pricePerNights", target = "pricePerNights")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity.getNights(), entity.getPricePerNights()))")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitTypeDTO")
    PostingResponseDTO entityToDto(RentalPosting entity);

    default List<PostingResponseDTO> entitiesToDtos(List<RentalPosting> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .filter(PostingResponseDTO::getIsValid)
                .collect(Collectors.toList());

    }
    default Page<PostingResponseDTO> entitiesToDTOs(Page<RentalPosting> entities) {
        List<PostingResponseDTO> validDtos = entities.stream()
                .map(this::entityToDto)
                .filter(PostingResponseDTO::getIsValid)
                .collect(Collectors.toList());

        int totalElements = (int) entities.getTotalElements();
        Pageable pageable = entities.getPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), validDtos.size());

        List<PostingResponseDTO> pageContent = validDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, totalElements);
    }
    RentalPosting dtoToEntity(PostingResponseDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingResponseDTO> dtos);
    PostingResponseDTO.unitTypeDTO toUnitTypeDTO(UnitType unitType);
    @AfterMapping
    default void filterActiveEntities(RentalPosting entity, @MappingTarget PostingResponseDTO.PostingResponseDTOBuilder responseDTOBuilder) {
        boolean isValid = true;
        if (entity.getTimeshare() != null && Boolean.FALSE.equals(entity.getTimeshare().getIsActive())) {
            responseDTOBuilder.timeShareId(null);
            isValid = false;
        }

        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getIsActive())) {
                responseDTOBuilder.roomInfoId(null);
                responseDTOBuilder.roomName(null);
                isValid = false;
            }
        }

        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                entity.getTimeshare().getRoomInfo().getUnitType() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getUnitType().getIsActive())) {
                responseDTOBuilder.unitTypeDTO(null);
                isValid = false;
            }
        }

        responseDTOBuilder.isValid(isValid);
    }
    default Float calculateTotalPrice(Integer nights, Float pricePerNights) {
        if (nights != null && pricePerNights != null) {
            return nights * pricePerNights;
        }
        return 0f;
    }

}