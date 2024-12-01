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
    @Mapping(source = "timeshare.roomInfo.resort.location.name", target = "resortLocationName")
    @Mapping(source = "timeshare.roomInfo.resort.location.displayName", target = "resortLocationDisplayName")
    @Mapping(source = "isVerify", target = "isVerify")
    @Mapping(source = "nights", target = "nights")
    @Mapping(source = "pricePerNights", target = "pricePerNights")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity.getNights(), entity.getPricePerNights()))")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitTypeDTO")
    @Mapping(source = "priceValuation",target = "priceValuation")
    @Mapping(source = "staffRefinementPrice",target = "staffRefinementPrice")
    PostingResponseDTO entityToDto(RentalPosting entity);

    default List<PostingResponseDTO> entitiesToDtos(List<RentalPosting> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

    }

    RentalPosting dtoToEntity(PostingResponseDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingResponseDTO> dtos);
    PostingResponseDTO.unitTypeDTO toUnitTypeDTO(UnitType unitType);

    default Float calculateTotalPrice(Integer nights, Float pricePerNights) {
        if (nights != null && pricePerNights != null) {
            return nights * pricePerNights;
        }
        return 0f;
    }

}