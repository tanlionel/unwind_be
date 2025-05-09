package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.config.FeeConfig;
import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.*;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackReportResponseDto;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.ResortDTO.ResortRequestDTO;
import com.capstone.unwind.model.SystemDTO.PolicyMapper;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;
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
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.capstone.unwind.config.EmailMessageConfig.*;
import static com.capstone.unwind.config.EmailMessageConfig.CREATE_RENTAL_POSTING_CONTENT;

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
    @Autowired
    private final DocumentStoreRepository documentStoreRepository;
    @Autowired
    private final SendinblueService sendinblueService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FcmService fcmService;


    /*    private final String processing = "Processing";
    private final String pendingPricing = "PendingPricing";
    private final String  pendingApproval = "PendingApproval";*/
    @Override
    public Page<PostingResponseDTO> getAllPostings(Integer resortId, Pageable pageable, String status) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }

        Specification<RentalPosting> spec = Specification.where(RentalPostingSpecification.hasOwnerId(customer.getId()))
                .and(RentalPostingSpecification.isActive(true));

        if (resortId != null) {
            spec = spec.and(RentalPostingSpecification.hasResortId(resortId));
        }
        if (status != null) {
            spec = spec.and(RentalPostingSpecification.hasStatus(status));
        }
        spec = spec.and(RentalPostingSpecification.hasPackageAndStatus());
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        }

        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAll(spec, pageable);
        return rentalPostings.map(listRentalPostingMapper::entityToDto);
    }

    @Override
    public Page<PostingResponseDTO> getAllPublicPostings(String resortName,Integer nights, Pageable pageable) throws OptionalNotFoundException {
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByFilters(true,
                resortName,nights, String.valueOf(RentalPostingEnum.Processing), pageable);
        Page<PostingResponseDTO> postingDtoPage = rentalPostings.map(listRentalPostingMapper::entityToDto);
        return postingDtoPage;
    }

    @Override
    public PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(rentalPosting.getId(), DocumentStoreEnum.RentalPosting.toString());
        PostingDetailResponseDTO responseDTO = postingDetailMapper.entityToDto(rentalPosting);
        responseDTO.setImageUrls(imageUrls);
        return responseDTO;
    }

    @Override
    public PostingDetailResponseDTO deActiveRentalPosting(Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findById(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID"));
        if (!rentalPosting.getStatus().equals(String.valueOf(RentalPostingEnum.Processing))) {
            throw new ErrMessageException("Rental Posting must be in 'Processing' status to be deactivated");
        }
        rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Closed));
        RentalPosting updatedRentalPosting = rentalPostingRepository.save(rentalPosting);
        return postingDetailMapper.entityToDto(updatedRentalPosting);
    }

    @Override
    public Page<PostingResponseTsStaffDTO> getAllPostingsTsStaff(String roomInfoCode, Integer packageId, Pageable pageable) throws OptionalNotFoundException {
        TimeShareCompanyStaffDTO user = timeShareStaffService.getLoginStaff();
        Optional<TimeshareCompanyStaff> timeshareCompanyStaffOpt = timeshareCompanyStaffRepository.findById(user.getId());
        if (timeshareCompanyStaffOpt.isEmpty()) {
            throw new OptionalNotFoundException("Timeshare company staff not found with id: " + user.getId());
        }
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffOpt.get();
        Page<RentalPosting> rentalPostings = null;
        if (packageId != null) {
            rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_IdAndRentalPackageId(
                    true, roomInfoCode, String.valueOf(RentalPostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), packageId, sortedPageable);
        } else {
            rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_RoomInfoCodeContainingAndStatusAndRoomInfo_Resort_Id(
                    true, roomInfoCode, String.valueOf(RentalPostingEnum.PendingApproval), timeshareCompanyStaff.getResort().getId(), sortedPageable);
        }

        Page<PostingResponseTsStaffDTO> postingDtoPage = rentalPostings.map(listRentalPostingTsStaffMapper::entityToDto);
        return postingDtoPage;
    }

    @Override
    public Page<PostingResponseTsStaffDTO> getAllPostingsSystemStaff(String resortName, Pageable pageable, String status) throws OptionalNotFoundException {
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<RentalPosting> rentalPostings;
        if (status == null) {
            rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRentalPackage_Id(
                    true, resortName, 4, sortedPageable);
        } else {
            rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRentalPackage_IdAndStatus(
                    true, resortName, 4, sortedPageable, status);
        }
        return rentalPostings.map(listRentalPostingTsStaffMapper::entityToDto);
    }

    public Page<PostingResponseTsStaffDTO> getAllPackagePostingSystemStaff(String resortName, Pageable pageable, String status, Integer packageId) throws OptionalNotFoundException {
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Specification<RentalPosting> spec = Specification.where(RentalPostingSpecification.isActive(true))
                .and(RentalPostingSpecification.resortNameContains(resortName));

        if (status != null) {
            spec = spec.and(RentalPostingSpecification.hasStatus(status));
        }

        if (packageId != null) {
            spec = spec.and(RentalPostingSpecification.hasPackageId(packageId));
        }

        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAll(spec, sortedPageable);
        return rentalPostings.map(listRentalPostingTsStaffMapper::entityToDto);
    }

    @Override
    public RentalPostingResponseDto createRentalPosting(RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new ErrMessageException("Not init customer yet");
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
        if (rentalPostingRequestDto.getRentalPackageId() == 1)
            rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Processing));
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting);
        try {
            for (String imageUrl : rentalPostingRequestDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.RentalPosting.toString());
                document.setEntityId(rentalPostingInDb.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setName(rentalPostingInDb.getOwner().getFullName());
            emailRequestDto.setSubject(CREATE_RENTAL_POSTING_SUBJECT);
            emailRequestDto.setContent(CREATE_RENTAL_POSTING_CONTENT);
            emailRequestDto.setTransactionType("RENTALPOSTING");
            emailRequestDto.setMoney(rentalPostingInDb.getRentalPackage().getPrice());

            sendinblueService.sendEmailWithTemplate(
                    user.getEmail(),
                    EmailEnum.TRANSACTION_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        if (rentalPosting.getRentalPackage().getId()!=1){
            String title = "Yêu cầu duyệt bài đăng cho thuê";
            String content = "Xin chào, bạn vừa có một yêu cầu duyệt bài đăng đăng cho thuê từ một khách hàng";
            String topic = "resort"+rentalPosting.getRoomInfo().getResort().getId();
            Notification notification = Notification.builder()
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .type(String.valueOf(NotificationEnum.RentalPosting))
                    .entityId(rentalPosting.getId())
                    .role(topic)
                    .build();
            notificationRepository.save(notification);
            try{
                fcmService.pushNotificationTopic(title,content,topic);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (rentalPosting.getRentalPackage().getId()==4){
                String titleSystemStaff = "Yêu cầu duyệt bài đăng cho thuê";
                String contentSystemStaff = "Xin chào, bạn vừa có một yêu cầu duyệt bài đăng đăng cho thuê từ một khách hàng";
                String topicSystemStaff = "systemstaff";
                Notification notificationSystemStaff = Notification.builder()
                        .title(titleSystemStaff)
                        .content(contentSystemStaff)
                        .isRead(false)
                        .type(String.valueOf(NotificationEnum.RentalPosting))
                        .entityId(rentalPosting.getId())
                        .role(String.valueOf(UserRole.SYSTEMSTAFF))
                        .build();
                notificationRepository.save(notificationSystemStaff);
                try{
                    fcmService.pushNotificationTopic(title,content,topicSystemStaff);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return rentalPostingResponseMapper.toDto(rentalPostingInDb);
    }

    @Override
    public RentalPostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, RentalPostingApprovalDto rentalPostingApprovalDto) throws OptionalNotFoundException, ErrMessageException {
        Optional<RentalPosting> rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId);
        if (!rentalPosting.isPresent()) throw new OptionalNotFoundException("Not found rental posting");
        if (!rentalPosting.get().getStatus().equals(String.valueOf(RentalPostingEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        Optional<UnitType> unitType = null;
        if (rentalPostingApprovalDto.getUnitTypeId() != 0) {
            unitType = unitTypeRepository.findById(rentalPostingApprovalDto.getUnitTypeId());
            if (!unitType.isPresent()) throw new OptionalNotFoundException("Not found unit type ");
        }
        RentalPosting rentalPostingUpdate = rentalPosting.get();
        if (rentalPostingApprovalDto.getUnitTypeId() != 0) {
            RoomInfo roomInfo = rentalPostingUpdate.getRoomInfo();
            roomInfo.setUnitType(unitType.get());
            RoomInfo roomInfoAfterSave = roomInfoRepository.save(roomInfo);
        }

        if (rentalPostingUpdate.getRentalPackage().getId() == 3) {
            if (rentalPostingApprovalDto.getStaffRefinementPrice() <= 0)
                throw new ErrMessageException("Staff refinement price must be quarter than 0");
            else {
                rentalPostingUpdate.setStaffRefinementPrice(rentalPostingApprovalDto.getStaffRefinementPrice());
            }
            rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.AwaitingConfirmation));
        }
        if (rentalPostingUpdate.getRentalPackage().getId() == 4) {
            if (rentalPostingApprovalDto.getPriceValuation() <= 0)
                throw new ErrMessageException("Price valuation must be quarter than 0");
            else {
                rentalPostingUpdate.setPriceValuation(rentalPostingApprovalDto.getPriceValuation());
                rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.PendingPricing));
            }
        }
        if (rentalPostingUpdate.getRentalPackage().getId() == 2)
            rentalPostingUpdate.setStatus(String.valueOf(RentalPostingEnum.Processing));
        rentalPostingUpdate.setNote(rentalPostingApprovalDto.getNote());
        rentalPostingUpdate.setIsVerify(true);
        rentalPostingUpdate.getTimeshare().setIsVerify(true);
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPostingUpdate);
        try {
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ duyệt bài đăng cho thuê với mã " + rentalPostingInDb.getId();
            String transactionType = "APPROVAL_RENTALPOSTING";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee, money, paymentMethod, description, transactionType, timeshareCompanyStaff.getTimeshareCompanyId());

        } catch (Exception e) {
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        String title = "Yêu cầu duyệt bài";
        String content = "Xin chào, yêu cầu duyệt bài của bạn đã được duyệt.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(rentalPosting.get().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.RentalPosting))
                .entityId(rentalPosting.get().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = rentalPosting.get().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (rentalPostingUpdate.getRentalPackage().getId()==4){
            String titleSystemStaff = "Yêu cầu duyệt bài đăng cho thuê";
            String contentSystemStaff = "Xin chào, bạn vừa có một yêu cầu duyệt bài đăng đăng cho thuê từ một khách hàng";
            String topic = "systemstaff";
            Notification notificationSystemStaff = Notification.builder()
                    .title(titleSystemStaff)
                    .content(contentSystemStaff)
                    .isRead(false)
                    .type(String.valueOf(NotificationEnum.RentalPosting))
                    .entityId(rentalPosting.get().getId())
                    .role(topic)
                    .build();
            notificationRepository.save(notificationSystemStaff);
            try{
                fcmService.pushNotificationTopic(title,content,topic);
            }catch (Exception e){
                e.printStackTrace();
            }


        }



        return rentalPostingApprovalMapper.toDto(rentalPostingInDb);
    }

    @Override
    public RentalPostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException {
        Optional<RentalPosting> rentalPosting = rentalPostingRepository.findById(postingId);
        if (!rentalPosting.isPresent()) throw new OptionalNotFoundException("Not found posting");
        if (!rentalPosting.get().getStatus().equals(String.valueOf(RentalPostingEnum.PendingApproval)))
            throw new ErrMessageException("Status must be pending approval");
        rentalPosting.get().setNote(note);
        rentalPosting.get().setStatus(String.valueOf(RentalPostingEnum.Reject));
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting.get());
        Optional<Customer> customer = customerRepository.findById(rentalPostingInDb.getOwner().getId());
        if (!customer.isPresent())
            throw new ErrMessageException("Error when refund money to customer but reject successfully");
        float feeCustomer = FeeConfig.fee_reject;
        float moneyCustomer = rentalPostingInDb.getRentalPackage().getPrice() - feeCustomer;
        String paymentMethodCusomer = "WALLET";
        String descriptionCustomer = "Giao dịch hoàn tiền từ chối bài đăng";
        String transactionTypeCustomer = "RENTALPOSTING";
        WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(customer.get().getId(), feeCustomer, moneyCustomer, paymentMethodCusomer, descriptionCustomer, transactionTypeCustomer);

        try {
            TimeShareCompanyStaffDTO timeshareCompanyStaff = timeShareStaffService.getLoginStaff();
            float fee = 0;
            float money = FeeConfig.fee_approval;
            String paymentMethod = "WALLET";
            String description = "Giao dịch cộng tiền từ từ chối bài đăng cho thuê với mã " + rentalPostingInDb.getId();
            String transactionType = "REJECT_RENTALPOSTING";
            WalletTransaction walletTransactionDto = walletService.createTransactionTsCompany(fee, money, paymentMethod, description, transactionType, timeshareCompanyStaff.getTimeshareCompanyId());
        } catch (Exception e) {
            throw new ErrMessageException("Fail to create transaction wallet ts company");
        }
        String title = "Yêu cầu duyệt bài";
        String content = "Xin chào, yêu cầu duyệt bài của bạn đã bị từ chối.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(rentalPosting.get().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.RentalPosting))
                .entityId(rentalPosting.get().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = rentalPosting.get().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rentalPostingApprovalMapper.toDto(rentalPostingInDb);
    }

    @Override
    public RentalPostingApprovalResponseDto approvalPostingSystemStaff(Integer postingId, Float newPriceValuation) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findById(postingId).orElseThrow(() -> new OptionalNotFoundException("Not found posting"));
        if (rentalPosting.getRentalPackage().getId() != 4) throw new ErrMessageException("Package must be 4");
        if (!rentalPosting.getStatus().equals(String.valueOf(RentalPostingEnum.PendingPricing)))
            throw new ErrMessageException("Status must be pending pricing");
        if (newPriceValuation <= 0) throw new ErrMessageException("New price valuation must be quarter than 0");
        rentalPosting.setStatus(String.valueOf(RentalPostingEnum.AwaitingConfirmation));
        rentalPosting.setPriceValuation(newPriceValuation);
        RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto = rentalPostingApprovalMapper.toDto(rentalPostingRepository.save(rentalPosting));
        String title = "Yêu cầu trao quyền sở hữu bài đăng";
        String content = "Xin chào, trao quyền sở hữu bài đăng của bạn đã được duyệt.";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(rentalPosting.getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.RentalPosting))
                .entityId(rentalPosting.getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = rentalPosting.getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rentalPostingApprovalResponseDto;
    }

    @Override
    public RentalPostingResponseDto actionConfirmPosting(Integer postingId, Float newPrice, Boolean isAccepted) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId).orElseThrow(() -> new OptionalNotFoundException("not found posting or posting inactive"));
        if (rentalPosting.getRentalPackage().getId() == 2 || rentalPosting.getRentalPackage().getId() == 1)
            throw new ErrMessageException("package must be type 3 or 4");
        if (!rentalPosting.getStatus().equals(String.valueOf(RentalPostingEnum.AwaitingConfirmation)))
            throw new ErrMessageException("Status must be awaiting confirmation");
        if (rentalPosting.getRentalPackage().getId() == 3) {
            if (newPrice <= 0) throw new ErrMessageException("New price must be quarter than 0");
            rentalPosting.setPricePerNights(newPrice);
            rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Processing));
        } else if (rentalPosting.getRentalPackage().getId() == 4) {
            if (isAccepted) {
                rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Processing));
                rentalPosting.setPricePerNights(rentalPosting.getPriceValuation());
                String title = "Yêu cầu duyệt bài đăng cho thuê";
                String content = "Xin chào, khách hàng đã đồng ý với yêu cầu của hệ thống";
                String topic = "systemstaff";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .type(String.valueOf(NotificationEnum.RentalPosting))
                        .entityId(rentalPosting.getId())
                        .role(topic)
                        .build();
                notificationRepository.save(notification);
                try{
                    fcmService.pushNotificationTopic(title,content,topic);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (!isAccepted) {
                rentalPosting.setStatus(String.valueOf(RentalPostingEnum.RejectPrice));
                Customer customer = userService.getLoginUser().getCustomer();
                if (customer == null)
                    throw new ErrMessageException("Error when refund money to customer but reject successfully");
                float fee = 0;
                float money = rentalPosting.getRentalPackage().getPrice() - 20000;
                String paymentMethod = "WALLET";
                String description = "Giao dịch hoàn tiền từ chối chuyển nhượng quyền sở hữu timeshare gói 4";
                String transactionType = "RENTALPOSTING";
                WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(customer.getId(), fee, money, paymentMethod, description, transactionType);
                rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Processing));
                rentalPosting.setPricePerNights(rentalPosting.getPriceValuation());
                String title = "Yêu cầu duyệt bài đăng cho thuê";
                String content = "Xin chào, khách hàng đã từ chối ý với yêu cầu của hệ thống";
                String topic = "systemstaff";
                Notification notification = Notification.builder()
                        .title(title)
                        .content(content)
                        .isRead(false)
                        .type(String.valueOf(NotificationEnum.RentalPosting))
                        .entityId(rentalPosting.getId())
                        .role(topic)
                        .build();
                notificationRepository.save(notification);
                try{
                    fcmService.pushNotificationTopic(title,content,topic);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting);
        return rentalPostingResponseMapper.toDto(rentalPostingInDb);
    }

    @Override
    public PostingDetailTsStaffResponseDTO getRentalPostingDetailTsStaffById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(rentalPosting.getId(), DocumentStoreEnum.RentalPosting.toString());
        PostingDetailTsStaffResponseDTO responseDTO = postingDetailTsStaffMapper.entityToDto(rentalPosting);
        responseDTO.setImageUrls(imageUrls);
        return responseDTO;
    }

    @Override
    public Page<PostingResponseDTO> getAllPostingsByResortId(Integer resortId, Pageable pageable) {
        Specification<RentalPosting> spec = Specification.where(RentalPostingSpecification.isActive(true));

        if (resortId != null) {
            spec = spec.and(RentalPostingSpecification.hasResortId(resortId));
        }
        spec = spec.and(RentalPostingSpecification.hasStatus(String.valueOf(RentalPostingEnum.Processing)));
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        }

        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAll(spec, pageable);
        return rentalPostings.map(listRentalPostingMapper::entityToDto);
    }

    @Transactional
    @Override
    public RentalPostingResponseDto updateRentalPosting(Integer postingId, UpdateRentalPostingDto updateRentalPostingDto)
            throws  ErrMessageException {
        Optional<CancellationPolicy> cancellationPolicy = cancellationPolicyRepository.findById(updateRentalPostingDto.getCancellationTypeId());
        if (!cancellationPolicy.isPresent()) throw new ErrMessageException("Not found policy");

        RentalPosting rental = rentalPostingRepository.findById(postingId)
                .orElseThrow(() -> new ErrMessageException("posting with ID " + postingId + " does not exist"));

        if (rental.getRentalPackage().getId() == 4){
            throw new ErrMessageException("you dont have permission in package 4");
        }
        if (!rental.getStatus().equals(String.valueOf(RentalPostingEnum.Processing))){
            throw new ErrMessageException("Status must be Processing");
        }
        rental.setCancellationType(cancellationPolicy.get());
        rental.setDescription(updateRentalPostingDto.getDescription());
        rental.setPricePerNights(updateRentalPostingDto.getPricePerNights());
        rental.setIsActive(true);

        RentalPosting updatedRentalPosting = rentalPostingRepository.save(rental);
        documentStoreRepository.deactivateOldImages(updatedRentalPosting.getId(), DocumentStoreEnum.RentalPosting.toString());

        try {
            for (String imageUrl : updateRentalPostingDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.RentalPosting.toString());
                document.setEntityId(updatedRentalPosting.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        return rentalPostingResponseMapper.toDto(updatedRentalPosting);
    }
}
