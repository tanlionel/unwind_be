package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BookingDTO.RentalBookingDetailDto;
import com.capstone.unwind.model.BookingDTO.RentalBookingRequestDto;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingRequestDto;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingResponseDto;
import com.capstone.unwind.model.ExchangePostingDTO.PostingExchangeDetailResponseDTO;
import com.capstone.unwind.model.ExchangePostingDTO.PostingExchangeResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import com.capstone.unwind.model.PostingDTO.RentalPostingRequestDto;
import com.capstone.unwind.model.PostingDTO.RentalPostingResponseDto;
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

    @GetMapping("exchange/posting")
    public ResponseEntity<Page<PostingExchangeResponseDTO>> getAllActivePostings(
            @RequestParam(required = false) Integer resortId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostingExchangeResponseDTO> postings = exchangePostingService.getAllPostings(resortId,pageable);
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

}
