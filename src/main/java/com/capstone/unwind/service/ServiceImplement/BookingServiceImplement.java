package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.RentalBooking;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.enums.RentalBookingEnum;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailMapper;
import com.capstone.unwind.model.BookingDTO.RentalBookingRequestDto;
import com.capstone.unwind.repository.RentalBookingRepository;
import com.capstone.unwind.repository.RentalPostingRepository;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
