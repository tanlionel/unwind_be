package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.FeedbackDTO.*;
import com.capstone.unwind.model.PostingDTO.PostingResponseTsStaffDTO;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.FaqMapper;
import com.capstone.unwind.model.SystemDTO.FaqRequestDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.FeedbackService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackServiceImplement implements FeedbackService {
    @Autowired
    private final FeedbackRepository feedbackRepository;
    @Autowired
    private final RentalBookingRepository rentalBookingRepository;
    @Autowired
    private final ExchangeBookingRepository exchangeBookingRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final FeedbackMapper feedbackMapper;
    @Autowired
    private final FeedbackReportMapper feedbackReportMapper;
    @Autowired
    private final TimeShareStaffService timeShareStaffService;


@Override
    public FeedbackResponseDto createRentalFeedback(FeedbackRequestDto feedbackRequestDto) {
    RentalBooking booking = rentalBookingRepository.findById(feedbackRequestDto.getBookingId())
            .orElseThrow(() -> new IllegalArgumentException("Booking not found for this renter"));

        Feedback feedback = Feedback.builder()
                .comment(feedbackRequestDto.getComment())
                .ratingPoint(feedbackRequestDto.getRatingPoint())
                .resort(booking.getRentalPosting().getTimeshare().getRoomInfo().getResort())
                .user(booking.getRenter())
                .isActive(true)
                .isReport(false)
                .build();
        Feedback savedFeedback = feedbackRepository.save(feedback);
        booking.setIsFeedback(true);
        rentalBookingRepository.save(booking);

        return feedbackMapper.toFeedbackResponseDto(savedFeedback);
    }
    @Override
    public FeedbackResponseDto createExchangeFeedback(FeedbackRequestDto feedbackRequestDto) {

        ExchangeBooking booking = exchangeBookingRepository.findById(feedbackRequestDto.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for this renter"));

        Feedback feedback = Feedback.builder()
                .comment(feedbackRequestDto.getComment())
                .ratingPoint(feedbackRequestDto.getRatingPoint())
                .resort(booking.getExchangePosting().getTimeshare().getRoomInfo().getResort())
                .user(booking.getRenter())
                .isActive(true)
                .isReport(false)
                .build();
        Feedback savedFeedback = feedbackRepository.save(feedback);
        booking.setIsFeedback(true);
        exchangeBookingRepository.save(booking);

        return feedbackMapper.toFeedbackResponseDto(savedFeedback);
    }
    @Override
    public Page<FeedbackResponseDto> getAllFeedbackByResortId(Integer resortId, Pageable pageable)  {
        Page<Feedback> feedbacks = feedbackRepository.findAllByResort_IdAndIsActive(
                resortId,true,pageable);
        Page<FeedbackResponseDto> feedbackDtoPage = feedbacks.map(feedbackMapper::toFeedbackResponseDto);
        return feedbackDtoPage;
    }
    @Override
    public Page<FeedbackReportResponseDto> getTsStaffFeedbackByResortId(Pageable pageable)  {
        TimeShareCompanyStaffDTO timeshareCompanyStaff =  timeShareStaffService.getLoginStaff();
        Page<Feedback> feedbacks = feedbackRepository.findByResortIdAndIsActive(
                timeshareCompanyStaff.getResortId(),pageable);
        Page<FeedbackReportResponseDto> feedbackDtoPage = feedbacks.map(feedbackReportMapper::toFeedbackResponseDto);
        return feedbackDtoPage;
    }
    @Override
    public Page<FeedbackReportResponseDto> getSystemStaffFeedbackByResortId(Integer resortId,Boolean isReport,Pageable pageable)  {
        Page<Feedback> feedbacks = feedbackRepository.findByResortIdAndIsActiveAndIsReport(
                resortId,isReport,pageable);
        Page<FeedbackReportResponseDto> feedbackDtoPage = feedbacks.map(feedbackReportMapper::toFeedbackResponseDto);
        return feedbackDtoPage;
    }
    @Override
    public FeedbackReportResponseDto reportFeedback(Integer feedbackId,ReportRequestDto reportRequestDto) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        feedback.setIsReport(true);
        feedback.setNote(reportRequestDto.getNote());
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return feedbackReportMapper.toFeedbackResponseDto(updatedFeedback);
    }
    @Override
    public FeedbackReportResponseDto deActiveFeedback(Integer feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        feedback.setIsActive(false);
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return feedbackReportMapper.toFeedbackResponseDto(updatedFeedback);
    }
}
