package com.capstone.unwind.model.FeedbackDTO;

import com.capstone.unwind.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedbackReportMapper {
    FeedbackReportMapper INSTANCE = Mappers.getMapper(FeedbackReportMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "isReport", target = "isReport")
    @Mapping(source = "resort.id", target = "resort.id")
    @Mapping(source = "resort.resortName", target = "resort.resortName")
    @Mapping(source = "customer.id", target = "customer.id")
    @Mapping(source = "customer.fullName", target = "customer.fullName")
    @Mapping(source = "customer.avatar", target = "customer.avatar")
    FeedbackReportResponseDto toFeedbackResponseDto(Feedback feedback);
}