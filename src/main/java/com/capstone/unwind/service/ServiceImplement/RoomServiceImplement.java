package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.RoomDTO.RoomRequestDTO;
import com.capstone.unwind.model.RoomDTO.RoomResponseDTO;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.RoomService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomServiceImplement implements RoomService {
    @Autowired
    private final RoomInfoRepository roomInfoRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;
    @Autowired
    private final ResortRepository resortRepository;
    @Autowired
    private final RoomAmentityRepository roomAmentityRepository;
    @Autowired
    private final CustomerRepository customerRepository;
@Override
    public RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) throws EntityDoesNotExistException,  UserDoesNotHavePermission,ErrMessageException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
    Resort resort = resortRepository.findById(roomRequestDTO.getResortId())
            .orElseThrow(() -> new ErrMessageException("Resort with ID " + roomRequestDTO.getResortId() + " does not exist"));
    UnitType unitTypeInDb = unitTypeRepository.findById(roomRequestDTO.getUnitTypeId())
            .orElseThrow(() -> new ErrMessageException("unitType with ID " + roomRequestDTO.getUnitTypeId() + " does not exist"));
    if (!unitTypeInDb.getResort().getId().equals(roomRequestDTO.getResortId())) {
        throw new  ErrMessageException("unitType with ID " + roomRequestDTO.getUnitTypeId() + "is not owned by this Resort");
    }
    boolean roomCodeExists = roomInfoRepository.existsByRoomInfoCodeAndResortId(roomRequestDTO.getRoomInfoCode(), resort.getId());
    if (roomCodeExists) {
        throw new ErrMessageException("Room code '" + roomRequestDTO.getRoomInfoCode() + "' already exists in this resort.");
    }
    RoomInfo roomInfo = RoomInfo.builder()
            .roomInfoCode(roomRequestDTO.getRoomInfoCode())
            .resortId(resort.getId())
            .roomInfoName(roomRequestDTO.getRoomName())
            .status(roomRequestDTO.getStatus())
            .unitTypeId(unitTypeInDb.getId())
            .isActive(true)
            .build();

    RoomInfo roomInfoInDb = roomInfoRepository.save(roomInfo);
    if (roomRequestDTO.getRoomAmenities() != null && !roomRequestDTO.getRoomAmenities().isEmpty()) {
        try {
            for (RoomRequestDTO.roomAmenity tmp : roomRequestDTO.getRoomAmenities()) {
                RoomAmenity roomAmenity = RoomAmenity.builder()
                        .roomInfo(roomInfo)
                        .name(tmp.getName())
                        .type(tmp.getType())
                        .isActive(true)
                        .build();
                roomAmentityRepository.save(roomAmenity);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving room amenities");
        }
    }

    List<RoomAmenity> roomAmenities = roomAmentityRepository.findAllByRoomInfoId(roomInfo.getId());
        RoomResponseDTO.unitType unitTypeDTO = RoomResponseDTO.unitType.builder()
                .id(unitTypeInDb.getId())
                .title(unitTypeInDb.getTitle())
                .area(unitTypeInDb.getArea())
                .bathrooms(unitTypeInDb.getBathrooms())
                .bedrooms(unitTypeInDb.getBedrooms())
                .bedsFull(unitTypeInDb.getBedsFull())
                .bedsKing(unitTypeInDb.getBedsKing())
                .bedsSofa(unitTypeInDb.getBedsSofa())
                .buildingsOption(unitTypeInDb.getBuildingsOption())
                .bedsMurphy(unitTypeInDb.getBedsMurphy())
                .bedsQueen(unitTypeInDb.getBedsQueen())
                .bedsTwin(unitTypeInDb.getBedsTwin())
                .price(unitTypeInDb.getPrice())
                .description(unitTypeInDb.getDescription())
                .kitchen(unitTypeInDb.getKitchen())
                .photos(unitTypeInDb.getPhotos())
                .sleeps(unitTypeInDb.getSleeps())
                .view(unitTypeInDb.getView())
                .isActive(unitTypeInDb.getIsActive())
                .build();

        RoomResponseDTO responseDTO = RoomResponseDTO.builder()
                .roomId(roomInfoInDb.getId())
                .roomInfoCode(roomInfoInDb.getRoomInfoCode())
                .resortId(roomInfoInDb.getResortId())
                .isActive(roomInfoInDb.getIsActive())
                .status(roomInfoInDb.getStatus())
                .roomName(roomInfoInDb.getRoomInfoName())
                .roomAmenities(roomAmenities.stream()
                        .map(r -> RoomResponseDTO.roomAmenity.builder()
                                .name(r.getName())
                                .type(r.getType())
                                .isActive(r.getIsActive())
                                .build())
                        .toList())
                .unitType(unitTypeDTO)
                .createdAt(roomInfoInDb.getCreatedAt())
                .build();
        return responseDTO;
    }
}
