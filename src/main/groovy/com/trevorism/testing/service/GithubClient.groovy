package com.trevorism.testing.service

import com.trevorism.testing.model.WorkflowRequest

interface GithubClient {

    boolean invokeWorkflow(String projectName, WorkflowRequest workflowRequest)

}