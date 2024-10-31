package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.UnitType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListRentalPostingTsStaffMapper {

    ListRentalPostingTsStaffMapper INSTANCE = Mappers.getMapper(ListRentalPostingTsStaffMapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "rentalPackage.id", target = "rentalPackageId")
    @Mapping(source = "rentalPackage.rentalPackageName", target = "rentalPackageName")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "pricePerNights", target = "pricePerNights")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity.getNights(), entity.getPricePerNights()))")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    PostingResponseTsStaffDTO entityToDto(RentalPosting entity);

    default List<PostingResponseTsStaffDTO> entitiesToDtos(List<RentalPosting> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .filter(PostingResponseTsStaffDTO::getIsValid)
                .collect(Collectors.toList());

    }
    default Page<PostingResponseTsStaffDTO> entitiesToDTOs(Page<RentalPosting> entities) {
        List<PostingResponseTsStaffDTO> validDtos = entities.stream()
                .map(this::entityToDto)
                .filter(PostingResponseTsStaffDTO::getIsValid)
                .collect(Collectors.toList());

        int totalElements = (int) entities.getTotalElements();
        Pageable pageable = entities.getPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), validDtos.size());

        List<PostingResponseTsStaffDTO> pageContent = validDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, totalElements);
    }
    RentalPosting dtoToEntity(PostingResponseTsStaffDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingResponseTsStaffDTO> dtos);
    @AfterMapping
    default void filterActiveEntities(RentalPosting entity, @MappingTarget PostingResponseTsStaffDTO.PostingResponseTsStaffDTOBuilder responseDTOBuilder) {
        boolean isValid = true;
        if (entity.getTimeshare() != null && Boolean.FALSE.equals(entity.getTimeshare().getIsActive())) {
            responseDTOBuilder.timeShareId(null);
            isValid = false;
        }

        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getIsActive())) {
                responseDTOBuilder.roomInfoId(null);
                responseDTOBuilder.roomCode(null);
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