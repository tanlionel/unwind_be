package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingApprovalDto;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingApprovalResponseDto;
import com.capstone.unwind.model.ExchangePostingDTO.ExchangePostingResponseTsStaffDTO;
import com.capstone.unwind.model.ExchangePostingDTO.PostingExchangeDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseTsStaffDTO;
import com.capstone.unwind.model.PostingDTO.RentalPostingApprovalDto;
import com.capstone.unwind.model.PostingDTO.RentalPostingApprovalResponseDto;
import com.capstone.unwind.service.ServiceInterface.ExchangePostingService;
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
public class TSStaffExchangePostingController {
    @Autowired
    ExchangePostingService exchangePostingService;
    @GetMapping("exchange/postings")
    public Page<ExchangePostingResponseTsStaffDTO> getPublicPostings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String roomInfoCode) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ExchangePostingResponseTsStaffDTO> postingResponsePage = exchangePostingService.getAllExchangePostingsTsStaff(roomInfoCode, pageable);

        return postingResponsePage;
    }
    @GetMapping("exchange/posting/{postingId}")
    public PostingExchangeDetailResponseDTO getExchangePostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return exchangePostingService.getExchangePostingDetailById(postingId);
    }
    @PostMapping("exchange/posting/approval/{postingId}")
    public ResponseEntity<ExchangePostingApprovalResponseDto> approvalPosting(@PathVariable Integer postingId, @RequestBody ExchangePostingApprovalDto exchangePostingApprovalDto) throws ErrMessageException, OptionalNotFoundException {
        ExchangePostingApprovalResponseDto exchangePostingApprovalResponseDto = exchangePostingService.approvalPostingTimeshareStaff(postingId,exchangePostingApprovalDto);
        return ResponseEntity.ok(exchangePostingApprovalResponseDto);
    }
    @PostMapping("exchange/posting/reject/{postingId}")
    public ResponseEntity<ExchangePostingApprovalResponseDto> rejectPosting(@PathVariable Integer postingId,@RequestBody String note) throws ErrMessageException, OptionalNotFoundException {
        ExchangePostingApprovalResponseDto exchangePostingApprovalResponseDto = exchangePostingService.rejectPostingTimeshareStaff(postingId,note);
        return ResponseEntity.ok(exchangePostingApprovalResponseDto);
    }
}
