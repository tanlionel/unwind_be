package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.model.SystemDTO.PolicyMapper;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalPostingServiceImplement implements RentalPostingService {
    @Autowired
    private final RentalPostingRepository rentalPostingRepository;
    @Autowired
    private final ListRentalPostingMapper listRentalPostingMapper;
    @Autowired
    private final PostingDetailTsStaffMapper postingDetailTsStaffMapper;
    @Autowired
    private final ListRentalPostingTsStaffMapper listRentalPostingTsStaffMapper;
    @Autowired
    private final PostingDetailMapper postingDetailMapper;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TimeShareStaffService timeShareStaffService;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;
    @Autowired
    private final TimeShareRepository timeShareRepository;
    @Autowired
    private final CancellationPolicyRepository cancellationPolicyRepository;
    @Autowired
    private final RentalPackageRepository rentalPackageRepository;
    @Autowired
    private final RentalPostingResponseMapper rentalPostingResponseMapper;
/*    private final String processing = "Processing";
    private final String pendingPricing = "PendingPricing";
    private final String  pendingApproval = "PendingApproval";*/
    @Override
    public List<PostingResponseDTO> getAllPostings() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }
        List<RentalPosting> rentalPostings = rentalPostingRepository.findAllByOwnerIdAndIsActive(customer.getId(),true);
        return listRentalPostingMapper.entitiesToDtos(rentalPostings);
    }
    @Override
    public Page<PostingResponseDTO> getAllPublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException {
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatus(true,
                resortName, String.valueOf(RentalPostingEnum.Processing), pageable);
        return listRentalPostingMapper.entitiesToDTOs(rentalPostings);
    }
    @Override
    public PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        return postingDetailMapper.entityToDto(rentalPosting);
    }
    @Override
    public Page<PostingResponseTsStaffDTO> getAllPostingsTsStaff(String roomInfoCode, Pageable pageable) throws OptionalNotFoundException {
        TimeShareCompanyStaffDTO user = timeShareStaffService.getLoginStaff();
        Optional<TimeshareCompanyStaff> timeshareCompanyStaffOpt = timeshareCompanyStaffRepository.findById(user.getId());
        if (timeshareCompanyStaffOpt.isEmpty()) {
            throw new OptionalNotFoundException("Timeshare company staff not found with id: " + user.getId());
        }
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffOpt.get();
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                true, roomInfoCode,String.valueOf(RentalPostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), pageable);
        return listRentalPostingTsStaffMapper.entitiesToDTOs(rentalPostings);
    }
    @Override
    public Page<PostingResponseTsStaffDTO> getAllPostingsSystemStaff(String resortName, Pageable pageable) throws OptionalNotFoundException {
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatusAndRentalPackage_Id(
                true, resortName,String.valueOf(RentalPostingEnum.PendingApproval),4 ,pageable);
        return listRentalPostingTsStaffMapper.entitiesToDTOs(rentalPostings);
    }

    @Override
    public RentalPostingResponseDto createRentalPosting(RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer()==null) throw new ErrMessageException("Not init customer yet");
        Optional<Timeshare> timeshare = timeShareRepository.findById(rentalPostingRequestDto.getTimeshareId());
        if (!timeshare.isPresent()) throw new OptionalNotFoundException("Not found timeshare");
        Optional<CancellationPolicy> cancellationPolicy = cancellationPolicyRepository.findById(rentalPostingRequestDto.getCancellationTypeId());
        if (!cancellationPolicy.isPresent()) throw new OptionalNotFoundException("Not found cancellation");
        Optional<RentalPackage> rentalPackage = rentalPackageRepository.findById(rentalPostingRequestDto.getRentalPackageId());
        if (!rentalPackage.isPresent()) throw new OptionalNotFoundException("Not found rental package");
        RentalPosting rentalPosting = RentalPosting.builder()
                .description(rentalPostingRequestDto.getDescription())
                .nights(rentalPostingRequestDto.getNights())
                .pricePerNights(rentalPostingRequestDto.getPricePerNights())
                .isVerify(false)
                .isBookable(false)
                .timeshare(timeshare.get())
                .roomInfo(timeshare.get().getRoomInfo())
                .cancellationType(cancellationPolicy.get())
                .checkinDate(rentalPostingRequestDto.getCheckinDate())
                .checkoutDate(rentalPostingRequestDto.getCheckoutDate())
                .expiredDate(LocalDate.now().plusDays(rentalPackage.get().getDuration()))
                .status(String.valueOf(RentalPostingEnum.PendingApproval))
                .owner(user.getCustomer())
                .rentalPackage(rentalPackage.get())
                .isActive(true)
                .build();
        if (rentalPostingRequestDto.getRentalPackageId()==1) rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Processing));
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting);
        return rentalPostingResponseMapper.toDto(rentalPostingInDb);
    }

    @Override
    public PostingDetailTsStaffResponseDTO getRentalPostingDetailTsStaffById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        return postingDetailTsStaffMapper.entityToDto(rentalPosting);
    }


}
