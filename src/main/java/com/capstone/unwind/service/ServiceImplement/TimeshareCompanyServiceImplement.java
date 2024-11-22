package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.DocumentStore;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.enums.DocumentStoreEnum;
import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyMapper;
import com.capstone.unwind.model.TimeshareCompany.UpdateTimeshareCompanyDto;
import com.capstone.unwind.repository.DocumentStoreRepository;
import com.capstone.unwind.repository.TimeshareCompanyRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.TimeshareCompanyService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.capstone.unwind.config.EmailMessageConfig.*;
import static com.capstone.unwind.config.EmailMessageConfig.TIMESHARE_COMPANY_CREATION_CONTENT;

@RequiredArgsConstructor
@Service
public class TimeshareCompanyServiceImplement implements TimeshareCompanyService {
    @Autowired
    private final TimeshareCompanyMapper timeshareCompanyMapper;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final DocumentStoreRepository documentStoreRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final SendinblueService sendinblueService;
    @Override
    public TimeshareCompanyDto createTimeshareCompany(TimeshareCompanyDto timeshareCompanyDto) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException {
        User user = userRepository.findUserById(timeshareCompanyDto.getOwnerId());
        if (user==null) throw new UserDoesNotExistException();
        if (user.getRole().getId() != 2) throw new ErrMessageException("Must be timeshare company role");
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (timeshareCompany!= null) throw new ErrMessageException("User already exist in another timeshare company");
        TimeshareCompany timeshareCompanyRequest = TimeshareCompany.builder()
                .timeshareCompanyName(timeshareCompanyDto.getTimeshareCompanyName())
                .logo(timeshareCompanyDto.getLogo())
                .address(timeshareCompanyDto.getAddress())
                .description(timeshareCompanyDto.getDescription())
                .contact(timeshareCompanyDto.getContact())
                .isActive(true)
                .owner(user)
                .build();
        TimeshareCompany timeshareCompanyDB = timeshareCompanyRepository.save(timeshareCompanyRequest);
        try {
            for (String imageUrl : timeshareCompanyDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.TimeshareCompany.toString());
                document.setEntityId(timeshareCompanyDB.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        TimeshareCompanyDto timeshareCompanyDtoDB = timeshareCompanyMapper.toDto(timeshareCompanyDB);
        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject(TIMESHARE_COMPANY_CREATION_SUBJECT);
            emailRequestDto.setContent(TIMESHARE_COMPANY_CREATION_CONTENT);
            sendinblueService.sendEmailWithTemplate(
                    timeshareCompanyDB.getOwner().getEmail(),
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return timeshareCompanyDtoDB;
    }
    @Override
    public TimeshareCompanyDto getProfileTimeshareCompanyById() throws EntityDoesNotExistException {
        User user = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (timeshareCompany==null) throw new EntityDoesNotExistException();
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(timeshareCompany.getId(), DocumentStoreEnum.TimeshareCompany.toString());
        TimeshareCompanyDto responseDTO = timeshareCompanyMapper.toDto(timeshareCompany);
        responseDTO.setImageUrls(imageUrls);
        return responseDTO;
    }
    @Override
    public TimeshareCompanyDto updateTimeshareCompany( UpdateTimeshareCompanyDto timeshareCompanyDto) throws   ErrMessageException, OptionalNotFoundException {
        User user = userService.getLoginUser();
        TimeshareCompany existingTimeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (existingTimeshareCompany==null) throw new OptionalNotFoundException("Timeshare Company not found");

        existingTimeshareCompany.setTimeshareCompanyName(timeshareCompanyDto.getTimeshareCompanyName());
        existingTimeshareCompany.setLogo(timeshareCompanyDto.getLogo());
        existingTimeshareCompany.setAddress(timeshareCompanyDto.getAddress());
        existingTimeshareCompany.setDescription(timeshareCompanyDto.getDescription());
        existingTimeshareCompany.setContact(timeshareCompanyDto.getContact());
        TimeshareCompany updatedTimeshareCompany = timeshareCompanyRepository.save(existingTimeshareCompany);
        documentStoreRepository.deactivateOldImages(updatedTimeshareCompany.getId(), DocumentStoreEnum.TimeshareCompany.toString());

        try {
            for (String imageUrl : timeshareCompanyDto.getImageUrls()) {
                DocumentStore document = new DocumentStore();
                document.setType(DocumentStoreEnum.TimeshareCompany.toString());
                document.setEntityId(updatedTimeshareCompany.getId());
                document.setImageUrl(imageUrl);
                document.setIsActive(true);
                documentStoreRepository.save(document);
            }
        } catch (Exception e) {
            throw new ErrMessageException("Error when saving images");
        }
        return timeshareCompanyMapper.toDto(updatedTimeshareCompany);
    }
    @Override()
    public Page<TimeshareCompanyDto> getPageableTimeshareCompany(Integer pageNo, Integer pageSize, String tsName) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("id").ascending());
        Page<TimeshareCompany> timeshareCompanyPage = timeshareCompanyRepository.findAllByTimeshareCompanyNameContaining(tsName,pageable);
        Page<TimeshareCompanyDto> timeshareCompanyDtoPage = timeshareCompanyPage.map(timeshareCompanyMapper::toDto);
        return timeshareCompanyDtoPage;
    }

    @Override
    public TimeshareCompanyDto getTimeshareCompanyById(Integer tsId) throws EntityDoesNotExistException {
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyById(tsId);
        if (timeshareCompany==null) throw new EntityDoesNotExistException();
        List<String> imageUrls = documentStoreRepository.findUrlsByEntityIdAndType(timeshareCompany.getId(), DocumentStoreEnum.TimeshareCompany.toString());
        TimeshareCompanyDto responseDTO = timeshareCompanyMapper.toDto(timeshareCompany);
        responseDTO.setImageUrls(imageUrls);
        return responseDTO;
    }
}
