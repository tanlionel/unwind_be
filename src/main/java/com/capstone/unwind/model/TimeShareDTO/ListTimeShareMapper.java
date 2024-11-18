package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Timeshare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ListTimeShareMapper {

    @Mapping(source = "id", target = "timeShareId")
    @Mapping(source = "roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "roomInfo.unitType.bathrooms", target = "bathRoom")
    @Mapping(source = "roomInfo.unitType.bedrooms", target = "bedRooms")
    @Mapping(source = "startDate", target = "startDate", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "endDate", target = "endDate", dateFormat = "dd-MM-yyyy")
    ListTimeShareDTO toDto(Timeshare timeshare);

    List<ListTimeShareDTO> toDtoList(List<Timeshare> timeshares);
}