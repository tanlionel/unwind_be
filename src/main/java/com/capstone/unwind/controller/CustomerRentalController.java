package com.capstone.unwind.controller;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.ExchangeBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingRequestDto;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerRentalController {
    @Autowired
    RentalPostingService rentalPostingService;
    @Autowired
    BookingService bookingService;
    @GetMapping("rental/posting")
    public ResponseEntity<Page<PostingResponseDTO>> getAllActivePostings(
            @RequestParam(required = false) Integer resortId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status)throws OptionalNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostingResponseDTO> postings = rentalPostingService.getAllPostings(resortId,pageable,status);
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailById(postingId);
    }
    @PutMapping("/deactivate/{postingId}")
    public ResponseEntity<PostingDetailResponseDTO> deActiveRentalPosting(
            @PathVariable Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        PostingDetailResponseDTO postingDetailResponseDTO = rentalPostingService.deActiveRentalPosting(postingId);
        return ResponseEntity.ok(postingDetailResponseDTO);
    }
    @PostMapping("rental/posting")
    public ResponseEntity<RentalPostingResponseDto> createPosting(@RequestBody RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingResponseDto rentalPostingResponseDto = rentalPostingService.createRentalPosting(rentalPostingRequestDto);
        return ResponseEntity.ok(rentalPostingResponseDto);
    }
    @PutMapping("rental/{postingId}")
    public ResponseEntity<RentalPostingResponseDto> updateRentalPosting(
            @PathVariable Integer postingId,
            @RequestBody  UpdateRentalPostingDto updateRentalPostingDto) throws ErrMessageException {
        RentalPostingResponseDto response = rentalPostingService.updateRentalPosting(postingId, updateRentalPostingDto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("rental/posting/confirmation/{postingId}")
    public ResponseEntity<RentalPostingResponseDto> actionConfirmationCustomerPosting(@PathVariable Integer postingId,@RequestParam(required = false) Float newPrice,@RequestParam(required = false) Boolean isAccepted) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingResponseDto rentalPostingResponseDto =rentalPostingService.actionConfirmPosting(postingId,newPrice,isAccepted);
        return ResponseEntity.ok(rentalPostingResponseDto);
    }
    @PostMapping("rental/booking/{postingId}")
    public ResponseEntity<RentalBookingDetailDto> bookingRentalPosting(@PathVariable Integer postingId, @RequestBody RentalBookingRequestDto rentalBookingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        RentalBookingDetailDto rentalBookingDetailDto = bookingService.createBookingRentalPosting(postingId,rentalBookingRequestDto);
        return ResponseEntity.ok(rentalBookingDetailDto);
    }
    @GetMapping("rental/booking/{bookingId}")
    public ResponseEntity<RentalBookingDetailDto> getRentalBookingDetailById(@PathVariable Integer bookingId) throws OptionalNotFoundException {
        RentalBookingDetailDto rentalBookingDetailDto = bookingService.getRentalBookingDetailById(bookingId);
        return ResponseEntity.ok(rentalBookingDetailDto);
    }

    @GetMapping("booking")
    public Page<MergedBooking> getAllBookingPagination( @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        Page<MergedBooking> mergedBookingPage = bookingService.getPaginationBookingCustomer(page,size);
        return mergedBookingPage;
    }
    @PostMapping("rental/booking/cancel/{bookingId}")
    public ResponseEntity<RentalBookingDto> cancelBooking(@PathVariable Integer bookingId) throws ErrMessageException, OptionalNotFoundException {
        RentalBookingDto rentalBookingDetailDto = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(rentalBookingDetailDto);
    }
    @PostMapping("rental/booking/form/{postingId}")
    public ResponseEntity<Boolean> createContactForm(@PathVariable Integer postingId, @RequestBody RentalPackageBasicRequestDto rentalPackageBasicRequestDto) throws ErrMessageException, OptionalNotFoundException {
        Boolean result = bookingService.createContactForm(postingId,rentalPackageBasicRequestDto);
        return ResponseEntity.ok(result);
    }
}
