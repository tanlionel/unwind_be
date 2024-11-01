package com.capstone.unwind.controller;

import com.capstone.unwind.model.FeedbackDTO.FeedbackReportResponseDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackResponseDto;
import com.capstone.unwind.model.FeedbackDTO.ReportRequestDto;
import com.capstone.unwind.service.ServiceInterface.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system-staff")
@RequiredArgsConstructor
@CrossOrigin
public class SystemStaffFeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/feedback/resort/{resortId}")
    public Page<FeedbackReportResponseDto> getAllFeedbackByResortId( @PathVariable Integer resortId,
                                                              @RequestParam(defaultValue = "false") Boolean isReport,
                                                              @RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                              @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<FeedbackReportResponseDto> feedbackPage = feedbackService.getSystemStaffFeedbackByResortId(resortId,isReport,pageable);
        return feedbackPage ;
    }
    @PutMapping("/feedback/deActive/{feedbackId}")
    public ResponseEntity<FeedbackReportResponseDto> reportFeedback(@PathVariable Integer feedbackId) {
        FeedbackReportResponseDto feedbackResponse = feedbackService.deActiveFeedback(feedbackId);
        return ResponseEntity.ok(feedbackResponse);
    }
}
