package com.capstone.unwind.model.FeedbackDTO;

import com.capstone.unwind.entity.Feedback;
import com.capstone.unwind.entity.RentalBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "resort.id", target = "resort.id")
    @Mapping(source = "resort.resortName", target = "resort.resortName")
    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "user.fullName", target = "user.fullName")
    @Mapping(source = "user.avatar", target = "user.avatar")
    FeedbackResponseDto toFeedbackResponseDto(Feedback feedback);
}