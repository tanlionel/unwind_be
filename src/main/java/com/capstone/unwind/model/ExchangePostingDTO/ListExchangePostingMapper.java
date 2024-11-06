package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListExchangePostingMapper {

    ListExchangePostingMapper INSTANCE = Mappers.getMapper(ListExchangePostingMapper.class);

    @Mapping(source = "id", target = "exchangePostingId")
    @Mapping(source = "expired", target = "expired")
    @Mapping(source = "exchangePackage.id", target = "exchangePackageId")
    @Mapping(source = "exchangePackage.packageName", target = "exchangePackageName")
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
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "timeshare.roomInfo.unitType", target = "unitTypeDTO")
    PostingExchangeResponseDTO entityToDto(ExchangePosting entity);



    ExchangePosting dtoToEntity(PostingExchangeResponseDTO dto);
    List<ExchangePosting> dtosToEntities(List<PostingExchangeResponseDTO> dtos);
    PostingExchangeResponseDTO.unitTypeDTO toUnitTypeDTO(UnitType unitType);


}