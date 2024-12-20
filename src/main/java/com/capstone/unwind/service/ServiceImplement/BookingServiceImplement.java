package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.config.FeeConfig;
import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.*;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.*;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingResponseDto;
import com.capstone.unwind.model.ExchangePostingDTO.UpdateExchangePostingDto;
import com.capstone.unwind.model.PostingDTO.RentalPackageBasicRequestDto;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.capstone.unwind.config.EmailMessageConfig.*;
import static org.springframework.data.domain.Sort.sort;

@Service
@RequiredArgsConstructor
public class BookingServiceImplement implements BookingService {
    @Autowired
    private final RentalBookingRepository rentalBookingRepository;
    @Autowired
    private final RentalPostingRepository rentalPostingRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final RentalBookingDetailMapper rentalBookingDetailMapper;
    @Autowired
    private final MergedBookingRepository mergedBookingRepository;
    @Autowired
    private final TimeShareStaffService timeShareStaffService;
    @Autowired
    private final ExchangeBookingRepository exchangeBookingRepository;
    @Autowired
    private final ExchangeBookingDetailMapper exchangeBookingDetailMapper;
    @Autowired
    private final WalletService walletService;
    @Autowired
    private final RentalBookingMapper rentalBookingMapper;
    @Autowired
    private final SendinblueService sendinblueService;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    final ExchangeBookingMapper exchangeBookingMapper;
    @Autowired
    private final FcmService fcmService;
    @Autowired
    private final NotificationRepository notificationRepository;

