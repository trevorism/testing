package com.trevorism.testing.model

class WorkflowRequest {
    String branchName = "master"
    String yamlName = "test.yml"
    Map<String, String> workflowInputs = [:]
}
