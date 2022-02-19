package com.trevorism.testing.service

import com.google.gson.Gson
import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.filtering.FilterBuilder
import com.trevorism.data.model.filtering.SimpleFilter
import com.trevorism.http.HttpClient
import com.trevorism.http.JsonHttpClient
import com.trevorism.testing.controller.ErrorsController
import com.trevorism.testing.model.JenkinsRunResult
import com.trevorism.testing.model.TestError
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteDetails
import com.trevorism.testing.model.TestSuiteKind

import java.util.logging.Logger

class DefaultTestSuiteService implements TestSuiteService {

    private static final Logger log = Logger.getLogger(DefaultTestSuiteService.class.name)
    private Repository<TestSuite> testSuiteRepository = new PingingDatastoreRepository<>(TestSuite)
    private Repository<TestSuiteDetails> testSuiteDetailsRepository = new PingingDatastoreRepository<>(TestSuiteDetails)
    Gson gson = new Gson()
    HttpClient client = new JsonHttpClient()


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
        def list = testSuiteDetailsRepository.filter(new FilterBuilder().addFilter(new SimpleFilter("testSuiteId","=", testSuiteId)).build())
        if(!list)
            return null
        return list[0]
    }

    TestSuiteDetails updateDetailsFromJenkins(String testSuiteId) {
        TestSuite testSuite = get(testSuiteId)
        TestSuiteDetails foundDetails = getSuiteDetails(testSuiteId)
        try{
            TestSuiteDetails details = getJenkinsLastRunInfo(testSuite, foundDetails)

            if(!details.lastRunSuccess){
                new ErrorsController().createError(new TestError(source: testSuite.source, message: "Test run unsuccessful for test suite id: ${details.testSuiteId}", date: new Date()))
            }

            return details
        }catch(Exception e){
            log.warning("Unable to update test suite details: ${e.message}")
        }
        return foundDetails
    }

    private TestSuiteDetails getJenkinsLastRunInfo(TestSuite testSuite, TestSuiteDetails foundDetails) {
        String jenkinsName = getJenkinsName(testSuite)
        String json = client.get("https://trevorism-build.eastus.cloudapp.azure.com/job/$jenkinsName/lastBuild/api/json")
        JenkinsRunResult result = gson.fromJson(json, JenkinsRunResult)
        foundDetails.lastRunDate = new Date(result.timestamp)
        foundDetails.lastRunSuccess = result.result == "SUCCESS"
        return testSuiteDetailsRepository.update(foundDetails.id, foundDetails)
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
                return "https://trevorism-build.eastus.cloudapp.azure.com/job/unit-${testSuite.source}/Unit_20tests"
            case TestSuiteKind.JAVASCRIPT:
            case TestSuiteKind.CUCUMBER:
            case TestSuiteKind.WEB:
            case TestSuiteKind.POWERSHELL:
            case TestSuiteKind.SELENIUM:
                return null
        }
        return null
    }


    private String getJenkinsName(TestSuite testSuite) {
        switch (testSuite.kind){
            case "unit": return "unit-${testSuite.source}"
            case "cucumber": return "acceptance-${testSuite.source}"
            case "javascript": return "karma-${testSuite.source}"
            default: throw new RuntimeException("Unable to compute test suite name")
        }
    }
}
