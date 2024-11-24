package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.enums.RentalBookingEnum;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.*;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.PostingDTO.RentalPackageBasicRequestDto;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        if (isComing == true && willGo == false){
            mergedBookingPage = mergedBookingRepository.findAllByCheckinDateAfterAndStatusAndResortId(searchDate,String.valueOf(RentalBookingEnum.Booked),resortId,pageable);
        }else if (isComing==false && willGo==true){
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
            }else throw new ErrMessageException("Status must be checkin");
        }else if (!isCheckIn && !isCheckOut){
            throw new ErrMessageException("must be checkin or checkout");
        }else if (isCheckOut && isCheckIn){
            throw new ErrMessageException("not be check in and check out in the same time");
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
        ExchangeBookingDetailDto exchangeBookingDetailDto = exchangeBookingDetailMapper.toDto(exchangeBookingRepository.save(exchangeBooking));
        return exchangeBookingDetailDto;
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
            feeToOwner = -feeToOwner;
        }
        Float feeToCustomer = refundToOwner;
        if (feeToOwner > 0) {
            feeToCustomer= -feeToCustomer;
        }
        refundToCustomer = Math.max(refundToCustomer, 0);
        refundToOwner = Math.max(refundToOwner, 0);
        walletService.refundMoneyToCustomer(booking.getRenter().getId(), feeToCustomer, refundToCustomer,
                "WALLET", "Hoàn tiền khi hủy đặt phòng ", "RENTALREFUND");
        walletService.refundMoneyToCustomer(booking.getRentalPosting().getOwner().getId(), feeToOwner, refundToOwner,
                "WALLET", "Hoàn tiền khi hủy đặt phòng", "RENTALREFUND");

        RentalPosting posting = booking.getRentalPosting();
        posting.setStatus("Processing");
        rentalPostingRepository.save(posting);

        booking.setStatus("Cancelled");
        booking.setIsActive(false);
        RentalBooking rentalBookingInDb = rentalBookingRepository.save(booking);
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject(REJECT_RENTAL_BOOKING_SUBJECT);
            emailRequestDto.setContent(REJECT_RENTAL_BOOKING_CONTENT);
            sendinblueService.sendEmailWithTemplate(
                    booking.getRentalPosting().getOwner().getUser().getEmail(),
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
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
