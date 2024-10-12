package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@CrossOrigin
public class PubicController {
    @Autowired
    private final ResortService resortService;
    @GetMapping("/resort")
    public Page<ResortDto> getPageableResort(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String resortName) throws UserDoesNotHavePermission {
        Page<ResortDto> resortDtoPage = resortService.getPublicPageableResort(pageNo,pageSize,resortName);
        return resortDtoPage;
    }
    @GetMapping("/resort/{resortId}")
    public ResortDetailResponseDTO getResortById(@PathVariable Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        ResortDetailResponseDTO resortDetailResponseDTO = resortService.getResortById(resortId);
        return resortDetailResponseDTO;
    }
}
