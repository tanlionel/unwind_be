package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.config.FeeConfig;
import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.*;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.capstone.unwind.config.EmailMessageConfig.*;

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
    @Autowired
    private final ExchangeRequestRepository exchangeRequestRepository;
    @Autowired
    private final ExchangeRequestMapper exchangeRequestMapper;
    @Autowired
    private final ExchangeRequestListMapper exchangeRequestListMapper;
    @Autowired
    private final ExchangeRequestPostingListMapper exchangeRequestPostingListMapper;
    @Autowired
    private final SendinblueService sendinblueService;
    @Autowired
    private final ExchangeBookingRepository exchangeBookingRepository;

    @Override
    public ExchangePostingResponseDto createExchangePosting(ExchangePostingRequestDto exchangePostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new ErrMessageException("Not init customer yet");
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
        if (exchangePostingRequestDto.getExchangePackageId() == 1)
            exchangelPosting.setStatus(String.valueOf(ExchangePostingEnum.Processing));
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
        // Gửi email thông báo
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setName(exchangePostingInDb.getOwner().getFullName());
            emailRequestDto.setSubject(CREATE_EXCHANGE_POSTING_SUBJECT);
            emailRequestDto.setContent(CREATE_EXCHANGE_POSTING_CONTENT);
            emailRequestDto.setTransactionType("EXCHANGEPOSTING");
            emailRequestDto.setMoney(exchangePostingInDb.getExchangePackage().getPrice());

            sendinblueService.sendEmailWithTemplate(
                    user.getEmail(),
                    EmailEnum.TRANSACTION_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
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
        Page<ExchangePosting> exchangePostings = exchangePostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                true, roomInfoCode, String.valueOf(ExchangePostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), pageable);

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
    public PostingExchangeDetailResponseDTO deActiveExchangePostingPosting(Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        ExchangePosting exchangePosting = exchangePostingRepository.findById(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Exchange Posting not found with ID"));
        if (!exchangePosting.getStatus().equals(String.valueOf(ExchangePostingEnum.Processing))) {
            throw new ErrMessageException("Exchange Posting must be in 'Processing' status to be deactivated");
        }
        exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Closed));
        ExchangePosting updatedExchangePosting = exchangePostingRepository.save(exchangePosting);
        return postingExchangeDetailMapper.entityToDto(exchangePosting);
    }
    @Override
    public ExchangePostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, ExchangePostingApprovalDto exchangePostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangePosting> exchangePosting = exchangePostingRepository.findByIdAndIsActive(postingId);
        if (!exchangePosting.isPresent()) throw new OptionalNotFoundException("Not found exchange posting");
        if (!exchangePosting.get().getStatus().equals(String.valueOf(ExchangePostingEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");

        // Kiểm tra và lấy UnitType nếu cần
        Optional<UnitType> unitType = null;
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            unitType = unitTypeRepository.findById(exchangePostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }

        ExchangePosting exchangePostingUpdate = exchangePosting.get();
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            RoomInfo roomInfo = exchangePostingUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            roomInfoRepository.save(roomInfo);
        }

        if (exchangePostingUpdate.getExchangePackage().getId() == 2) {
            exchangePostingUpdate.setStatus(String.valueOf(ExchangePostingEnum.Processing));
        }
        exchangePostingUpdate.setNote(exchangePostingApprovalDto.getNote());
        exchangePostingUpdate.setIsVerify(true);
        ExchangePosting exchangePostingInDb = exchangePostingRepository.save(exchangePostingUpdate);

        try{
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ duyệt bài đăng cho trao đổi với mã " + exchangePostingInDb.getId();
            String transactionType = "APPROVAL_EXCHANGEPOSTING";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee,money,paymentMethod,description,transactionType,timeshareCompanyStaff.getTimeshareCompanyId());
        }catch (Exception e){
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setName(exchangePostingInDb.getOwner().getFullName());
            emailRequestDto.setSubject(APPROVAL_EXCHANGE_POSTING_SUBJECT);
            emailRequestDto.setContent(APPROVAL_EXCHANGE_POSTING_CONTENT);
            emailRequestDto.setTransactionType("EXCHANGEPOSTING");
            emailRequestDto.setMoney(exchangePostingInDb.getExchangePackage().getPrice());

            sendinblueService.sendEmailWithTemplate(
                    exchangePostingInDb.getOwner().getUser().getEmail(),
                    EmailEnum.TRANSACTION_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }

        return exchangePostingApprovalMapper.toDto(exchangePostingInDb);
    }


    @Override
    public ExchangePostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangePosting> exchangePosting = exchangePostingRepository.findById(postingId);
        if (!exchangePosting.isPresent()) throw new OptionalNotFoundException("Not found posting");
        if (!exchangePosting.get().getStatus().equals(String.valueOf(ExchangePostingEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        exchangePosting.get().setNote(note);
        exchangePosting.get().setStatus(String.valueOf(ExchangePostingEnum.Reject));
        ExchangePosting exchangePostingInDb = exchangePostingRepository.save(exchangePosting.get());
        Optional<Customer> customer = customerRepository.findById(exchangePostingInDb.getOwner().getId());
        if (!customer.isPresent())
            throw new ErrMessageException("Error when refund money to customer but reject successfully");
        float feeCustomer = FeeConfig.fee_reject;
        float moneyCustomer = exchangePostingInDb.getExchangePackage().getPrice() - feeCustomer;
        String paymentMethodCustomer = "WALLET";
        String descriptionCustomer = "Giao dịch hoàn tiền từ chối bài đăng";
        String transactionTypeCustomer = "EXCHANGEREFUND";
        WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(customer.get().getId(), feeCustomer, moneyCustomer, paymentMethodCustomer, descriptionCustomer, transactionTypeCustomer);

        try{
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ từ chối bài đăng cho trao đổi với mã " + exchangePostingInDb.getId();
            String transactionType = "REJECT_EXCHANGEPOSTING";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee,money,paymentMethod,description,transactionType,timeshareCompanyStaff.getTimeshareCompanyId());
        }catch (Exception e){
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setName(exchangePostingInDb.getOwner().getFullName());
            emailRequestDto.setSubject(REJECT_EXCHANGE_POSTING_SUBJECT);
            emailRequestDto.setContent(REJECT_EXCHANGE_POSTING_CONTENT);
            emailRequestDto.setTransactionType(walletTransaction.getTransactionType());
            emailRequestDto.setMoney(walletTransaction.getMoney());

            sendinblueService.sendEmailWithTemplate(
                    exchangePostingInDb.getOwner().getUser().getEmail(),
                    EmailEnum.TRANSACTION_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return exchangePostingApprovalMapper.toDto(exchangePostingInDb);
    }

    @Override
    public Page<PostingExchangeResponseDTO> getAllPostings(Integer resortId, Pageable pageable, String status) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }

        Specification<ExchangePosting> spec = Specification.where(ExchangePostingSpecification.hasOwnerId(customer.getId()))
                .and(ExchangePostingSpecification.isActive(true));

        if (resortId != null) {
            spec = spec.and(ExchangePostingSpecification.hasResortId(resortId));
        }
        if (status != null) {
            spec = spec.and(ExchangePostingSpecification.hasStatus(status));
        }
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        }

        Page<ExchangePosting> rentalPostings = exchangePostingRepository.findAll(spec, pageable);
        return rentalPostings.map(listExchangePostingMapper::entityToDto);
    }

    @Override
    public Page<PostingExchangeResponseDTO> getAllExchangePublicPostings(String resortName, Pageable pageable,Integer resortId) throws OptionalNotFoundException {
        Page<ExchangePosting> exchangePostings = null;
        if (resortId == null) {
            exchangePostings = exchangePostingRepository.findAllByFilters(true,
                    resortName, String.valueOf(ExchangePostingEnum.Processing), pageable);
        }else {
            exchangePostings = exchangePostingRepository.findAllByFiltersWithResortId(true,
                    resortName, String.valueOf(ExchangePostingEnum.Processing),resortId, pageable);
        }
        Page<PostingExchangeResponseDTO> postingDtoPage = exchangePostings.map(listExchangePostingMapper::entityToDto);
        return postingDtoPage;
    }

    @Override
    public ExchangeRequestDetailDto createRequestExchange(Integer postingId, ExchangeRequestDto exchangeRequestDto) throws OptionalNotFoundException, ErrMessageException {
        Customer customer = userService.getLoginUser().getCustomer();
        if (customer == null) throw new ErrMessageException("Customer has not login yet");
        ExchangePosting exchangePosting = exchangePostingRepository.findByIdAndIsActive(postingId).orElseThrow(() -> new OptionalNotFoundException("not found exchange posting"));
        if (exchangePosting.getIsExchange() || !exchangePosting.getStatus().equals(String.valueOf(ExchangePostingEnum.Processing)))
            throw new ErrMessageException("Status must be processing or exchange posting not booked yet");
        if (customer.getId() == exchangePosting.getOwner().getId())
            throw new ErrMessageException("Can not book yourself");
        Timeshare timeshare = timeShareRepository.findById(exchangeRequestDto.getTimeshareId()).orElseThrow(() -> new OptionalNotFoundException("Not found timeshare"));
        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .timeshare(timeshare)
                .roomInfo(timeshare.getRoomInfo())
                .owner(customer)
                .startDate(exchangeRequestDto.getStartDate())
                .endDate(exchangeRequestDto.getEndDate())
                .status(String.valueOf(ExchangeRequestEnum.PendingCustomer))
                .exchangePosting(exchangePosting)
                .isActive(true)
                .build();
        ExchangeRequestDetailDto exchangeRequestDetailDto = exchangeRequestMapper.toDto(exchangeRequestRepository.save(exchangeRequest));

        return exchangeRequestDetailDto;
    }

    @Override
    public ExchangeRequestDetailDto getExchangeRequestById(Integer requestId) throws OptionalNotFoundException {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(requestId).orElseThrow(() -> new OptionalNotFoundException("Not found exchange request"));
        return exchangeRequestMapper.toDto(exchangeRequest);
    }

    @Override
    public Page<ExchangeRequestBasicDto> getPaginationExchangeRequest(int pageNo, int pageSize) throws ErrMessageException {
        Customer customer = userService.getLoginUser().getCustomer();
        if (customer == null) throw new ErrMessageException("Not login yet");
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        Page<ExchangeRequest> exchangeRequests = exchangeRequestRepository.findAllByIsActiveAndOwnerId(true, customer.getId(), pageable);
        Page<ExchangeRequestBasicDto> exchangeRequestBasicDtos = exchangeRequests.map(exchangeRequestListMapper::toDto);
        return exchangeRequestBasicDtos;
    }

    @Override
    public Page<ExchangeRequestPostingBasicDto> getPaginationExchangeRequestByPostingId(int pageNo, int pageSize, int postingId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        Page<ExchangeRequest> exchangePostings = exchangeRequestRepository.findAllByIsActiveAndExchangePostingId(true, postingId, pageable);
        Page<ExchangeRequestPostingBasicDto> exchangeRequestPostingBasicDtos = exchangePostings.map(exchangeRequestPostingListMapper::toDto);
        return exchangeRequestPostingBasicDtos;
    }

    @Override
    public Page<ExchangeRequestBasicDto> getAllExchangeRequestTsStaff(String roomInfoCode, Pageable pageable) throws OptionalNotFoundException {
        TimeShareCompanyStaffDTO user = timeShareStaffService.getLoginStaff();
        Optional<TimeshareCompanyStaff> timeshareCompanyStaffOpt = timeshareCompanyStaffRepository.findById(user.getId());
        if (timeshareCompanyStaffOpt.isEmpty()) {
            throw new OptionalNotFoundException("Timeshare company staff not found with id: " + user.getId());
        }
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffOpt.get();
        Page<ExchangeRequest> exchangePostings = exchangeRequestRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                true, roomInfoCode, String.valueOf(ExchangeRequestEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), pageable);

        Page<ExchangeRequestBasicDto> postingDtoPage = exchangePostings.map(exchangeRequestListMapper::toDto);
        return postingDtoPage;
    }

    @Override
    public ExchangeRequestBasicDto approvalRequestTimeshareStaff(Integer requestId, ExchangePostingApprovalDto exchangePostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangeRequest> ExchangeRequest = exchangeRequestRepository.findByIdAndIsActive(requestId);
        if (!ExchangeRequest.isPresent()) throw new OptionalNotFoundException("Not found exchange posting");
        if (!ExchangeRequest.get().getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        Optional<UnitType> unitType = null;
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            unitType = unitTypeRepository.findById(exchangePostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }
        ExchangeRequest exchangePostingUpdate = ExchangeRequest.get();
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            RoomInfo roomInfo = exchangePostingUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            RoomInfo roomInfoAfterSave = roomInfoRepository.save(roomInfo);
        }
        exchangePostingUpdate.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
        exchangePostingUpdate.setNote(exchangePostingApprovalDto.getNote());
        ExchangeRequest exchangeRequestInDb = exchangeRequestRepository.save(exchangePostingUpdate);
        Period period = Period.between(exchangeRequestInDb.getStartDate(), exchangeRequestInDb.getEndDate());
        int days = period.getDays() + 1;

        ExchangeBooking requesterBooking =  ExchangeBooking.builder()
                .isActive(true)
                .checkoutDate(exchangeRequestInDb.getEndDate())
                .checkinDate(exchangeRequestInDb.getStartDate())
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .exchangeRequest(exchangeRequestInDb)
                .exchangePosting(exchangeRequestInDb.getExchangePosting())
                .renter(exchangeRequestInDb.getExchangePosting().getOwner())
                .roomInfo(exchangeRequestInDb.getRoomInfo())
                .timeshare(exchangeRequestInDb.getTimeshare())
                .nights(days)
                .isFeedback(false)
                .build();
        exchangeBookingRepository.save(requesterBooking);

        ExchangeBooking ownerBooking = ExchangeBooking.builder()
                .timeshare(exchangeRequestInDb.getExchangePosting().getTimeshare())
                .roomInfo(exchangeRequestInDb.getExchangePosting().getRoomInfo())
                .renter(exchangeRequestInDb.getOwner())
                .exchangePosting(exchangeRequestInDb.getExchangePosting())
                .exchangeRequest(exchangeRequestInDb)
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .checkoutDate(exchangeRequestInDb.getExchangePosting().getCheckoutDate())
                .checkinDate(exchangeRequestInDb.getExchangePosting().getCheckinDate())
                .nights(exchangeRequestInDb.getExchangePosting().getNights())
                .isFeedback(false)
                .isActive(true)
                .build();
        exchangeBookingRepository.save(ownerBooking);
        try{
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ duyệt yêu cầu trao đổi với mã " + exchangeRequestInDb.getId();
            String transactionType = "APPROVAL_REQUESTEXCHANGE";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee,money,paymentMethod,description,transactionType,timeshareCompanyStaff.getTimeshareCompanyId());
        }catch (Exception e){
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setName(exchangeRequestInDb.getOwner().getFullName());
            emailRequestDto.setSubject(APPROVAL_EXCHANGE_POSTING_SUBJECT);
            emailRequestDto.setContent(APPROVAL_EXCHANGE_POSTING_CONTENT);
            emailRequestDto.setMoney(exchangeRequestInDb.getExchangePosting().getExchangePackage().getPrice());
            List<String> recipients = Arrays.asList(
                    exchangeRequestInDb.getExchangePosting().getOwner().getUser().getEmail(),
                    exchangeRequestInDb.getOwner().getUser().getEmail()
            );
            for (String recipient : recipients) {
                sendinblueService.sendEmailWithTemplate(
                        recipient,
                        EmailEnum.BASIC_MAIL,
                        emailRequestDto
                );
            }
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return exchangeRequestListMapper.toDto(exchangeRequestInDb);
    }

    @Override
    public ExchangeRequestBasicDto rejectRequestTimeshareStaff(Integer requestId, String note) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangeRequest> exchangeRequest = exchangeRequestRepository.findById(requestId);
        if (!exchangeRequest.isPresent()) throw new OptionalNotFoundException("Not found posting");
        if (!exchangeRequest.get().getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        exchangeRequest.get().setNote(note);
        exchangeRequest.get().setStatus(String.valueOf(ExchangeRequestEnum.Reject));
        ExchangeRequest exchangePostingInDb = exchangeRequestRepository.save(exchangeRequest.get());
        try{
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ từ chối yêu cầu trao đổi với mã " + exchangePostingInDb.getId();
            String transactionType = "REJECT_REQUESTEXCHANGE";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee,money,paymentMethod,description,transactionType,timeshareCompanyStaff.getTimeshareCompanyId());
        }catch (Exception e){
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject(REJECT_EXCHANGE_REQUEST_SUBJECT);
            emailRequestDto.setContent(REJECT_EXCHANGE_REQUEST_SUBJECT);
            sendinblueService.sendEmailWithTemplate(
                    exchangePostingInDb.getOwner().getUser().getEmail(),
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return exchangeRequestListMapper.toDto(exchangePostingInDb);
    }

    @Override
    public ExchangeRequestBasicDto approvalRequestCustomer(Integer requestId) throws OptionalNotFoundException, ErrMessageException {

        Optional<ExchangeRequest> exchangeRequestOpt = exchangeRequestRepository.findByIdAndIsActive(requestId);
        if (!exchangeRequestOpt.isPresent()) {
            throw new OptionalNotFoundException("Not found exchange posting");
        }
        ExchangeRequest exchangeRequest = exchangeRequestOpt.get();
        ExchangePosting exchangePosting = exchangeRequest.getExchangePosting();
        if (exchangePosting == null) {
            throw new OptionalNotFoundException("Exchange Posting not found");
        }
        if (exchangePosting.getExchangePackage().getId() == 2) {
            exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.PendingApproval));
            exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Accepted));
        } else if (exchangePosting.getExchangePackage().getId() == 1) {
            exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
            exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Accepted));
            Period period = Period.between(exchangeRequest.getStartDate(), exchangeRequest.getEndDate());
            int days = period.getDays() + 1;

            ExchangeBooking requesterBooking =  ExchangeBooking.builder()
                    .isActive(true)
                    .checkoutDate(exchangeRequest.getEndDate())
                    .checkinDate(exchangeRequest.getStartDate())
                    .status(String.valueOf(ExchangeBookingEnum.Booked))
                    .exchangeRequest(exchangeRequest)
                    .exchangePosting(exchangeRequest.getExchangePosting())
                    .renter(exchangeRequest.getExchangePosting().getOwner())
                    .roomInfo(exchangeRequest.getRoomInfo())
                    .timeshare(exchangeRequest.getTimeshare())
                    .nights(days)
                    .isFeedback(false)
                    .build();
            exchangeBookingRepository.save(requesterBooking);

            ExchangeBooking ownerBooking = ExchangeBooking.builder()
                    .timeshare(exchangeRequest.getExchangePosting().getTimeshare())
                    .roomInfo(exchangeRequest.getExchangePosting().getRoomInfo())
                    .renter(exchangeRequest.getOwner())
                    .exchangePosting(exchangeRequest.getExchangePosting())
                    .exchangeRequest(exchangeRequest)
                    .status(String.valueOf(ExchangeBookingEnum.Booked))
                    .checkoutDate(exchangeRequest.getExchangePosting().getCheckoutDate())
                    .checkinDate(exchangeRequest.getExchangePosting().getCheckinDate())
                    .nights(exchangeRequest.getExchangePosting().getNights())
                    .isFeedback(false)
                    .isActive(true)
                    .build();
            exchangeBookingRepository.save(ownerBooking);
            try {
                EmailRequestDto emailRequestDto = new EmailRequestDto();
                emailRequestDto.setSubject(APPROVAL_EXCHANGE_REQUEST_SUBJECT);
                emailRequestDto.setContent(APPROVAL_EXCHANGE_REQUEST_CONTENT);
                sendinblueService.sendEmailWithTemplate(
                        exchangeRequest.getOwner().getUser().getEmail(),
                        EmailEnum.BASIC_MAIL,
                        emailRequestDto
                );
            } catch (Exception e) {
                throw new ErrMessageException("Failed to send email notification");
            }
        } else {
            throw new ErrMessageException("Invalid exchange package type");
        }

        exchangeRequestRepository.save(exchangeRequest);
        exchangePostingRepository.save(exchangePosting);

        return exchangeRequestListMapper.toDto(exchangeRequest);
    }

}