    @Override
    public RentalBookingDetailDto createBookingRentalPosting(Integer postingId, RentalBookingRequestDto rentalBookingRequestDto) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId).orElseThrow(()-> new OptionalNotFoundException("Not found posting id or inactive"));
        if (!rentalPosting.getStatus().equals(String.valueOf(RentalPostingEnum.Processing))) throw new ErrMessageException("Status must be processing");
        rentalPosting.setStatus(String.valueOf(RentalPostingEnum.Completed));
        rentalPosting.setIsBookable(true);
        RentalPosting rentalPostingInDb = rentalPostingRepository.save(rentalPosting);
        float serviceFee = 0;
        Customer customer = userService.getLoginUser().getCustomer();
        if (rentalPosting.getOwner().getId()==customer.getId()) throw new ErrMessageException("Can not booking posting of yourself");
        RentalBooking rentalBooking = RentalBooking.builder()
                .rentalPosting(rentalPostingInDb)
                .status(String.valueOf(RentalBookingEnum.Booked))
                .checkinDate(rentalPostingInDb.getCheckinDate())
                .checkoutDate(rentalPostingInDb.getCheckoutDate())
                .primaryGuestName(rentalBookingRequestDto.getPrimaryGuestName())
                .primaryGuestPhone(rentalBookingRequestDto.getPrimaryGuestPhone())
                .primaryGuestEmail(rentalBookingRequestDto.getPrimaryGuestEmail())
                .isActive(true)
                .serviceFee(serviceFee)
                .totalPrice(rentalPostingInDb.getPricePerNights()*rentalPostingInDb.getNights()+serviceFee)
                .totalNights(rentalPostingInDb.getNights())
                .pricePerNights(rentalPostingInDb.getPricePerNights())
                .isFeedback(false)
                .renter(customer)
                .renterFullLegalName(customer.getFullName())
                .renterLegalPhone(customer.getPhone())
                .renterLegalAvatar(customer.getAvatar())
                .build();
        RentalBooking rentalBookingInDb = rentalBookingRepository.save(rentalBooking);
        rentalPostingRepository.closeAllRentalPostingsByOwnerInYear(rentalBookingInDb.getRentalPosting().getOwner().getId(),rentalBookingInDb.getCheckinDate().getYear());

        //send notification
        String title = "Bài đăng cho thuê được thuê";
        String content = "Bài đăng cho thuê của bạn vừa mới được thuê từ một vị khách";
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
        return rentalBookingDetailMapper.toDto(rentalBookingInDb);
    }

    @Override
    public RentalBookingDetailDto getRentalBookingDetailById(Integer bookingId) throws OptionalNotFoundException {
        RentalBooking rentalBooking = rentalBookingRepository.findById(bookingId).orElseThrow(()-> new OptionalNotFoundException("Not found booking"));
        RentalBookingDetailDto rentalBookingDetailDto = rentalBookingDetailMapper.toDto(rentalBooking);
        rentalBookingDetailDto.setSource("rental");
        return rentalBookingDetailDto;
    }

    @Override
    public Page<MergedBooking> getPaginationBookingCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("checkinDate").ascending());
        Customer customer = userService.getLoginUser().getCustomer();
        Page<MergedBooking> mergedBookingPage = mergedBookingRepository.findAllByRenterId(customer.getId(),pageable);
        return mergedBookingPage;
    }

    @Override
    public Page<MergedBooking> getMergeBookingByDateTsStaff(Integer pageNo, Integer pageSize, LocalDate searchDate, boolean isComing, boolean willGo) {
        Pageable pageable = PageRequest.of(pageNo,pageSize,Sort.by("checkinDate").ascending());
        Page<MergedBooking> mergedBookingPage;
        TimeShareCompanyStaffDTO timeShareCompanyStaffDTO = timeShareStaffService.getLoginStaff();
        Integer resortId = timeShareCompanyStaffDTO.getResortId();
        if (isComing && !willGo){
            mergedBookingPage = mergedBookingRepository.findAllByCheckinDateAfterAndStatusAndResortId(searchDate,String.valueOf(RentalBookingEnum.Booked),resortId,pageable);
        }else if (!isComing && willGo){
            mergedBookingPage = mergedBookingRepository.findAllByCheckinDateBeforeAndCheckoutDateAfterAndStatusAndResortId(searchDate,searchDate,String.valueOf(RentalBookingEnum.CheckIn),resortId,pageable);
        }else {
            mergedBookingPage = mergedBookingRepository.findAllByCheckinDateOrCheckoutDateAndResortId(searchDate,searchDate,resortId,pageable);
        }
        return mergedBookingPage;
    }

    @Override
    public ExchangeBookingDetailDto getExchangeBookingDetailById(Integer bookingId) throws OptionalNotFoundException {
        ExchangeBooking exchangeBooking = exchangeBookingRepository.findById(bookingId).orElseThrow(()-> new OptionalNotFoundException("Not found booking"));
        ExchangeBookingDetailDto exchangeBookingDetailDto = exchangeBookingDetailMapper.toDto(exchangeBooking);
        exchangeBookingDetailDto.setSource("exchange");
        return exchangeBookingDetailDto;
    }

    @Override
    public RentalBookingDetailDto updateRentalBooking(Integer bookingId, BookingTsStaffRequestDto bookingTsStaffRequestDto) throws OptionalNotFoundException, ErrMessageException {
        boolean isCheckIn = bookingTsStaffRequestDto.isCheckIn();
        boolean isCheckOut = bookingTsStaffRequestDto.isCheckOut();
        RentalBooking rentalBooking = rentalBookingRepository.findById(bookingId).orElseThrow(()-> new OptionalNotFoundException("Not found booking"));

        if (isCheckIn && !isCheckOut){
            if (rentalBooking.getStatus().equals(String.valueOf(RentalBookingEnum.Booked))){
                rentalBooking.setStatus(String.valueOf(RentalBookingEnum.CheckIn));
            }else throw new ErrMessageException("Status must be booked");
        }else if (isCheckOut && !isCheckIn){
            if (rentalBooking.getStatus().equals(String.valueOf(RentalBookingEnum.CheckIn))){
                rentalBooking.setStatus(String.valueOf(RentalBookingEnum.CheckOut));
                //refund money to customer
                RentalPosting rentalPosting = rentalBooking.getRentalPosting();
                if (rentalPosting.getRentalPackage().getId() == 4){
                    float fee = rentalPosting.getPricePerNights()*rentalPosting.getNights()*rentalPosting.getRentalPackage().getCommissionRate()/100;
                    float money = rentalPosting.getPricePerNights()*rentalPosting.getNights()-fee;
                    String paymentMethod = "WALLET";
                    String description = "Giao dịch cộng tiền từ khách hàng đã check out";
                    String transactionType = String.valueOf(WalletTransactionEnum.RENTALPACKAGE04);
                    WalletTransaction walletTransaction = walletService.createTransactionSystemPosting(fee,money,paymentMethod,description,transactionType);

                    Customer owner = rentalPosting.getOwner();
                    if (owner==null) throw new ErrMessageException("Error when refund money to customer but reject successfully");
                    float feePackageFour = rentalPosting.getPricePerNights()*rentalPosting.getNights() * rentalPosting.getRentalPackage().getCommissionRate() / 100 ;
                    float moneyCustomer = rentalPosting.getPricePerNights()*rentalPosting.getNights() - feePackageFour;
                    String paymentMethodCustomer = "WALLET";
                    String descriptionCustomer = "Giao dịch cộng tiền từ khách hàng đã check out";
                    String transactionTypeCustomer = "RENTALPOSTING";
                    WalletTransaction walletTransactionCustomerPackage04 = walletService.refundMoneyToCustomer(owner.getId(),feePackageFour,moneyCustomer,paymentMethodCustomer,descriptionCustomer,transactionTypeCustomer);
                }else {
                    Customer owner = rentalPosting.getOwner();
                    if (owner==null) throw new ErrMessageException("Error when refund money to customer but reject successfully");
                    float feeCustomer = rentalPosting.getPricePerNights()*rentalPosting.getNights()*rentalPosting.getRentalPackage().getCommissionRate()/100;
                    float moneyCustomer = rentalPosting.getPricePerNights()*rentalPosting.getNights()-feeCustomer;
                    String paymentMethodCustomer = "WALLET";
                    String descriptionCustomer = "Giao dịch cộng tiền từ khách hàng đã check out";
                    String transactionTypeCustomer = "RENTALPOSTING";
                    WalletTransaction walletTransaction = walletService.refundMoneyToCustomer(owner.getId(),feeCustomer,moneyCustomer,paymentMethodCustomer,descriptionCustomer,transactionTypeCustomer);
                }

            }else throw new ErrMessageException("Status must be checkin");
        }else if (!isCheckIn && !isCheckOut){
            throw new ErrMessageException("must be checkin or checkout");
        }else if (isCheckOut && isCheckIn){
            throw new ErrMessageException("not be check in and check out in the same time");
        }

        String title = "Thay đổi trạng thái đặt phòng";
        String content = "Xin chào, trạng thái đặt phòng của bạn đã được thay đổi bởi nhân viên tại " + rentalBooking.getRentalPosting().getRoomInfo().getResort().getResortName();
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(rentalBooking.getRenter().getUser().getId())
                .type(String.valueOf(NotificationEnum.RentalBooking))
                .entityId(rentalBooking.getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = rentalBooking.getRenter().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        RentalBookingDetailDto rentalBookingDetailDto = rentalBookingDetailMapper.toDto(rentalBookingRepository.save(rentalBooking));
        return rentalBookingDetailDto;
    }

    @Override
    public ExchangeBookingDetailDto updateExchangeBooking(Integer bookingId, BookingTsStaffRequestDto bookingTsStaffRequestDto) throws OptionalNotFoundException, ErrMessageException {
        boolean isCheckIn = bookingTsStaffRequestDto.isCheckIn();
        boolean isCheckOut = bookingTsStaffRequestDto.isCheckOut();
        ExchangeBooking exchangeBooking = exchangeBookingRepository.findById(bookingId).orElseThrow(()-> new OptionalNotFoundException("Not found booking"));
        if (isCheckIn){
            if (exchangeBooking.getStatus().equals(String.valueOf(RentalBookingEnum.Booked))){
                exchangeBooking.setStatus(String.valueOf(RentalBookingEnum.CheckIn));
            }else throw new ErrMessageException("Status must be booked");
        }else if (isCheckOut){
            if (exchangeBooking.getStatus().equals(String.valueOf(RentalBookingEnum.CheckIn))){
                exchangeBooking.setStatus(String.valueOf(RentalBookingEnum.CheckOut));
            }else throw new ErrMessageException("Status must be checkin");
        } else if (!isCheckIn && !isCheckOut){
            throw new ErrMessageException("must be checkin or checkout");
        }else if (isCheckOut && isCheckIn){
            throw new ErrMessageException("not be check in and check out in the same time");
        }
        String title = "Thay đổi trạng thái đặt phòng";
        String content = "Xin chào, trạng thái đặt phòng của bạn đã được thay đổi bởi nhân viên tại " + exchangeBooking.getExchangePosting().getRoomInfo().getResort().getResortName();
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(exchangeBooking.getRenter().getUser().getId())
                .type(String.valueOf(NotificationEnum.ExchangeBooking))
                .entityId(exchangeBooking.getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = exchangeBooking.getRenter().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        ExchangeBookingDetailDto exchangeBookingDetailDto = exchangeBookingDetailMapper.toDto(exchangeBookingRepository.save(exchangeBooking));
        return exchangeBookingDetailDto;
    }
    @Transactional
    @Override
    public ExchangeBookingDto updateExchangeBookingGuest(Integer bookingId, UpdateExchangeBookingDto updateExchangeBookingDto)
            throws ErrMessageException {
        ExchangeBooking exchange = exchangeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ErrMessageException("Booking with ID " + bookingId + " does not exist"));

        if (exchange.getIsPrimaryGuest()) {
            throw new ErrMessageException("The booking has already been updated.");
        }
        exchange.setPrimaryGuestEmail(updateExchangeBookingDto.getPrimaryGuestEmail());
        exchange.setPrimaryGuestName(updateExchangeBookingDto.getPrimaryGuestName());
        exchange.setPrimaryGuestPhone(updateExchangeBookingDto.getPrimaryGuestPhone());
        exchange.setPrimaryGuestCountry(updateExchangeBookingDto.getPrimaryGuestCountry());
        exchange.setPrimaryGuestCity(updateExchangeBookingDto.getPrimaryGuestCity());
        exchange.setPrimaryGuestState(updateExchangeBookingDto.getPrimaryGuestState());
        exchange.setPrimaryGuestStreet(updateExchangeBookingDto.getPrimaryGuestStreet());
        exchange.setPrimaryGuestPostalCode(updateExchangeBookingDto.getPrimaryGuestPostalCode());
        exchange.setIsPrimaryGuest(true);
        exchange.setIsActive(true);
        ExchangeBooking updatedExchangeBooking = exchangeBookingRepository.save(exchange);

        return exchangeBookingMapper.toDto(updatedExchangeBooking);
    }

    @Override
    public RentalBookingDto cancelBooking(Integer bookingId) throws OptionalNotFoundException, ErrMessageException {
        RentalBooking booking = rentalBookingRepository.findById(bookingId)
                .orElseThrow(() -> new OptionalNotFoundException("Booking does not exist"));

        LocalDate cancelledDate = LocalDate.now();
        CancellationPolicy policy = booking.getRentalPosting().getCancellationType();
        LocalDate checkinDate = booking.getCheckinDate();
        if (cancelledDate.isAfter(checkinDate.minusDays(policy.getDurationBefore()))) {
            throw new ErrMessageException("Cannot cancel because refund deadline has passed");
        }
        Float totalPrice = booking.getTotalPrice();
        Float refundRate = policy.getRefundRate() / 100.0f;
        Float refundToCustomer = totalPrice * refundRate;
        Float refundToOwner = totalPrice - refundToCustomer;
        Float feeToOwner = refundToCustomer;
        if (feeToOwner > 0) {
            feeToOwner = feeToOwner;
        }
        Float feeToCustomer = refundToOwner;
        if (feeToOwner > 0) {
            feeToCustomer= feeToCustomer;
        }
        refundToCustomer = Math.max(refundToCustomer, 0);
        refundToOwner = Math.max(refundToOwner, 0);
        walletService.refundMoneyToCustomer(booking.getRenter().getId(), 0, refundToCustomer,
                "WALLET", "Hoàn tiền khi hủy đặt phòng ", "RENTALREFUND");
        walletService.refundMoneyToCustomer(booking.getRentalPosting().getOwner().getId(), 0, refundToOwner,
                "WALLET", "Hoàn tiền khi hủy đặt phòng", "RENTALREFUND");

        RentalPosting posting = booking.getRentalPosting();
        posting.setStatus("Processing");
        posting.setIsBookable(false);
        rentalPostingRepository.save(posting);

        booking.setStatus(String.valueOf(RentalBookingEnum.Cancelled));
        RentalBooking rentalBookingInDb = rentalBookingRepository.save(booking);

        String title = "Người thuê hủy đặt phòng";
        String content = "Bài đăng cho thuê của bạn vừa mới được hủy từ một vị khách";
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .isRead(false)
                .userId(booking.getRentalPosting().getOwner().getUser().getId())
                .type(String.valueOf(NotificationEnum.RentalPosting))
                .entityId(booking.getRentalPosting().getId())
                .build();
        notificationRepository.save(notification);
        String fcmToken = booking.getRentalPosting().getOwner().getUser().getFcmToken();
        try{
            fcmService.pushNotification(fcmToken,title,content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rentalBookingMapper.toDto(rentalBookingInDb);
    }

    @Override
    public Boolean createContactForm(Integer postingId, RentalPackageBasicRequestDto rentalPackageBasicRequestDto) throws ErrMessageException, OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId).orElseThrow(()->new OptionalNotFoundException("Not found posting"));
        Customer customer = rentalPosting.getOwner();
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject(FORM_CONTACT_RENTAL_PACKAGE_01_SUBJECT);
            String content = FORM_CONTACT_RENTAL_PACKAGE_01_CONTENT + "Tên người yêu cầu: "+rentalPackageBasicRequestDto.getFullName().trim()+" Liên lạc: " + rentalPackageBasicRequestDto.getPhone().trim()+" .Nội dung: "+rentalPackageBasicRequestDto.getNote().trim();
            emailRequestDto.setContent(content);
            sendinblueService.sendEmailWithTemplate(
                    customer.getUser().getEmail(),
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return true;
    }

}
