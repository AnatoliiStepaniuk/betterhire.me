package com.sdehunt.commons.params;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;

public class SsmParameterService implements ParameterService {

    private final AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder
            .standard()
            .withCredentials(new ProfileCredentialsProvider(".aws/credentials", null))
            .withRegion("eu-central-1") // TODO take from config
            .build();

    @Override
    public String get(String param) {
        GetParameterRequest getParameterRequest = new GetParameterRequest();
        getParameterRequest.setName(param);
        return ssm.getParameter(getParameterRequest).getParameter().getValue();
    }
}
