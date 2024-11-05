package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingRequestDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface BookingService {
    RentalBookingDetailDto createBookingRentalPosting(Integer postingId, RentalBookingRequestDto rentalBookingRequestDto) throws OptionalNotFoundException, ErrMessageException;

    RentalBookingDetailDto getRentalBookingDetailById(Integer bookingId) throws OptionalNotFoundException;

    Page<MergedBooking> getPaginationBookingCustomer(int page, int size);

    Page<MergedBooking> getMergeBookingByDateTsStaff(Integer pageNo, Integer pageSize, LocalDate searchDate, boolean isComing, boolean willGo);
}
