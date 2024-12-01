package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.entity.UnitType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.security.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostingDetailTsStaffMapper {

    PostingDetailTsStaffMapper INSTANCE = Mappers.getMapper(PostingDetailTsStaffMapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "priceValuation", target = "priceValuation")
    @Mapping(source = "cancellationType.id", target = "cancelTypeId")
    @Mapping(source = "cancellationType.name", target = "cancelType")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoName", target = "roomName")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "timeshare.roomInfo.resort.description", target = "resortDescription")
    @Mapping(source = "timeshare.roomInfo.resort.location.name", target = "resortLocationName")
    @Mapping(source = "timeshare.roomInfo.resort.location.name", target = "resortLocationDisplayName")
    @Mapping(source = "isVerify", target = "isVerify")
    @Mapping(source = "nights", target = "nights")
    @Mapping(source = "pricePerNights", target = "pricePerNights")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(entity.getNights(), entity.getPricePerNights()))")
    @Mapping(source = "rentalPackage.id", target = "rentalPackageId")
    @Mapping(source = "rentalPackage.rentalPackageName", target = "rentalPackageName")
    @Mapping(source = "rentalPackage.description", target = "rentalPackageDescription")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitType")
    @Mapping(source = "timeshare.roomInfo.resort.amenities", target = "resortAmenities")
    @Mapping(source = "timeshare.roomInfo.unitType.amenities", target = "unitTypeAmenities")
    @Mapping(source = "timeshare.roomInfo.amenities", target = "roomAmenities")
    PostingDetailTsStaffResponseDTO entityToDto(RentalPosting entity);
    PostingDetailTsStaffResponseDTO.ResortAmenityDTO toResortAmenityDTO(ResortAmenity amenity);
    PostingDetailTsStaffResponseDTO.unitType toUnitTypeDTO(UnitType unitType);
    default Timestamp map(Timestamp value) {
        return value;
    }

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
