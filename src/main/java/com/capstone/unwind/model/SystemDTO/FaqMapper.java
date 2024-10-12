package com.capstone.unwind.model.SystemDTO;

import com.capstone.unwind.entity.Faq;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    @Mapping(source = "faqId", target = "id")
    Faq toEntity(FaqDTO fagDTO);

    @Mapping(source = "id", target = "faqId")
    FaqDTO toDto(Faq faq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "faqId", target = "id")
    Faq partialUpdate(FaqDTO fagDTO, @MappingTarget Faq faq);

    List<Faq> toEntityList(List<FaqDTO> faqDTOs);
    List<FaqDTO> toDtoList(List<Faq> faqs);
}
