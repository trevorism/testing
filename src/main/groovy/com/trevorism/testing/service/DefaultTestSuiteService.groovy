package com.trevorism.testing.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteDetails
import com.trevorism.testing.model.TestSuiteKind

class DefaultTestSuiteService implements TestSuiteService {

    Repository<TestSuite> testSuiteRepository = new PingingDatastoreRepository<>(TestSuite)
    Repository<TestSuiteDetails> testSuiteDetailsRepository = new PingingDatastoreRepository<>(TestSuiteDetails)

    @Override
    TestSuite create(TestSuite testSuite) {
        validateInput(testSuite)
        TestSuite createdSuite = testSuiteRepository.create(testSuite)
        testSuiteDetailsRepository.create(createInitialDetailsFromSuite(createdSuite))
        return createdSuite
    }

    @Override
    List<TestSuite> list() {
        testSuiteRepository.list()
    }

    @Override
    TestSuite get(String id) {
        testSuiteRepository.get(id)
    }

    @Override
    TestSuite delete(String id) {
        testSuiteRepository.delete(id)
    }

    @Override
    TestSuite update(String id, TestSuite testSuite) {
        testSuiteRepository.update(id, testSuite)
    }

    @Override
    TestSuiteDetails getSuiteDetails(String testSuiteId) {
        testSuiteDetailsRepository.list().find {
            it.testSuiteId == testSuiteId
        }
    }

    private static void validateInput(TestSuite testSuite) {
        if (!testSuite || !testSuite.name) {
            throw new RuntimeException("Invalid test suite, must have a name")
        }
        if (!testSuite.source) {
            throw new RuntimeException("Invalid test suite, must come from some source")
        }
        try {
            TestSuiteKind.valueOf(testSuite.kind?.toUpperCase())
        } catch (Exception e) {
            throw new RuntimeException("Invalid test suite, must be a valid kind", e)
        }
    }

    private static TestSuiteDetails createInitialDetailsFromSuite(TestSuite testSuite) {
        String testCodeUrl = buildTestCodeUrl(testSuite)
        String testResultUrl = buildTestResultUrl(testSuite)
        String codeUnderTestUrl = "https://github.com/trevorism/${testSuite.source}"
        new TestSuiteDetails(testSuiteId: testSuite.id, testCodeUrl: testCodeUrl, testResultUrl: testResultUrl, lastRunSuccess: false, codeUnderTestUrls: [codeUnderTestUrl])

    }

    private static String buildTestCodeUrl(TestSuite testSuite) {
        TestSuiteKind kind = TestSuiteKind.valueOf(testSuite.kind.toUpperCase())
        switch (kind) {
            case TestSuiteKind.UNIT:
                return "https://github.com/trevorism/${testSuite.source}/tree/master/src/test/groovy"
            case TestSuiteKind.JAVASCRIPT:
                return "https://github.com/trevorism/${testSuite.source}/tree/master/src/app/test"
            case TestSuiteKind.CUCUMBER:
                return "https://github.com/trevorism/${testSuite.source}/tree/master/src/acceptance"
            case TestSuiteKind.WEB:
            case TestSuiteKind.POWERSHELL:
            case TestSuiteKind.SELENIUM:
                return null
        }
        return null
    }

    private static String buildTestResultUrl(TestSuite testSuite) {
        TestSuiteKind kind = TestSuiteKind.valueOf(testSuite.kind.toUpperCase())
        switch (kind) {
            case TestSuiteKind.UNIT:
                return "http://trevorism-build.eastus.cloudapp.azure.com/job/unit-${testSuite.source}/Unit_20tests"
            case TestSuiteKind.JAVASCRIPT:
            case TestSuiteKind.CUCUMBER:
            case TestSuiteKind.WEB:
            case TestSuiteKind.POWERSHELL:
            case TestSuiteKind.SELENIUM:
                return null
        }
        return null
    }
}
