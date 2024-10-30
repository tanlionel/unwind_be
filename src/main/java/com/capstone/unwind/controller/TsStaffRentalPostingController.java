package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timeshare-staff")
@RequiredArgsConstructor
@CrossOrigin
public class TsStaffRentalPostingController {
    @Autowired
    RentalPostingService rentalPostingService;

    @GetMapping("rental/postings")
    public Page<PostingResponseTsStaffDTO> getPublicPostings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String roomInfoCode) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostingResponseTsStaffDTO> postingResponsePage = rentalPostingService.getAllPostingsTsStaff(roomInfoCode, pageable);

        return postingResponsePage;
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailTsStaffResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailTsStaffById(postingId);
    }
    @PostMapping("rental/posting/approval/{postingId}")
    public ResponseEntity<RentalPostingApprovalResponseDto> approvalPosting(@PathVariable Integer postingId, @RequestBody RentalPostingApprovalDto rentalPostingApprovalDto) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto = rentalPostingService.approvalPostingTimeshareStaff(postingId,rentalPostingApprovalDto);
        return ResponseEntity.ok(rentalPostingApprovalResponseDto);
    }
    @PostMapping("rental/posting/reject/{postingId}")
    public ResponseEntity<RentalPostingApprovalResponseDto> rejectPosting(@PathVariable Integer postingId,@RequestBody String note) throws ErrMessageException, OptionalNotFoundException {
        RentalPostingApprovalResponseDto rentalPostingApprovalDto = rentalPostingService.rejectPostingTimeshareStaff(postingId,note);
        return ResponseEntity.ok(rentalPostingApprovalDto);
    }
}
