package com.sdehunt.commons.email;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Map;

public class SesEmailSender implements EmailSender {

    static final String CONFIGSET = "default-configuration-set";
    private final AmazonSimpleEmailService client;
    private final ObjectMapper objectMapper;

    public SesEmailSender() {
        this.client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(".aws/credentials", null))
                .withRegion(Regions.EU_WEST_1).build(); // We have SES in EU_WEST_1 region
        this.objectMapper = new ObjectMapper();

    }

    @Override
    public void send(String from, String to, String templateId, Map<String, Object> data) {
        SendTemplatedEmailRequest request = buildRequest(from, to, templateId, data);
        client.sendTemplatedEmail(request);
    }

    @SneakyThrows(JsonProcessingException.class)
    private SendTemplatedEmailRequest buildRequest(String from, String to, String templateId, Map<String, Object> data) {
        return new SendTemplatedEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withTemplate(templateId)
                .withTemplateData(objectMapper.writeValueAsString(data))
                .withSource(from)
                // Comment or remove the next line if you are not using a
                // configuration set
                .withConfigurationSetName(CONFIGSET);

    }
}
