package com.develop.web_server.shared;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.develop.web_server.shared.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AmazonSES {
    private String FROM = "weishun.liao002@gmail.com";

    @Value("${aws.awsAccessKey}")
    private String awsAccessKey;

    @Value("${aws.awsSecretKey}")
    private String awsSecretKey;

    public void verifyEmail(UserDto userDto) {
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey))).build();

        String TEXT_BODY = "Please verify your email address. Click http://34.82.124.184/users/email-verification?token=";
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
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey))).build();


        String TEXT_BODY = "Please click the link to set your new password. Click http://34.82.124.184/users/password-reset?token=";
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
