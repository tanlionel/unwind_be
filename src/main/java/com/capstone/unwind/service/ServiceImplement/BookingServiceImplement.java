package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.entity.RentalBooking;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.enums.RentalBookingEnum;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailMapper;
import com.capstone.unwind.model.BookingDTO.RentalBookingRequestDto;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.repository.MergedBookingRepository;
import com.capstone.unwind.repository.RentalBookingRepository;
import com.capstone.unwind.repository.RentalPostingRepository;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        if (!rentalBooking.getIsActive()) throw new OptionalNotFoundException("Inactive booking");
        return rentalBookingDetailMapper.toDto(rentalBooking);
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

}
