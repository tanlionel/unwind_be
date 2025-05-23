package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.exception.ErrMessageException;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostingDetailMapper {

    PostingDetailMapper INSTANCE = Mappers.getMapper(PostingDetailMapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "expiredDate", target = "expiredDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "updatedDate", target = "updatedDate")
    @Mapping(source = "cancellationType.id", target = "cancelTypeId")
    @Mapping(source = "cancellationType.name", target = "cancelType")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "isVerify", target = "isVerify")
    @Mapping(source = "nights", target = "nights")
    @Mapping(source = "pricePerNights", target = "pricePerNights")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity.getNights(), entity.getPricePerNights()))")
    @Mapping(source = "rentalPackage.id", target = "rentalPackageId")
    @Mapping(source = "rentalPackage.rentalPackageName", target = "rentalPackageName")
    @Mapping(source = "rentalPackage.duration", target = "rentalPackageDuration")
    @Mapping(source = "rentalPackage.description", target = "rentalPackageDescription")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitType")
    @Mapping(source = "timeshare.roomInfo.resort.amenities", target = "resortAmenities")
    @Mapping(source = "timeshare.roomInfo.unitType.amenities", target = "unitTypeAmenities")
    @Mapping(source = "timeshare.roomInfo.amenities", target = "roomAmenities")
    @Mapping(source = "priceValuation",target = "priceValuation")
    @Mapping(source = "staffRefinementPrice",target = "staffRefinementPrice")

    @Mapping(source = "timeshare.roomInfo.resort.location.name", target = "location.name")
    @Mapping(source = "timeshare.roomInfo.resort.location.displayName", target = "location.displayName")
    @Mapping(source = "timeshare.roomInfo.resort.location.latitude", target = "location.latitude")
    @Mapping(source = "timeshare.roomInfo.resort.location.longitude", target = "location.longitude")
    @Mapping(source = "timeshare.roomInfo.resort.location.country", target = "location.country")
    @Mapping(source = "timeshare.roomInfo.resort.location.placeId", target = "location.placeId")
    PostingDetailResponseDTO entityToDto(RentalPosting entity);
    PostingDetailResponseDTO.ResortAmenityDTO toResortAmenityDTO(ResortAmenity amenity);
    PostingDetailResponseDTO.unitType toUnitTypeDTO(UnitType unitType);

    List<PostingDetailResponseDTO> entitiesToDtos(List<RentalPosting> entities);

    RentalPosting dtoToEntity(PostingDetailResponseDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingDetailResponseDTO> dtos);

    default Float calculateTotalPrice(Integer nights, Float pricePerNights) {
        if (nights != null && pricePerNights != null) {
            return nights * pricePerNights;
        }
        return 0f;
    }


}
