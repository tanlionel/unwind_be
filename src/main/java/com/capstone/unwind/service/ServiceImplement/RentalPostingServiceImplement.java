package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.PostingDTO.ListRentalPostingMapper;
import com.capstone.unwind.model.PostingDTO.PostingDetailMapper;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import com.capstone.unwind.model.SystemDTO.PolicyMapper;
import com.capstone.unwind.repository.CustomerRepository;
import com.capstone.unwind.repository.PolicyRespository;
import com.capstone.unwind.repository.RentalPostingRepository;
import com.capstone.unwind.service.ServiceInterface.RentalPostingService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalPostingServiceImplement implements RentalPostingService {
    @Autowired
    private final RentalPostingRepository rentalPostingRepository;
    @Autowired
    private final ListRentalPostingMapper listRentalPostingMapper;
    @Autowired
    private final PostingDetailMapper postingDetailMapper;
    @Autowired
    private final UserService userService;
    @Autowired
    private final CustomerRepository customerRepository;
    private final String processing = "Processing";
    @Override
    public List<PostingResponseDTO> getAllPostings() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist for user with ID: " + user.getId());
        }
        List<RentalPosting> rentalPostings = rentalPostingRepository.findAllByOwnerIdAndIsActive(customer.getId(),true);
        List<RentalPosting> validRentalPostings = rentalPostings.stream()
                .filter(timeShare -> {
                    RoomInfo roomInfo = timeShare.getRoomInfo();
                    if (roomInfo == null || !roomInfo.getIsActive()) {
                        return false;
                    }
                    Resort resort = roomInfo.getResort();
                            return resort != null && resort.getIsActive();
                })
                .collect(Collectors.toList());
        return listRentalPostingMapper.entitiesToDtos(validRentalPostings);
    }
    @Override
    public Page<PostingResponseDTO> getAllPublicPostings(String resortName, Pageable pageable) throws OptionalNotFoundException {
        Page<RentalPosting> rentalPostings = rentalPostingRepository.findAllByIsActiveAndRoomInfo_Resort_ResortNameContainingAndRoomInfo_IsActiveAndStatus(
                true, resortName, true, processing, pageable);
        return rentalPostings.map(listRentalPostingMapper::entityToDto);
    }
    @Override
    public PostingDetailResponseDTO getRentalPostingDetailById(Integer postingId) throws OptionalNotFoundException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Active Rental Posting not found with ID: " + postingId));
        return postingDetailMapper.entityToDto(rentalPosting);
    }

}
