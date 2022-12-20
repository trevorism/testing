package com.trevorism.testing.service

import com.trevorism.testing.model.WorkflowRequest
import com.trevorism.testing.model.WorkflowStatus

interface GithubClient {

    boolean invokeWorkflow(String projectName, WorkflowRequest workflowRequest)

    WorkflowStatus getWorkflowStatus(String projectName, WorkflowRequest workflowRequest)
}