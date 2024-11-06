package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.model.PostingDTO.PostingResponseTsStaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListExchangePostingTsStaffMapper {

    ListExchangePostingTsStaffMapper INSTANCE = Mappers.getMapper(ListExchangePostingTsStaffMapper.class);

    @Mapping(source = "id", target = "exchangePostingId")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "exchangePackage.id", target = "exchangePackageId")
    @Mapping(source = "exchangePackage.packageName", target = "exchangePackageName")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "checkinDate", target = "checkinDate")
    @Mapping(source = "checkoutDate", target = "checkoutDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isActive", target = "isActive")
    ExchangePostingResponseTsStaffDTO entityToDto(ExchangePosting entity);



    RentalPosting dtoToEntity(PostingResponseTsStaffDTO dto);
    List<RentalPosting> dtosToEntities(List<PostingResponseTsStaffDTO> dtos);


}