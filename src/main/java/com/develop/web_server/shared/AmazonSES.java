package com.develop.web_server.shared;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.develop.web_server.shared.dto.UserDto;
import org.springframework.stereotype.Component;


@Component
public class AmazonSES {
    private String FROM = "weishun.liao002@gmail.com";

    public void verifyEmail(UserDto userDto) {
        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(credentialsProvider).build();


        String TEXT_BODY = "Please verify your email address. Click http://localhost:8080/users/email-verification?token=";
        String SUBJECT = "Last step to complete your registration";

        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(TEXT_BODY + userDto.getEmailVerificationToken())))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);




        client.sendEmail(sendEmailRequest);
        System.out.println("Email sent");
    }

    public boolean passwordReset(String token, String email) {
        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(credentialsProvider).build();


        String TEXT_BODY = "Please click the link to set your new password. Click http://localhost:8080/password-reset?token=";
        String SUBJECT = "Password reset";

        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(email))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(TEXT_BODY + token)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);


        SendEmailResult sendEmailResult = client.sendEmail(sendEmailRequest);
        if (sendEmailResult != null && sendEmailResult.getMessageId() != null && !sendEmailResult.getMessageId().isEmpty()) {
            return true;
        }
        System.out.println("Email sent");
        return false;
    }
}
