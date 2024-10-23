package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RentalPostingService {
    List<PostingResponseDTO> getAllPostings() throws OptionalNotFoundException;
    Page<PostingResponseDTO> getAllPublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException;
    PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException;
}
