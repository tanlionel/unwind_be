package com.capstone.unwind.model.BookingDTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingTsStaffRequestDto {
    boolean isCheckIn;
    boolean isCheckOut;
}
