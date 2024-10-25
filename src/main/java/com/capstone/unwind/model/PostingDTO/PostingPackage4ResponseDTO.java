package com.capstone.unwind.model.PostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Data
@Getter
@Setter
@Builder
public class PostingPackage4ResponseDTO {
    private Integer rentalPostingId;
    private Integer timeShareId;
    private Integer roomInfoId;
    private String roomCode;
    private Integer resortId;
    private String resortName;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdDate;
    private Integer ownerId;
    private String ownerName;
    private String ownerPhone;
    private String status;
    private boolean isActive;
    @JsonIgnore
    private Boolean isValid;
}
