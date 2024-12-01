package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostingExchangeDetailMapper {

    PostingExchangeDetailMapper INSTANCE = Mappers.getMapper(PostingExchangeDetailMapper.class);

    @Mapping(source = "id", target = "exchangePostingId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "expired", target = "expiredDate")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoName", target = "roomName")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "nights", target = "nights")
    @Mapping(source = "exchangePackage.id", target = "exchangePackageId")
    @Mapping(source = "exchangePackage.packageName", target = "exchangePackageName")
    @Mapping(source = "exchangePackage.duration", target = "exchangePackageDuration")
    @Mapping(source = "exchangePackage.description", target = "exchangePackageDescription")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitType")
    @Mapping(source = "timeshare.roomInfo.resort.amenities", target = "resortAmenities")
    @Mapping(source = "timeshare.roomInfo.unitType.amenities", target = "unitTypeAmenities")
    @Mapping(source = "timeshare.roomInfo.amenities", target = "roomAmenities")

    @Mapping(source = "timeshare.roomInfo.resort.location", target = "location")
    PostingExchangeDetailResponseDTO entityToDto(ExchangePosting entity);
    PostingExchangeDetailResponseDTO.ResortAmenityDTO toResortAmenityDTO(ResortAmenity amenity);
    PostingExchangeDetailResponseDTO.unitType toUnitTypeDTO(UnitType unitType);

    List<PostingExchangeDetailResponseDTO> entitiesToDtos(List<ExchangePosting> entities);

    ExchangePosting dtoToEntity(PostingExchangeDetailResponseDTO dto);
    List<ExchangePosting> dtosToEntities(List<PostingDetailResponseDTO> dtos);

}
