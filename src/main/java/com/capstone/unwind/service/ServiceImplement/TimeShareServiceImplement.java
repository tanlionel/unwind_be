package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.DocumentStoreEnum;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.RoomDTO.RoomInfoMapper;
import com.capstone.unwind.model.TimeShareDTO.*;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.TimeShareService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TimeShareServiceImplement implements TimeShareService {
    @Autowired
    private final RoomInfoRepository roomInfoRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ResortRepository resortRepository;
    @Autowired
    private final TimeShareRepository timeShareRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final TimeShareMapper timeShareMapper;
    @Autowired
    private final ListTimeShareMapper listTimeShareMapper;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private final RentalPostingRepository rentalPostingRepository;
    @Autowired
    private final ExchangePostingRepository exchangePostingRepository;
    @Autowired
    private final DocumentStoreRepository documentStoreRepository;

    @Override
    public TimeShareResponseDTO createTimeShare(TimeShareRequestDTO timeShareRequestDTO) throws EntityDoesNotExistException, ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer==null) throw new OptionalNotFoundException("Customer not found");
        RoomInfo roomInfo = roomInfoRepository.findById(timeShareRequestDTO.getRoomInfoId())
                .orElseThrow(() -> new ErrMessageException("roomInfo with ID " + timeShareRequestDTO.getRoomInfoId() + " does not exist"));
        boolean isTimeshareConflict = timeShareRepository.existsByRoomInfoAndDateRange(
                roomInfo,
                timeShareRequestDTO.getStartDate(),
                timeShareRequestDTO.getEndDate()
        );
        if (isTimeshareConflict) {
            throw new ErrMessageException("This room already has a timeshare for the specified date range.");
        }
        if (timeShareRequestDTO.getStartYear() >= timeShareRequestDTO.getEndYear()) {
            throw new ErrMessageException("Start year must be less than end year.");
        }
        if (timeShareRequestDTO.getStartDate().isEqual(timeShareRequestDTO.getEndDate()) ||
                timeShareRequestDTO.getStartDate().isAfter(timeShareRequestDTO.getEndDate())) {
            throw new ErrMessageException("Start date must be less than end date and cannot be equal.");
        }
        if (!timeShareRequestDTO.getStartDate().getMonth().equals(timeShareRequestDTO.getEndDate().getMonth())
                || timeShareRequestDTO.getStartDate().getYear() != timeShareRequestDTO.getEndDate().getYear()) {
            throw new ErrMessageException("Start date and end date must be in the same month.");
        }
        if (timeShareRequestDTO.getStartDate().getYear() < timeShareRequestDTO.getStartYear() ||
                timeShareRequestDTO.getEndDate().getYear() > timeShareRequestDTO.getEndYear()) {
            throw new ErrMessageException("The year of start date and end date must not exceed the defined start year and end year.");
        }
        Timeshare timeshare = Timeshare.builder()
                .owner(customer)
                .startDate(timeShareRequestDTO.getStartDate())
                .endDate(timeShareRequestDTO.getEndDate())
                .startYear(timeShareRequestDTO.getStartYear())
                .endYear(timeShareRequestDTO.getEndYear())
                .status(timeShareRequestDTO.getStatus())
                .roomInfo(roomInfo)
                .isActive(true)
                .build();

        Timeshare timeShareInDb = timeShareRepository.save(timeshare);
        try {
            for (String imageUrl : timeShareRequestDTO.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.RentalPosting.toString());
                document.setEntityId(timeShareInDb.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        Optional<RoomInfo> roomInfoInDb = roomInfoRepository.findById(timeShareInDb.getRoomInfo().getId());
        Optional<Resort> resortInDb = resortRepository.findById(roomInfoInDb.get().getResort().getId());
        TimeShareResponseDTO timeShareResponseDTO = TimeShareResponseDTO.builder()
                .timeShareId(timeShareInDb.getId())
                .status(timeShareInDb.getStatus())
                .startDate(timeShareInDb.getStartDate())
                .endDate(timeShareInDb.getEndDate())
                .startYear(timeShareInDb.getStartYear())
                .endYear(timeShareInDb.getEndYear())
                .roomInfo(roomInfoMapper.toDto(roomInfoInDb.get()))
                .owner(timeShareInDb.getOwner().getFullName())
                .createdAt(timeShareInDb.getCreatedAt())
                .isActive(timeShareInDb.getIsActive())
                .build();
        return timeShareResponseDTO;
    }

    @Override
    public Page<ListTimeShareDTO> getAllTimeShares(Pageable pageable) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }
        Page<Timeshare> timeSharesPage = timeShareRepository.findAllByOwnerIdAndIsActive(customer.getId(), pageable,true);
        return timeSharesPage.map(listTimeShareMapper::toDto);
    }
