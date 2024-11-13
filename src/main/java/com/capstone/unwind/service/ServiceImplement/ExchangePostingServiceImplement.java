package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.DocumentStoreEnum;
import com.capstone.unwind.enums.ExchangePostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.ExchangePostingDTO.*;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.ExchangePostingService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangePostingServiceImplement implements ExchangePostingService {


    @Autowired
    private final UserService userService;
    @Autowired
    private final TimeShareRepository timeShareRepository;
    @Autowired
    private final UnitTypeRepository unitTypeRepository;
    @Autowired
    private final ExchangePackageRepository exchangePackageRepository;
    @Autowired
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;
    @Autowired
    private final ExchangePostingRepository exchangePostingRepository;
    @Autowired
    private final TimeShareStaffService timeShareStaffService;
    @Autowired
    private final ExchangePostingResponseMapper exchangePostingResponseMapper;
    @Autowired
    private final ListExchangePostingTsStaffMapper listExchangePostingTsStaffMapper;
    @Autowired
    private final PostingExchangeDetailMapper postingExchangeDetailMapper;
    @Autowired
    private final RoomInfoRepository roomInfoRepository;
    @Autowired
    private final ExchangePostingApprovalMapper exchangePostingApprovalMapper;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final WalletService walletService;
    @Autowired
    private final ListExchangePostingMapper listExchangePostingMapper;
    @Autowired
    private final DocumentStoreRepository documentStoreRepository;
    @Override
    public ExchangePostingResponseDto createExchangePosting(ExchangePostingRequestDto exchangePostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer()==null) throw new ErrMessageException("Not init customer yet");
        Optional<Timeshare> timeshare = timeShareRepository.findById(exchangePostingRequestDto.getTimeshareId());
        if (!timeshare.isPresent()) throw new OptionalNotFoundException("Not found timeshare");
/*        Optional<CancellationPolicy> cancellationPolicy = cancellationPolicyRepository.findById(exchangePostingRequestDto.getCancellationTypeId());
        if (!cancellationPolicy.isPresent()) throw new OptionalNotFoundException("Not found cancellation");*/
        Optional<ExchangePackage> exchangePackage = exchangePackageRepository.findById(exchangePostingRequestDto.getExchangePackageId());
        if (!exchangePackage.isPresent()) throw new OptionalNotFoundException("Not found exchange package");
        ExchangePosting exchangelPosting = ExchangePosting.builder()
                .description(exchangePostingRequestDto.getDescription())
                .nights(exchangePostingRequestDto.getNights())
                .isVerify(false)
                .isExchange(false)
                .timeshare(timeshare.get())
                .roomInfo(timeshare.get().getRoomInfo())
                .checkinDate(exchangePostingRequestDto.getCheckinDate())
                .checkoutDate(exchangePostingRequestDto.getCheckoutDate())
                .expired(LocalDate.now().plusDays(exchangePackage.get().getDuration()))
                .status(String.valueOf(ExchangePostingEnum.PendingApproval))
                .owner(user.getCustomer())
                .exchangePackage(exchangePackage.get())
                .isActive(true)
                .build();
        if (exchangePostingRequestDto.getExchangePackageId()==1) exchangelPosting.setStatus(String.valueOf(ExchangePostingEnum.Processing));
        ExchangePosting exchangePostingInDb = exchangePostingRepository.save(exchangelPosting);
        try {
            for (String imageUrl : exchangePostingRequestDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.ExchangePosting.toString());
                document.setEntityId(exchangePostingInDb.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        return exchangePostingResponseMapper.toDto(exchangePostingInDb);
    }
    @Override
    public Page<ExchangePostingResponseTsStaffDTO> getAllExchangePostingsTsStaff(String roomInfoCode, Pageable pageable) throws OptionalNotFoundException {
        TimeShareCompanyStaffDTO user = timeShareStaffService.getLoginStaff();
        Optional<TimeshareCompanyStaff> timeshareCompanyStaffOpt = timeshareCompanyStaffRepository.findById(user.getId());
        if (timeshareCompanyStaffOpt.isEmpty()) {
            throw new OptionalNotFoundException("Timeshare company staff not found with id: " + user.getId());
        }
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffOpt.get();
        Page<ExchangePosting> exchangePostings  = exchangePostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                    true, roomInfoCode,String.valueOf(ExchangePostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), pageable);

        Page<ExchangePostingResponseTsStaffDTO> postingDtoPage = exchangePostings.map(listExchangePostingTsStaffMapper::entityToDto);
        return postingDtoPage;
    }
    @Override
    public PostingExchangeDetailResponseDTO getExchangePostingDetailById(Integer postingId) throws OptionalNotFoundException {
        ExchangePosting exchangePosting = exchangePostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Exchange Posting not found with ID: " + postingId));
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(exchangePosting.getId(), DocumentStoreEnum.ExchangePosting.toString());
        PostingExchangeDetailResponseDTO responseDTO = postingExchangeDetailMapper.entityToDto(exchangePosting);
        responseDTO.setImageUrls(imageUrls);
        return responseDTO;
    }
    @Override
    public ExchangePostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, ExchangePostingApprovalDto exchangePostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangePosting> exchangePosting = exchangePostingRepository.findByIdAndIsActive(postingId);
        if (!exchangePosting.isPresent()) throw new OptionalNotFoundException("Not found exchange posting");
        if (!exchangePosting.get().getStatus().equals(String.valueOf(ExchangePostingEnum.PendingApproval))) throw new ErrMessageException("Status must be pending approval");
        Optional<UnitType> unitType = null;
        if (exchangePostingApprovalDto.getUnitTypeId()!=0){
            unitType = unitTypeRepository.findById(exchangePostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }
        ExchangePosting exchangePostingUpdate = exchangePosting.get();
        if (exchangePostingApprovalDto.getUnitTypeId()!=0) {
            RoomInfo roomInfo = exchangePostingUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            RoomInfo roomInfoAfterSave = roomInfoRepository.save(roomInfo);
        }
        if (exchangePostingUpdate.getExchangePackage().getId()==2) exchangePostingUpdate.setStatus(String.valueOf(ExchangePostingEnum.Processing));
        exchangePostingUpdate.setNote(exchangePostingApprovalDto.getNote());
        exchangePostingUpdate.setIsVerify(true);
        ExchangePosting exchangePostingInDb = exchangePostingRepository.save(exchangePostingUpdate);
        return exchangePostingApprovalMapper.toDto(exchangePostingInDb);
    }
    @Override
    public ExchangePostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangePosting> exchangePosting = exchangePostingRepository.findById(postingId);
        if (!exchangePosting.isPresent()) throw new OptionalNotFoundException("Not found posting");
        if (!exchangePosting.get().getStatus().equals(String.valueOf(ExchangePostingEnum.PendingApproval))) throw new ErrMessageException("Status must be pending approval");
        exchangePosting.get().setNote(note);
        exchangePosting.get().setStatus(String.valueOf(ExchangePostingEnum.Reject));
        ExchangePosting exchangePostingInDb = exchangePostingRepository.save(exchangePosting.get());
        Optional<Customer> customer = customerRepository.findById(exchangePostingInDb.getOwner().getId());
        if (!customer.isPresent()) throw new ErrMessageException("Error when refund money to customer but reject successfully");
        float fee = 0;
        float money = exchangePostingInDb.getExchangePackage().getPrice()-20000;
        String paymentMethod = "WALLET";
        String description = "Giao dịch hoàn tiền từ chối bài đăng";
        String transactionType = "EXCHANGEPOSTING";
        WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(customer.get().getId(),fee,money,paymentMethod,description,transactionType);
        return exchangePostingApprovalMapper.toDto(exchangePostingInDb);
    }
    @Override
    public Page<PostingExchangeResponseDTO> getAllPostings(Integer resortId, Pageable pageable) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }

        Page<ExchangePosting> exchangePostings;
        if (resortId == null) {
            exchangePostings = exchangePostingRepository.findByOwnerIdAndIsActive(customer.getId(), pageable);
        } else {
            exchangePostings = exchangePostingRepository.findAllByOwnerIdAndIsActiveAndResortId(customer.getId(), resortId, pageable);
        }

        return exchangePostings.map(listExchangePostingMapper::entityToDto);
    }
    @Override
    public Page<PostingExchangeResponseDTO> getAllExchangePublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException {
        Page<ExchangePosting> exchangePostings = exchangePostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndStatus(true,
                resortName, String.valueOf(ExchangePostingEnum.Processing), pageable);
        Page<PostingExchangeResponseDTO> postingDtoPage = exchangePostings.map(listExchangePostingMapper::entityToDto);
        return postingDtoPage;
    }
}
