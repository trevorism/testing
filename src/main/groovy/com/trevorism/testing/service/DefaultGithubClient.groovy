package com.trevorism.testing.service

import com.google.gson.Gson
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.WorkflowRequest
import com.trevorism.testing.model.WorkflowStatus
import io.micronaut.runtime.http.scope.RequestScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RequestScope
class DefaultGithubClient implements GithubClient {

    private static final Logger log = LoggerFactory.getLogger(DefaultGithubClient.class.name)
    private Gson gson = new Gson()
    private SecureHttpClient client

    DefaultGithubClient(SecureHttpClient secureHttpClient){
        this.client = secureHttpClient
    }

    @Override
    boolean invokeWorkflow(String projectName, WorkflowRequest workflowRequest) {
        try {
            client.post(createUrl(projectName), gson.toJson(workflowRequest))
            return true
        } catch (Exception e) {
            log.warn("Unable to invoke workflow", e)
            return false
        }
    }

    @Override
    WorkflowStatus getWorkflowStatus(String projectName, WorkflowRequest workflowRequest) {
        String status = client.get("${createUrl(projectName)}/${workflowRequest.yamlName}")
        gson.fromJson(status, WorkflowStatus)
    }

    private static String createUrl(String projectName) {
        return "https://github.project.trevorism.com/repo/${projectName}/workflow"
    }

}
