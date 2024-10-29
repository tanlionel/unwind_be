package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RentalPostingService {
    List<PostingResponseDTO> getAllPostings() throws OptionalNotFoundException;
    Page<PostingResponseDTO> getAllPublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException;
    PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException;
    Page<PostingResponseTsStaffDTO> getAllPostingsTsStaff(String resortName, Pageable pageable) throws OptionalNotFoundException;
    PostingDetailTsStaffResponseDTO getRentalPostingDetailTsStaffById(Integer postingId) throws OptionalNotFoundException;
    Page<PostingResponseTsStaffDTO> getAllPostingsSystemStaff(String roomInfoCode, Pageable pageable) throws OptionalNotFoundException;

    RentalPostingResponseDto createRentalPosting(RentalPostingRequestDto rentalPostingRequestDto) throws ErrMessageException, OptionalNotFoundException;
}
