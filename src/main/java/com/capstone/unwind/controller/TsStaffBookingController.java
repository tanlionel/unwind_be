package com.capstone.unwind.controller;

import com.capstone.unwind.entity.MergedBooking;
import com.capstone.unwind.service.ServiceInterface.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/timeshare-staff")
@RequiredArgsConstructor
@CrossOrigin
public class TsStaffBookingController {
    @Autowired
    private final BookingService bookingService;
    @GetMapping("booking")
    public Page<MergedBooking> getMergeBookingByDate(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate searchDate,
                                                     @RequestParam(required = false,defaultValue = "false") boolean isComing,
                                                     @RequestParam(required = false,defaultValue = "false") boolean willGo){
        if (searchDate==null) searchDate=LocalDate.now();
        Page<MergedBooking> mergedBookingPage = bookingService.getMergeBookingByDateTsStaff(pageNo,pageSize,searchDate,isComing,willGo);
        return mergedBookingPage;
    }
}
