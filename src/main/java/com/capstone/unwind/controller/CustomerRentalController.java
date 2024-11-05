package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import com.capstone.unwind.model.PostingDTO.RentalPostingRequestDto;
import com.capstone.unwind.model.PostingDTO.RentalPostingResponseDto;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import com.capstone.unwind.service.ServiceInterface.TimeShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerRentalController {
    @Autowired
    RentalPostingService rentalPostingService;
    @GetMapping("rental/posting")
    public ResponseEntity<Page<PostingResponseDTO>> getAllActivePostings(
            @RequestParam(required = false) Integer resortId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostingResponseDTO> postings = rentalPostingService.getAllPostings(resortId,pageable);
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailById(postingId);
    }
    @PostMapping("rental/posting")
    public ResponseEntity<RentalPostingResponseDto> createPosting(@RequestBody RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingResponseDto rentalPostingResponseDto = rentalPostingService.createRentalPosting(rentalPostingRequestDto);
        return ResponseEntity.ok(rentalPostingResponseDto);
    }
    @PostMapping("rental/posting/confirmation/{postingId}")
    public ResponseEntity<RentalPostingResponseDto> actionConfirmationCustomerPosting(@PathVariable Integer postingId,@RequestParam(required = false) Float newPrice,@RequestParam(required = false) Boolean isAccepted) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingResponseDto rentalPostingResponseDto =rentalPostingService.actionConfirmPosting(postingId,newPrice,isAccepted);
        return ResponseEntity.ok(rentalPostingResponseDto);
    }
}
