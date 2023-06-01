package com.trevorism.testing.service

import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.WorkflowRequest
import org.junit.jupiter.api.Test

class DefaultGithubClientTest {

    @Test
    void testInvokeWorkflow() {
        GithubClient githubClient = new DefaultGithubClient()
        githubClient.client = [post: { url, json -> "true"}] as SecureHttpClient
        assert githubClient.invokeWorkflow("threshold", new WorkflowRequest(workflowInputs: ["TEST_TYPE":"cucumber"]))
    }

    @Test
    void testGetWorkflowStatus() {
        GithubClient githubClient = new DefaultGithubClient()
        githubClient.client = [get: { url -> "{}"}] as SecureHttpClient
        assert githubClient.getWorkflowStatus("github", new WorkflowRequest(yamlName: "test.yml"))
    }

    @Test
    void testInvokeInvalidWorkflow() {
        GithubClient githubClient = new DefaultGithubClient()
        githubClient.client = [post: { url, json -> throw new RuntimeException("Error")}] as SecureHttpClient
        assert !githubClient.invokeWorkflow("zzz_blah", new WorkflowRequest(workflowInputs: ["TEST_TYPE":"cucumber"]))
    }
}
