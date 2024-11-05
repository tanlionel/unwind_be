package com.capstone.unwind.controller;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.PostingDetailTsStaffResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseTsStaffDTO;
import com.capstone.unwind.model.PostingDTO.RentalPostingApprovalResponseDto;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/system-staff")
@RequiredArgsConstructor
@CrossOrigin
public class SystemStaffRentalPostingController {
    @Autowired
    RentalPostingService rentalPostingService;


    @GetMapping("rental/postings")
    public Page<PostingResponseTsStaffDTO> getPublicPostings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String resortName) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostingResponseTsStaffDTO> postingResponsePage = rentalPostingService.getAllPostingsSystemStaff(resortName, pageable);

        return postingResponsePage;
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailTsStaffResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailTsStaffById(postingId);
    }
    @PostMapping("rental/posting/approval/{postingId}")
    public ResponseEntity<RentalPostingApprovalResponseDto> approvalRentalPostingSystemStaff(@PathVariable Integer postingId,@RequestParam Float newPriceValuation) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto = rentalPostingService.approvalPostingSystemStaff(postingId,newPriceValuation);
        return ResponseEntity.ok(rentalPostingApprovalResponseDto);
    }

}