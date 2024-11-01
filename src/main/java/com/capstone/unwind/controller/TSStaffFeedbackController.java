package com.capstone.unwind.controller;

import com.capstone.unwind.model.FeedbackDTO.FeedbackReportResponseDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackRequestDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackResponseDto;
import com.capstone.unwind.model.FeedbackDTO.ReportRequestDto;
import com.capstone.unwind.service.ServiceInterface.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timeshare-staff")
@RequiredArgsConstructor
@CrossOrigin
public class TSStaffFeedbackController {
    @Autowired
    FeedbackService feedbackService;


    @GetMapping("/feedback/resort")
    public Page<FeedbackReportResponseDto> getAllFeedbackByResortId(
            @RequestParam(required = false,defaultValue = "0") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<FeedbackReportResponseDto> feedbackPage = feedbackService.getTsStaffFeedbackByResortId(pageable);
        return feedbackPage ;
    }
    @PutMapping("/feedback/report/{feedbackId}")
    public ResponseEntity<FeedbackReportResponseDto> reportFeedback(@PathVariable Integer feedbackId,
            @RequestBody ReportRequestDto reportRequestDto) {
        FeedbackReportResponseDto feedbackResponse = feedbackService.reportFeedback(feedbackId,reportRequestDto);
        return ResponseEntity.ok(feedbackResponse);
    }
}
