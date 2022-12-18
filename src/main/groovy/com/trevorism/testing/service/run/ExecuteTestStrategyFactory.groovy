package com.trevorism.testing.service.run

import com.google.gson.Gson
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteKind
import com.trevorism.testing.model.WorkflowRequest

class ExecuteTestStrategyFactory {

    private static SecureHttpClient client = new DefaultSecureHttpClient()
    private static Gson gson = new Gson()

    static ExecuteTestStrategy create(String testKind) {
        TestSuiteKind kind = testKind.toUpperCase() as TestSuiteKind
        switch (kind) {
            case TestSuiteKind.UNIT:
                return { client.post(createUrl(it), createJsonForRequest(true)) } as ExecuteTestStrategy
            case TestSuiteKind.CUCUMBER:
                return { client.post(createUrl(it), createJsonForRequest(false)) } as ExecuteTestStrategy
            case TestSuiteKind.JAVASCRIPT:
            case TestSuiteKind.WEB:
            case TestSuiteKind.POWERSHELL:
            case TestSuiteKind.SELENIUM:
                return { false } as ExecuteTestStrategy
        }
        return { false } as ExecuteTestStrategy
    }

    private static String createUrl(TestSuite it) {
        return "https://github.project.trevorism.com/repo/${it.source}/workflow"
    }

    private static String createJsonForRequest(boolean unitTest){
        WorkflowRequest workflowRequest = new WorkflowRequest(unitTest: unitTest)
        gson.toJson(workflowRequest)
    }
}
