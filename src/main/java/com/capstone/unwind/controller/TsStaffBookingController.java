package com.capstone.unwind.controller;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.BookingTsStaffRequestDto;
import com.capstone.unwind.model.BookingDTO.ExchangeBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/timeshare-staff")
@RequiredArgsConstructor
@CrossOrigin
public class TsStaffBookingController {
    @Autowired
    private final BookingService bookingService;
    @GetMapping("booking")
    public Page<MergedBooking> getMergeBookingByDate(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate searchDate,
                                                     @RequestParam(required = false,defaultValue = "false") boolean isComing,
                                                     @RequestParam(required = false,defaultValue = "false") boolean willGo){
        if (searchDate==null) searchDate=LocalDate.now();
        Page<MergedBooking> mergedBookingPage = bookingService.getMergeBookingByDateTsStaff(pageNo,pageSize,searchDate,isComing,willGo);
        return mergedBookingPage;
    }
    @GetMapping("booking/rental/{bookingId}")
    public ResponseEntity<RentalBookingDetailDto> getRentalBookingDetail(@PathVariable Integer bookingId) throws OptionalNotFoundException {
        RentalBookingDetailDto rentalBookingDetailDto = bookingService.getRentalBookingDetailById(bookingId);
        return ResponseEntity.ok(rentalBookingDetailDto);
    }
    @GetMapping("booking/exchange/{bookingId}")
    public ResponseEntity<ExchangeBookingDetailDto> getExchangeBookingDetail(@PathVariable Integer bookingId) throws OptionalNotFoundException {
        ExchangeBookingDetailDto exchangeBookingDetailDto = bookingService.getExchangeBookingDetailById(bookingId);
        return ResponseEntity.ok(exchangeBookingDetailDto);
    }
    @PostMapping("booking/rental/{bookingId}")
    public ResponseEntity<RentalBookingDetailDto> updateRentalBooking(@PathVariable Integer bookingId,
                                                                      @RequestBody BookingTsStaffRequestDto bookingTsStaffRequestDto) throws ErrMessageException, OptionalNotFoundException {
        RentalBookingDetailDto rentalBookingDetailDto = bookingService.updateRentalBooking(bookingId,bookingTsStaffRequestDto);
        return ResponseEntity.ok(rentalBookingDetailDto);
    }

    @PostMapping("booking/exchange/{bookingId}")
    public ResponseEntity<ExchangeBookingDetailDto> updateExchangeBooking(@PathVariable Integer bookingId,
                                                                          @RequestBody BookingTsStaffRequestDto bookingTsStaffRequestDto) throws ErrMessageException, OptionalNotFoundException {
        ExchangeBookingDetailDto exchangeBookingDetailDto = bookingService.updateExchangeBooking(bookingId,bookingTsStaffRequestDto);
        return ResponseEntity.ok(exchangeBookingDetailDto);
    }
}
