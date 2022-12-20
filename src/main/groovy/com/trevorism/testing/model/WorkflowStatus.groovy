package com.trevorism.testing.model

import groovy.transform.ToString

@ToString
class WorkflowStatus {
    String state
    String result
    Date createdAt
    Date updatedAt
}
