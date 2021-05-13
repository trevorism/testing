package com.trevorism.testing.service.run

import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteKind

class ExecuteTestStrategyFactory {

    private static SecureHttpClient client = new DefaultSecureHttpClient()

    static ExecuteTestStrategy create(String testKind) {
        TestSuiteKind kind = testKind.toUpperCase() as TestSuiteKind
        switch (kind) {
            case TestSuiteKind.UNIT:
                return { client.post(createCinvokeUrl("unit", it), "{}") == "true" } as ExecuteTestStrategy
            case TestSuiteKind.JAVASCRIPT:
                return { client.post(createCinvokeUrl("karma", it), "{}") == "true" } as ExecuteTestStrategy
            case TestSuiteKind.CUCUMBER:
                return { client.post(createCinvokeUrl("acceptance", it), "{}") == "true" } as ExecuteTestStrategy
            case TestSuiteKind.WEB:
            case TestSuiteKind.POWERSHELL:
            case TestSuiteKind.SELENIUM:
                return { false } as ExecuteTestStrategy
        }
        return { false } as ExecuteTestStrategy
    }

    private static String createCinvokeUrl(String prefix, TestSuite it) {
        return "https://cinvoke.datastore.trevorism.com/job/${prefix}-${it.source}/build"
    }
}
