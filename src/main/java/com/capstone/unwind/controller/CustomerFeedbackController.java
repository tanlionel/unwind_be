package com.capstone.unwind.controller;

import com.capstone.unwind.model.FeedbackDTO.FeedbackRequestDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackResponseDto;
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
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerFeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/feedback/rental")
    public ResponseEntity<FeedbackResponseDto> createRentalFeedback(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        FeedbackResponseDto feedbackResponse = feedbackService.createRentalFeedback(feedbackRequestDto);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
    @PostMapping("/feedback/exchange")
    public ResponseEntity<FeedbackResponseDto> createExchangeFeedback(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        FeedbackResponseDto feedbackResponse = feedbackService.createExchangeFeedback(feedbackRequestDto);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
    @GetMapping("/feedback/resort/{resortId}")
    public Page<FeedbackResponseDto> getAllFeedbackByResortId(
            @PathVariable Integer resortId,@RequestParam(required = false,defaultValue = "0") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<FeedbackResponseDto> feedbackPage = feedbackService.getAllFeedbackByResortId(resortId, pageable);
        return feedbackPage ;
    }
}
