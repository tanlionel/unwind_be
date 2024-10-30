package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.model.SystemDTO.PolicyMapper;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.Period;
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
    @Autowired
    private final RentalPostingApprovalMapper rentalPostingApprovalMapper;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private final RoomInfoRepository roomInfoRepository;
    @Autowired
    private final WalletService walletService;


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
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRentalPackage_Id(
                true, resortName,4 ,pageable);
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
    public RentalPostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, RentalPostingApprovalDto rentalPostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<RentalPosting> rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId);
        if (!rentalPosting.isPresent()) throw new OptionalNotFoundException("Not found rental posting");
        if (!rentalPosting.get().getStatus().equals(String.valueOf(RentalPostingEnum.PendingApproval))) throw new ErrMessageException("Status must be pending approval");
        Optional<UnitType> unitType = null;
        if (rentalPostingApprovalDto.getUnitTypeId()!=0){
            unitType = unitTypeRepository.findById(rentalPostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }
        RentalPosting rentalPostingUpdate = rentalPosting.get();
        if (rentalPostingApprovalDto.getUnitTypeId()!=0) {
            RoomInfo roomInfo = rentalPostingUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            RoomInfo roomInfoAfterSave = roomInfoRepository.save(roomInfo);
        }
        if (rentalPostingUpdate.getRentalPackage().getId() == 3){
            if (rentalPostingApprovalDto.getStaffRefinementPrice()<=0) throw new ErrMessageException("Staff refinement price must be quarter than 0");
            else {
                rentalPostingUpdate.setStaffRefinementPrice(rentalPostingApprovalDto.getStaffRefinementPrice());
            }
            rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.AwaitingConfirmation));
        }
        if (rentalPostingUpdate.getRentalPackage().getId()==4){
            if (rentalPostingApprovalDto.getPriceValuation()<=0) throw new ErrMessageException("Price valuation must be quarter than 0");
            else {
                rentalPostingUpdate.setPriceValuation(rentalPostingApprovalDto.getPriceValuation());
                rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.PendingPricing));
            }
        }
        if (rentalPostingUpdate.getRentalPackage().getId()==2) rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.Processing));
        rentalPostingUpdate.setNote(rentalPostingApprovalDto.getNote());
        rentalPostingUpdate.setIsVerify(true);
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPostingUpdate);
        return rentalPostingApprovalMapper.toDto(rentalPostingInDb);
    }

    @Override
    public RentalPostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException {
        Optional<RentalPosting> rentalPosting = rentalPostingRepository.findById(postingId);
        if (!rentalPosting.isPresent()) throw new OptionalNotFoundException("Not found posting");
        if (!rentalPosting.get().getStatus().equals(String.valueOf(RentalPostingEnum.PendingApproval))) throw new ErrMessageException("Status must be pending approval");
        rentalPosting.get().setNote(note);
        rentalPosting.get().setStatus(String.valueOf(RentalPostingEnum.Reject));
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting.get());
        Optional<Customer> customer = customerRepository.findById(rentalPostingInDb.getOwner().getId());
        if (!customer.isPresent()) throw new ErrMessageException("Error when refund money to customer but reject successfully");
        float fee = 0;
        float money = rentalPostingInDb.getRentalPackage().getPrice()-20000;
        String paymentMethod = "WALLET";
        String description = "Giao dịch hoàn tiền từ chối bài đăng";
        String transactionType = "RENTALPOSTING";
        WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(customer.get().getId(),fee,money,paymentMethod,description,transactionType);
        return rentalPostingApprovalMapper.toDto(rentalPostingInDb);
    }

    @Override
    public RentalPostingApprovalResponseDto approvalPostingSystemStaff(Integer postingId, Float newPriceValuation) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findById(postingId).orElseThrow(()->new OptionalNotFoundException("Not found posting"));
        if (rentalPosting.getRentalPackage().getId()!=4) throw new ErrMessageException("Package must be 4");
        if (!rentalPosting.getStatus().equals(String.valueOf(RentalPostingEnum.PendingPricing))) throw new ErrMessageException("Status must be pending pricing");
        if (newPriceValuation<=0) throw new ErrMessageException("New price valuation must be quarter than 0");
        rentalPosting.setStatus(String.valueOf(RentalPostingEnum.AwaitingConfirmation));
        rentalPosting.setPriceValuation(newPriceValuation);
        RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto = rentalPostingApprovalMapper.toDto(rentalPostingRepository.save(rentalPosting));
        return rentalPostingApprovalResponseDto;
    }

    @Override
    public PostingDetailTsStaffResponseDTO getRentalPostingDetailTsStaffById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        return postingDetailTsStaffMapper.entityToDto(rentalPosting);
    }


}
