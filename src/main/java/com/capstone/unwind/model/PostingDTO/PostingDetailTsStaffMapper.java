package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.entity.UnitType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.security.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostingDetailTsStaffMapper {

    PostingDetailTsStaffMapper INSTANCE = Mappers.getMapper(PostingDetailTsStaffMapper.class);

    @Mapping(source = "id", target = "rentalPostingId")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "timeshare.id", target = "timeShareId")
    @Mapping(source = "timeshare.roomInfo.id", target = "roomInfoId")
    @Mapping(source = "timeshare.roomInfo.roomInfoName", target = "roomName")
    @Mapping(source = "timeshare.roomInfo.roomInfoCode", target = "roomCode")
    @Mapping(source = "timeshare.roomInfo.resort.id", target = "resortId")
    @Mapping(source = "timeshare.roomInfo.resort.resortName", target = "resortName")
    @Mapping(source = "timeshare.roomInfo.resort.address", target = "address")
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
    @AfterMapping
    default void filterActiveEntities(RentalPosting entity, @MappingTarget PostingDetailTsStaffResponseDTO.PostingDetailTsStaffResponseDTOBuilder responseDTOBuilder) {
        boolean isValid = true;

        if (entity.getTimeshare() != null && Boolean.FALSE.equals(entity.getTimeshare().getIsActive())) {
            responseDTOBuilder.timeShareId(null);
            isValid = false;
        }

        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getIsActive())) {
                responseDTOBuilder.roomInfoId(null);
                responseDTOBuilder.roomName(null);
                responseDTOBuilder.roomCode(null);
                isValid = false;
            }
        }
        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                entity.getTimeshare().getRoomInfo().getUnitType() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getUnitType().getIsActive())) {
                responseDTOBuilder.unitType(null);
                isValid = false;
            }
        }
        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                entity.getTimeshare().getRoomInfo().getResort() != null) {
            if (Boolean.FALSE.equals(entity.getTimeshare().getRoomInfo().getResort().getIsActive())) {
                responseDTOBuilder.resortId(null);
                responseDTOBuilder.resortName(null);
                responseDTOBuilder.address(null);
                isValid = false;
            }
        }

        if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                entity.getTimeshare().getRoomInfo().getResort() != null) {
            List<PostingDetailTsStaffResponseDTO.ResortAmenityDTO> activeAmenities = entity.getTimeshare().getRoomInfo().getResort().getAmenities().stream()
                    .filter(amenity -> Boolean.TRUE.equals(amenity.getIsActive()))
                    .map(amenity -> PostingDetailTsStaffResponseDTO.ResortAmenityDTO.builder()
                            .id(amenity.getId())
                            .name(amenity.getName())
                            .type(amenity.getType())
                            .build())
                    .collect(Collectors.toList());
            responseDTOBuilder.resortAmenities(activeAmenities);
            if (activeAmenities.isEmpty()) {
                isValid = false;
            }
        }

            if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                    entity.getTimeshare().getRoomInfo().getResort() != null) {
                List<PostingDetailTsStaffResponseDTO.RoomAmenityDTO> activeRoomAmenities = entity.getTimeshare().getRoomInfo().getAmenities().stream()
                        .filter(amenity -> Boolean.TRUE.equals(amenity.getIsActive()))
                        .map(amenity -> PostingDetailTsStaffResponseDTO.RoomAmenityDTO.builder()
                                .id(amenity.getId())
                                .name(amenity.getName())
                                .type(amenity.getType())
                                .build())
                        .collect(Collectors.toList());
                responseDTOBuilder.roomAmenities(activeRoomAmenities);
                if (activeRoomAmenities.isEmpty()) {
                    isValid = false;
                }
            }
                if (entity.getTimeshare() != null && entity.getTimeshare().getRoomInfo() != null &&
                        entity.getTimeshare().getRoomInfo().getResort() != null) {
                    List<PostingDetailTsStaffResponseDTO.UnitTypeAmenityDTO> activeUnitTypeAmenities = entity.getTimeshare().getRoomInfo().getUnitType().getAmenities().stream()
                            .filter(amenity -> Boolean.TRUE.equals(amenity.getIsActive()))
                            .map(amenity -> PostingDetailTsStaffResponseDTO.UnitTypeAmenityDTO.builder()
                                    .id(amenity.getId())
                                    .name(amenity.getName())
                                    .type(amenity.getType())
                                    .build())
                            .collect(Collectors.toList());
                    responseDTOBuilder.unitTypeAmenities(activeUnitTypeAmenities);
            if (activeUnitTypeAmenities.isEmpty()) {
                isValid = false;
            }
        }
        if (!isValid) {
            throw new IllegalStateException("TimeShare or room , unitType related entities are inactive.");
        }
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
