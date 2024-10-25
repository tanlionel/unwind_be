package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.*;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timeshare-staff")
@RequiredArgsConstructor
@CrossOrigin
public class RentalPostingTsStaffController {
    @Autowired
    RentalPostingService rentalPostingService;

    @GetMapping("rental/postings")
    public Page<PostingResponseTsStaffDTO> getPublicPostings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String roomInfoCode) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostingResponseTsStaffDTO> postingResponsePage = rentalPostingService.getAllPostingsTsStaff(roomInfoCode, pageable);

        return postingResponsePage;
    }
    @GetMapping("/rentals/postings/price-guide")
    public Page<PostingPackage4ResponseDTO> getPostingsPrice(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String roomInfoCode) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostingPackage4ResponseDTO> postingResponsePage = rentalPostingService.getAllPostingsPackage4(roomInfoCode, pageable);

        return postingResponsePage;
    }
    @GetMapping("rental/posting/price-guide/{postingId}")
    public PostingDetailPackage4ResponseDTO getPostingPriceDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailPackage4ById(postingId);
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailTsStaffResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailTsStaffById(postingId);
    }
}
