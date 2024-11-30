package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RentalPostingService {
    Page<PostingResponseDTO> getAllPostings(Integer resortId, Pageable pageable, String status) throws OptionalNotFoundException;
    Page<PostingResponseDTO> getAllPublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException;
    PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException;
    Page<PostingResponseTsStaffDTO> getAllPostingsTsStaff(String resortName,Integer packageId, Pageable pageable) throws OptionalNotFoundException;
    PostingDetailTsStaffResponseDTO getRentalPostingDetailTsStaffById(Integer postingId) throws OptionalNotFoundException;
    Page<PostingResponseTsStaffDTO> getAllPostingsSystemStaff(String roomInfoCode, Pageable pageable,String status) throws OptionalNotFoundException;

    RentalPostingResponseDto createRentalPosting(RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException;

    RentalPostingApprovalResponseDto approvalPostingTimeshareStaff(Integer postingId, RentalPostingApprovalDto rentalPostingApprovalDto) throws OptionalNotFoundException, ErrMessageException;

    RentalPostingApprovalResponseDto rejectPostingTimeshareStaff(Integer postingId, String note) throws OptionalNotFoundException, ErrMessageException;

    RentalPostingApprovalResponseDto approvalPostingSystemStaff(Integer postingId, Float newPriceValuation) throws OptionalNotFoundException, ErrMessageException;

    RentalPostingResponseDto actionConfirmPosting(Integer postingId, Float newPrice, Boolean isAccepted) throws OptionalNotFoundException, ErrMessageException;
    Page<PostingResponseDTO> getAllPostingsByResortId(Integer resortId, Pageable pageable);
    Page<PostingResponseTsStaffDTO> getAllPackagePostingSystemStaff(String resortName, Pageable pageable, String status, Integer packageId) throws OptionalNotFoundException;
    PostingDetailResponseDTO deActiveRentalPosting(Integer postingId) throws OptionalNotFoundException, ErrMessageException;
    RentalPostingResponseDto updateRentalPosting(Integer postingId, UpdateRentalPostingDto updateRentalPostingDto)
            throws  ErrMessageException;
}
