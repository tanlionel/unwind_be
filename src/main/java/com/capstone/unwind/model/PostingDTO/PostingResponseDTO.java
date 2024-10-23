package com.capstone.unwind.model.PostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class PostingResponseDTO {
    private Integer rentalPostingId;
    private Integer timeShareId;
    private Integer roomInfoId;
    private String roomName;
    private Integer resortId;
    private String resortName;
    private String address;
    private Boolean isVerify;
    private Integer nights;
    private Float pricePerNights;
    private Float totalPrice;
/*    private Integer rental_package_id;
    private String rental_package_name;*/
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;
    private String status;
    private boolean isActive;

}
