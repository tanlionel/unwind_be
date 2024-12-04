package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.*;
import com.capstone.unwind.model.ExchangePostingDTO.*;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.ExchangePostingService;
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
public class CustomerExchangeController {
    @Autowired
    ExchangePostingService exchangePostingService;
    @Autowired
    BookingService bookingService;


    @GetMapping("exchange/posting")
    public ResponseEntity<Page<PostingExchangeResponseDTO>> getAllActivePostings(
            @RequestParam(required = false) Integer resortId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) throws OptionalNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostingExchangeResponseDTO> postings = exchangePostingService.getAllPostings(resortId,pageable,status);
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }
    @GetMapping("exchange/posting/{postingId}")
    public PostingExchangeDetailResponseDTO getExchangePostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return exchangePostingService.getExchangePostingDetailById(postingId);
    }
    @PostMapping("exchange/posting")
    public ResponseEntity<ExchangePostingResponseDto> createPosting(@RequestBody ExchangePostingRequestDto exchangePostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        ExchangePostingResponseDto exchangePostingResponseDto = exchangePostingService.createExchangePosting(exchangePostingRequestDto);
        return ResponseEntity.ok(exchangePostingResponseDto);
    }
    @PutMapping("exchange/{postingId}")
    public ResponseEntity<ExchangePostingResponseDto> updateExchangePosting(
            @PathVariable Integer postingId,
            @RequestBody UpdateExchangePostingDto updateExchangePostingDto) throws ErrMessageException {
        ExchangePostingResponseDto response = exchangePostingService.updateExchangePosting(postingId, updateExchangePostingDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/deactivate/exchange/{postingId}")
    public ResponseEntity<PostingExchangeDetailResponseDTO> deActiveRentalPosting(
            @PathVariable Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        PostingExchangeDetailResponseDTO postingDetailResponseDTO = exchangePostingService.deActiveExchangePostingPosting(postingId);
        return ResponseEntity.ok(postingDetailResponseDTO);
    }
    @PostMapping("exchange/request/{postingId}")
    public ResponseEntity<ExchangeRequestDetailDto> createRequestExchange(@PathVariable Integer postingId,@RequestBody ExchangeRequestDto exchangeRequestDto) throws ErrMessageException, OptionalNotFoundException {
        ExchangeRequestDetailDto exchangeRequestDetailDto = exchangePostingService.createRequestExchange(postingId,exchangeRequestDto);
        return ResponseEntity.ok(exchangeRequestDetailDto);
    }

    @GetMapping("exchange/request/{requestId}")
    public ResponseEntity<ExchangeRequestDetailDto> getExchangeRequestById(@PathVariable Integer requestId) throws OptionalNotFoundException {
        ExchangeRequestDetailDto exchangeRequestDetailDto = exchangePostingService.getExchangeRequestById(requestId);
        return ResponseEntity.ok(exchangeRequestDetailDto);
    }
    @GetMapping("exchange/request")
    public Page<ExchangeRequestBasicDto> getExchangeRequestPagination(
                                                                      @RequestParam(required = false,defaultValue = "0") int pageNo,
                                                                      @RequestParam(required = false,defaultValue = "10") int pageSize) throws ErrMessageException {
        Page<ExchangeRequestBasicDto> exchangeRequestBasicDtos = exchangePostingService.getPaginationExchangeRequest(pageNo,pageSize);
        return exchangeRequestBasicDtos;
    }
    @GetMapping("exchange/request/posting/{postingId}")
    public  Page<ExchangeRequestPostingBasicDto> getExchangeRequestListByPostingIdPagination(@RequestParam(required = false,defaultValue = "0") int pageNo,
                                                                                             @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                                                             @PathVariable int postingId){
        Page<ExchangeRequestPostingBasicDto> exchangeRequestPostingBasicDtos = exchangePostingService.getPaginationExchangeRequestByPostingId(pageNo,pageSize,postingId);
        return exchangeRequestPostingBasicDtos;
    }
    @PostMapping("exchange/request/approval/{requestId}")
    public ResponseEntity<ExchangeRequestBasicDto> approvalRequest(@PathVariable Integer requestId) throws ErrMessageException, OptionalNotFoundException {
        ExchangeRequestBasicDto exchangeRequestApprovalResponseDto = exchangePostingService.approvalRequestCustomer(requestId);
        return ResponseEntity.ok(exchangeRequestApprovalResponseDto);
    }
    @PutMapping("exchange/booking/primary-guest/{bookingId}")
    public ResponseEntity<ExchangeBookingDto> updateExchangeBookingGuest(
            @PathVariable Integer bookingId,
            @RequestBody UpdateExchangeBookingDto updateExchangeBookingDto) throws ErrMessageException {

            ExchangeBookingDto updatedBooking = bookingService.updateExchangeBookingGuest(bookingId, updateExchangeBookingDto);
            return ResponseEntity.ok(updatedBooking);

    }
    @GetMapping("exchange/booking/{bookingId}")
    public ResponseEntity<ExchangeBookingDetailDto> getExchangeBookingDetailById(@PathVariable Integer bookingId) throws OptionalNotFoundException {
        ExchangeBookingDetailDto exchangeBookingDetailDto = bookingService.getExchangeBookingDetailById(bookingId);
        return ResponseEntity.ok(exchangeBookingDetailDto);
    }

}
