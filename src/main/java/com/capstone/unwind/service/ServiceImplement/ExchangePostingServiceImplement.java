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
import com.capstone.unwind.service.ServiceInterface.*;
import jakarta.transaction.Transactional;
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
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private final FcmService fcmService;

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
                .preferCheckinDate(exchangePostingRequestDto.getPreferCheckinDate())
                .preferCheckoutDate(exchangePostingRequestDto.getPreferCheckoutDate())
                .preferLocation(exchangePostingRequestDto.getPreferLocation())
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
        if (exchangelPosting.getExchangePackage().getId()==2){
            String title = "Yêu cầu duyệt bài đăng cho trao đổi";
            String content = "Xin chào, bạn vừa có một yêu cầu duyệt bài đăng trao đổi";
            String topic = "resort"+exchangelPosting.getRoomInfo().getResort().getId();
            Notification notification = Notification.builder()
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .type(String.valueOf(NotificationEnum.ExchangePosting))
                    .entityId(exchangelPosting.getId())
                    .role(topic)
                    .build();
            notificationRepository.save(notification);
            try{
                fcmService.pushNotificationTopic(title,content,topic);
            }catch (Exception e){
                e.printStackTrace();
            }
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
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ExchangePosting> exchangePostings = exchangePostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                true, roomInfoCode, String.valueOf(ExchangePostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), sortedPageable);

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
        exchangePostingUpdate.getTimeshare().setIsVerify(true);
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
        String title = "Yêu cầu duyệt bài đăng trao đổi";
        String content = "Bài đăng trao đổi của bạn vừa được duyệt.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangePostingInDb.getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangePosting))
                .entityId(exchangePostingInDb.getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangePostingInDb.getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
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
        String title = "Yêu cầu duyệt bài đăng trao đổi";
        String content = "Bài đăng trao đổi của bạn vừa bị từ chối.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangePostingInDb.getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangePosting))
                .entityId(exchangePostingInDb.getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangePostingInDb.getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
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
    public Page<PostingExchangeResponseDTO> getAllExchangePublicPostings(String resortName,Integer nights, Pageable pageable,Integer resortId) throws OptionalNotFoundException {
        Page<ExchangePosting> exchangePostings = null;
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        if (resortId == null) {
            exchangePostings = exchangePostingRepository.findAllByFilters(true,
                    resortName,nights, String.valueOf(ExchangePostingEnum.Processing), sortedPageable);
        }else {
            exchangePostings = exchangePostingRepository.findAllByFiltersWithResortId(true,
                    resortName, String.valueOf(ExchangePostingEnum.Processing),nights,resortId, sortedPageable);
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
                .status(String.valueOf(ExchangeRequestEnum.PendingOwner))
                .exchangePosting(exchangePosting)
                .priceValuation(exchangeRequestDto.getPriceValuation())
                .isActive(true)
                .note(exchangeRequestDto.getNote())
                .build();
        ExchangeRequestDetailDto exchangeRequestDetailDto = exchangeRequestMapper.toDto(exchangeRequestRepository.save(exchangeRequest));
        String title = "Yêu cầu trao đổi timeshare";
        String content = "Xin chào, bạn có một người yêu cầu trao đổi timeshare từ hệ thống.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangeRequest.getExchangePosting().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangePosting))
                .entityId(exchangeRequest.getExchangePosting().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangeRequest.getExchangePosting().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffOpt.get();
        Page<ExchangeRequest> exchangePostings = exchangeRequestRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                true, roomInfoCode, String.valueOf(ExchangeRequestEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), sortedPageable);

        Page<ExchangeRequestBasicDto> postingDtoPage = exchangePostings.map(exchangeRequestListMapper::toDto);
        return postingDtoPage;
    }

    @Override
    @Transactional
    public ExchangeRequestBasicDto approvalRequestTimeshareStaff(Integer requestId, ExchangePostingApprovalDto exchangePostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<ExchangeRequest> exchangeRequest = exchangeRequestRepository.findByIdAndIsActive(requestId);
        if (!exchangeRequest.isPresent()) throw new OptionalNotFoundException("Not found exchange posting");
        if (!exchangeRequest.get().getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        Optional<UnitType> unitType = null;
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            unitType = unitTypeRepository.findById(exchangePostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }
        ExchangeRequest exchangeRequestUpdate = exchangeRequest.get();
        if (exchangePostingApprovalDto.getUnitTypeId() != 0) {
            RoomInfo roomInfo = exchangeRequestUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            RoomInfo roomInfoAfterSave = roomInfoRepository.save(roomInfo);
        }
        exchangeRequestUpdate.getExchangePosting().setStatus(String.valueOf(ExchangePostingEnum.Completed));
        exchangeRequestUpdate.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
        exchangeRequestUpdate.setNote(exchangePostingApprovalDto.getNote());
        if (exchangeRequestUpdate.getPriceValuation() > 0 ){
            exchangeRequestUpdate.setStatus(String.valueOf(ExchangeRequestEnum.PendingRenterPayment));
        }else if(exchangeRequestUpdate.getPriceValuation()<0){
            exchangeRequestUpdate.setStatus(String.valueOf(ExchangeRequestEnum.PendingOwnerPayment));
        }
        ExchangeRequest exchangeRequestInDb = exchangeRequestRepository.save(exchangeRequestUpdate);


        if (exchangeRequestInDb.getPriceValuation()== 0.0f){
            exchangeRequestRepository.updateOtherRequestsStatusByExchangePosting(
                    exchangeRequestUpdate.getExchangePosting().getId(),
                    exchangeRequestUpdate.getId()
            );
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
                    .isPrimaryGuest(false)
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
                    .isPrimaryGuest(false)
                    .isActive(true)
                    .build();
            exchangeBookingRepository.save(ownerBooking);
            exchangePostingRepository.closeProcessingExchangePostingsByOwner(requesterBooking.getExchangePosting().getOwner().getId(),ownerBooking.getCheckinDate().getYear());

        }

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
        String title = "Yêu cầu trao đổi đã được duyệt";
        String content = "Xin chào, yêu cầu  trao đổi của bạn đã được duyệt ";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangeRequest.get().getExchangePosting().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangePosting))
                .entityId(exchangeRequest.get().getExchangePosting().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangeRequest.get().getExchangePosting().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }

        String titleRenter = "Yêu cầu trao đổi đã được duyệt";
        String contentRenter = "Xin chào, yêu cầu  trao đổi của bạn đã được duyệt ";
        Notification notificationRenter = Notification.builder()
                .title(titleRenter)
                .content(contentRenter)
                .isRead(false)
                .userId(exchangeRequest.get().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangeRequest))
                .entityId(exchangeRequest.get().getId())
                .build();
        notificationRepository.save(notificationRenter);
        String fcmTokenRenter = exchangeRequest.get().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmTokenRenter,title,content);
        }catch (Exception e){
            e.printStackTrace();
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
        exchangeRequest.get().setStatus(String.valueOf(ExchangeRequestEnum.RejectApproval));
        ExchangeRequest exchangePostingInDb = exchangeRequestRepository.save(exchangeRequest.get());
        ExchangePosting exchangePosting = exchangeRequest.get().getExchangePosting();
        exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Processing));
        exchangePostingRepository.save(exchangePosting);
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
        String title = "Yêu cầu trao đổi ";
        String content = "Xin chào, yêu cầu  trao đổi của bạn đã bị từ chối";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangeRequest.get().getExchangePosting().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangePosting))
                .entityId(exchangeRequest.get().getExchangePosting().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangeRequest.get().getExchangePosting().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }

        String titleRenter = "Yêu cầu trao đổi ";
        String contentRenter = "Xin chào, yêu cầu  trao đổi của bạn đã bị từ chối ";
        Notification notificationRenter = Notification.builder()
                .title(titleRenter)
                .content(contentRenter)
                .isRead(false)
                .userId(exchangeRequest.get().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangeRequest))
                .entityId(exchangeRequest.get().getId())
                .build();
        notificationRepository.save(notificationRenter);
        String fcmTokenRenter = exchangeRequest.get().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmTokenRenter,title,content);
        }catch (Exception e){
            e.printStackTrace();
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
        if (exchangePosting.getExchangePackage().getId() == 2 && (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingOwner))||exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPricing)))) {
            if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingOwner))){
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người chủ đồng ý";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangeRequest))
                        .entityId(exchangeRequest.getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if(exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPricing))){
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người thuê đồng ý";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getExchangePosting().getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangePosting))
                        .entityId(exchangeRequest.getExchangePosting().getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getExchangePosting().getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.PendingApproval));
            exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Accepted));

        } else if (exchangePosting.getExchangePackage().getId() == 1) {
            exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
            exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Completed));
            Period period = Period.between(exchangeRequest.getStartDate(), exchangeRequest.getEndDate());
            int days = period.getDays() + 1;

            ExchangeBooking requesterBooking =  ExchangeBooking.builder()
                    .isActive(true)
                    .checkoutDate(exchangeRequest.getEndDate())
                    .checkinDate(exchangeRequest.getStartDate())
                    .status(String.valueOf(ExchangeBookingEnum.Booked))
                    .exchangeRequest(exchangeRequest)
                    .isPrimaryGuest(false)
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
                    .isPrimaryGuest(false)
                    .isFeedback(false)
                    .isActive(true)
                    .build();
            exchangeBookingRepository.save(ownerBooking);
            exchangePostingRepository.closeProcessingExchangePostingsByOwner(requesterBooking.getExchangePosting().getOwner().getId(),ownerBooking.getCheckinDate().getYear());
        } else {
            throw new ErrMessageException("Invalid request status or package type");
        }
        if(exchangePosting.getExchangePackage().getId()==2){
            String titleSystemStaff = "Yêu cầu duyệt yêu cầu trao đổi";
            String contentSystemStaff = "Xin chào, bạn vừa có một yêu cầu duyệt yêu cầu trao đổi từ một khách hàng";
            String topicSystemStaff = "resort"+exchangePosting.getRoomInfo().getResort().getId();
            Notification notificationSystemStaff = Notification.builder()
                    .title(titleSystemStaff)
                    .content(contentSystemStaff)
                    .isRead(false)
                    .type(String.valueOf(NotificationEnum.ExchangeRequest))
                    .entityId(exchangeRequest.getId())
                    .role(topicSystemStaff)
                    .build();
            notificationRepository.save(notificationSystemStaff);
            try{
                fcmService.pushNotificationTopic(titleSystemStaff,contentSystemStaff,topicSystemStaff);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        exchangeRequestRepository.save(exchangeRequest);
        exchangePostingRepository.save(exchangePosting);

        return exchangeRequestListMapper.toDto(exchangeRequest);
    }
    @Transactional
    @Override
    public ExchangePostingResponseDto updateExchangePosting(Integer postingId, UpdateExchangePostingDto updateExchangePostingDto)
            throws  ErrMessageException {

        ExchangePosting exchange = exchangePostingRepository.findById(postingId)
                .orElseThrow(() -> new ErrMessageException("posting with ID " + postingId + " does not exist"));

        if (!exchange.getStatus().equals(String.valueOf(ExchangePostingEnum.Processing))){
            throw new ErrMessageException("Status must be Processing");
        }
        exchange.setDescription(updateExchangePostingDto.getDescription());
        exchange.setIsActive(true);

        ExchangePosting updatedExchangePosting = exchangePostingRepository.save(exchange);
        documentStoreRepository.deactivateOldImages(updatedExchangePosting.getId(), DocumentStoreEnum.ExchangePosting.toString());

        try {
            for (String imageUrl : updateExchangePostingDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.ExchangePosting.toString());
                document.setEntityId(updatedExchangePosting.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        return exchangePostingResponseMapper.toDto(updatedExchangePosting);
    }

    @Override
    public ExchangeRequestBasicDto rejectRequestCustomer(Integer requestId) throws OptionalNotFoundException, ErrMessageException {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findByIdAndIsActive(requestId).orElseThrow(()->new OptionalNotFoundException("Not found request exchange"));
        if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPricing)) || exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingOwner))){
            if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPricing))){
                exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.RenterReject));
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người thuê từ chối ";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getExchangePosting().getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangePosting))
                        .entityId(exchangeRequest.getExchangePosting().getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getExchangePosting().getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.OwnerReject));
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người chủ từ chối ";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangeRequest))
                        .entityId(exchangeRequest.getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            throw new ErrMessageException("Must be status pending renter pricing or pending owner");
        }
        ExchangeRequest exchangeRequestInDb = exchangeRequestRepository.save(exchangeRequest);

        return exchangeRequestListMapper.toDto(exchangeRequestInDb);
    }

    @Override
    public ExchangeRequestBasicDto pricingRequest(Integer requestId, Float priceValuation,String note) throws OptionalNotFoundException, ErrMessageException {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findByIdAndIsActive(requestId).orElseThrow(()->new OptionalNotFoundException("Not found request exchange"));
        if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPricing))||exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingOwner))){
            if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingOwner))){
                exchangeRequest.setPriceValuation(priceValuation);
                exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.PendingRenterPricing));
                exchangeRequest.setNote(note);
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người chủ thảo luận lại về giá tiền";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangeRequest))
                        .entityId(exchangeRequest.getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                exchangeRequest.setPriceValuation(priceValuation);
                exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.PendingOwner));
                exchangeRequest.setNote(note);
                String title = "Yêu cầu trao đổi";
                String content = "Xin chào, yêu cầu trao đổi của bạn đã được người thuê yêu cầu trao đổi lại v giá";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .userId(exchangeRequest.getExchangePosting().getOwner().getUser().getId())
                        .type(String.valueOf(NotificationEnum.ExchangePosting))
                        .entityId(exchangeRequest.getExchangePosting().getId())
                        .build();
                notificationRepository.save(notification);
                String fcmToken = exchangeRequest.getExchangePosting().getOwner().getUser().getFcmToken();
                try{
                    fcmService.pushNotification(fcmToken,title,content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            throw new ErrMessageException("Must be status pending renter pricing or pending owner");
        }
        ExchangeRequest exchangeRequestInDb = exchangeRequestRepository.save(exchangeRequest);
        return exchangeRequestListMapper.toDto(exchangeRequestInDb);
    }


}
