package com.trevorism.testing.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.WorkflowRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@jakarta.inject.Singleton
class DefaultGithubClient implements GithubClient {

    private static final Logger log = LoggerFactory.getLogger(DefaultGithubClient.class.name)
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
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

    private static String createUrl(String projectName) {
        return "https://github.project.trevorism.com/repo/${projectName}/workflow"
    }

}
