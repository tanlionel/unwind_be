package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListRentalPostingPackage4Mapper {

    ListRentalPostingPackage4Mapper INSTANCE = Mappers.getMapper(ListRentalPostingPackage4Mapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "owner.phone", target = "ownerPhone")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    PostingPackage4ResponseDTO entityToDto(RentalPosting entity);

    default List<PostingPackage4ResponseDTO> entitiesToDtos(List<RentalPosting> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .filter(PostingPackage4ResponseDTO::getIsValid)
                .collect(Collectors.toList());

    }
    default Page<PostingPackage4ResponseDTO> entitiesToDTOs(Page<RentalPosting> entities) {
        List<PostingPackage4ResponseDTO> validDtos = entities.stream()
                .map(this::entityToDto)
                .filter(PostingPackage4ResponseDTO::getIsValid)
                .collect(Collectors.toList());

        int totalElements = validDtos.size();
        int start = Math.toIntExact(entities.getPageable().getOffset());
        int end = Math.min((start + entities.getPageable().getPageSize()), totalElements);

        List<PostingPackage4ResponseDTO> pageContent = validDtos.subList(start, end);

        return new PageImpl<>(pageContent, entities.getPageable(), totalElements);
    }
    RentalPosting dtoToEntity(PostingPackage4ResponseDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingPackage4ResponseDTO> dtos);
    @AfterMapping
    default void filterActiveEntities(RentalPosting entity, @MappingTarget PostingPackage4ResponseDTO.PostingPackage4ResponseDTOBuilder responseDTOBuilder) {
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
   /* default Float calculateTotalPrice(Integer nights, Float pricePerNights) {
        if (nights != null && pricePerNights != null) {
            return nights * pricePerNights;
        }
        return 0f;
    }*/

}