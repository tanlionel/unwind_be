package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.model.FeedbackDTO.FeedbackReportResponseDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackRequestDto;
import com.capstone.unwind.model.FeedbackDTO.FeedbackResponseDto;
import com.capstone.unwind.model.FeedbackDTO.ReportRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    FeedbackResponseDto createRentalFeedback(FeedbackRequestDto feedbackRequestDto);
    FeedbackResponseDto createExchangeFeedback(FeedbackRequestDto feedbackRequestDto);
    Page<FeedbackResponseDto> getAllFeedbackByResortId(Integer resortId, Pageable pageable);
    Page<FeedbackReportResponseDto> getTsStaffFeedbackByResortId(Pageable pageable);
    Page<FeedbackReportResponseDto> getSystemStaffFeedbackByResortId(Integer resortId,Boolean isReport,Pageable pageable);
    FeedbackReportResponseDto reportFeedback(Integer feedbackId,ReportRequestDto reportRequestDto);
    FeedbackReportResponseDto deActiveFeedback(Integer feedbackId);
}