@Override
    public TimeShareDetailDTO getTimeShareDetails(Integer timeShareID) throws OptionalNotFoundException {
        Optional<Timeshare> optionalTimeShare = timeShareRepository.findById(timeShareID);
        if (!optionalTimeShare.isPresent()) throw new OptionalNotFoundException("Not found room");
        Timeshare timeShare = optionalTimeShare.get();
        if (!timeShare.getIsActive()) throw new OptionalNotFoundException("timeshare is not active");

        Optional<RoomInfo> roomInfo = roomInfoRepository.findById(timeShare.getRoomInfo().getId());
        if (!roomInfo.get().getIsActive()) throw new OptionalNotFoundException("room is not active");
    List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(timeShare.getId(), DocumentStoreEnum.Timeshare.toString());
    Optional<UnitType> optionalUnitType = unitTypeRepository.findByIdAndIsActiveTrue(roomInfo.get().getUnitType().getId());
    UnitType unitType = optionalUnitType.orElseThrow(() -> new OptionalNotFoundException("Unit type not found or inactive"));
        Optional<Resort> resort = resortRepository.findById(roomInfo.get().getResort().getId());
        if (!resort.get().getIsActive()) throw new OptionalNotFoundException("resort is not active");
        TimeShareDetailDTO timeShareDetailDTO = TimeShareDetailDTO.builder()
                .timeShareId(timeShare.getId())
                .resortName(resort.get().getResortName())
                .roomName(roomInfo.get().getRoomInfoName())
                .roomCode(roomInfo.get().getRoomInfoCode())
                .resortAddress(resort.get().getAddress())
                .startDate(timeShare.getStartDate())
                .endDate(timeShare.getEndDate())
                .resortId(resort.get().getId())
                .roomId(roomInfo.get().getId())
                .imageUrls(imageUrls)
                .unitType(TimeShareDetailDTO.unitType.builder()
                        .id(unitType.getId())
                        .title(unitType.getTitle())
                        .area(unitType.getArea())
                        .bathrooms(unitType.getBathrooms())
                        .bedrooms(unitType.getBedrooms())
                        .bedsFull(unitType.getBedsFull())
                        .bedsKing(unitType.getBedsKing())
                        .bedsQueen(unitType.getBedsQueen())
                        .bedsSofa(unitType.getBedsSofa())
                        .bedsMurphy(unitType.getBedsMurphy())
                        .bedsTwin(unitType.getBedsTwin())
                        .buildingsOption(unitType.getBuildingsOption())
                        .description(unitType.getDescription())
                        .kitchen(unitType.getKitchen())
                        .photos(unitType.getPhotos())
                        .sleeps(unitType.getSleeps())
                        .view(unitType.getView())
                        .build())
                .build();
        return timeShareDetailDTO;
    }

    @Override
    public List<Integer> getTimeshareValidYears(Integer timeshareId) throws OptionalNotFoundException {
        Optional<Timeshare> timeshare = timeShareRepository.findById(timeshareId);
        if (!timeshare.isPresent()) throw new OptionalNotFoundException("not found timeshare");
        int startYears = timeshare.get().getStartYear();
        int endYears = timeshare.get().getEndYear();
        int currentYear = Year.now().getValue();
        List<Integer> notExchangeValidYears = exchangePostingRepository.findAllNotValidYears(timeshareId);
        List<Integer> notValidYears = rentalPostingRepository.findAllNotValidYears(timeshareId);
        List<Integer> validYears = IntStream.rangeClosed(startYears, endYears)
                .boxed()
                .filter(year ->year>=currentYear && !notValidYears.contains(year) && !notExchangeValidYears.contains(year))
                .collect(Collectors.toList());

        return validYears;
    }
}