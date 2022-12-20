package com.trevorism.testing.service

import com.google.gson.Gson
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.WorkflowRequest
import com.trevorism.testing.model.WorkflowStatus

class DefaultGithubClient implements GithubClient{

    private SecureHttpClient client = new DefaultSecureHttpClient()
    private Gson gson = new Gson()

    @Override
    boolean invokeWorkflow(String projectName, WorkflowRequest workflowRequest) {
        client.post(createUrl(projectName), gson.toJson(workflowRequest))
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
