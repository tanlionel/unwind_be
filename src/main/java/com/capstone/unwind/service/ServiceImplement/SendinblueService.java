package com.capstone.unwind.service.ServiceImplement;


import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SendinblueService {

    @Value("${sendinblue.api-key}")
    private String apiKey;

    @Value("${sendinblue.sender-email}")
    private String senderEmail;



    private TransactionalEmailsApi transactionalEmailsApi;

    @PostConstruct
    public void init() {
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKey(apiKey);
        this.transactionalEmailsApi = new TransactionalEmailsApi(apiClient);
    }
    public void sendEmailWithTemplate(String to, EmailEnum emailEnum, EmailRequestDto emailRequestDto ) {

        SendSmtpEmailSender sender = new SendSmtpEmailSender().email(senderEmail);
        SendSmtpEmailTo recipient = new SendSmtpEmailTo().email(to);

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                .sender(sender)
                .to(Arrays.asList(recipient))
                .templateId(emailEnum.getTemplateId())
                .params(Map.of(
                        "name", emailRequestDto.getName(),
                        "subject",emailRequestDto.getSubject(),
                        "content", emailRequestDto.getContent()
                ));
        try {
            transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
            log.info("Email sent successfully to {}", to);
        } catch (ApiException e) {
            log.error("Error sending email to {}: {}", to, e.getMessage(), e);
        }
    }

}