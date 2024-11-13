package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.ExchangePostingDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExchangePostingService {
    ExchangePostingResponseDto createExchangePosting(ExchangePostingRequestDto exchangePostingRequestDto) throws ErrMessageException, OptionalNotFoundException;
    Page<ExchangePostingResponseTsStaffDTO> getAllExchangePostingsTsStaff(String roomInfoCode, Pageable pageable) throws OptionalNotFoundException;
    PostingExchangeDetailResponseDTO getExchangePostingDetailById(Integer postingId) throws OptionalNotFoundException;
    ExchangePostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, ExchangePostingApprovalDto exchangePostingApprovalDto) throws OptionalNotFoundException, ErrMessageException;
    ExchangePostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException;
    Page<PostingExchangeResponseDTO> getAllPostings(Integer resortId, Pageable pageable) throws OptionalNotFoundException;
    Page<PostingExchangeResponseDTO> getAllExchangePublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException;

    ExchangeRequestDetailDto createRequestExchange(Integer postingId, ExchangeRequestDto exchangeRequestDto) throws OptionalNotFoundException, ErrMessageException;

    ExchangeRequestDetailDto getExchangeRequestById(Integer requestId) throws OptionalNotFoundException;

    Page<ExchangeRequestBasicDto> getPaginationExchangeRequest(int pageNo, int pageSize) throws ErrMessageException;

    Page<ExchangeRequestPostingBasicDto> getPaginationExchangeRequestByPostingId(int pageNo, int pageSize,int postingId);
}
