package com.trevorism.testing.controller

import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.model.TestError
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.WorkflowRequest
import com.trevorism.testing.model.WorkflowStatus
import com.trevorism.testing.service.DefaultGithubClient
import com.trevorism.testing.service.DefaultTestExecutorService
import com.trevorism.testing.service.DefaultTestSuiteService
import com.trevorism.testing.service.GithubClient
import com.trevorism.testing.service.TestExecutorService
import com.trevorism.testing.service.TestSuiteService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag



@Controller("/api/suite")
class TestSuiteController {

    TestSuiteService testSuiteService = new DefaultTestSuiteService()
    TestExecutorService testExecutorService = new DefaultTestExecutorService()
    GithubClient githubClient = new DefaultGithubClient()

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Creates a new test suite **Secure")
    @Secure(Roles.USER)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestSuite registerSuite(@Body TestSuite testSuite) {
        testSuiteService.create(testSuite)
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Lists all test suites **Secure")
    @Secure(Roles.USER)
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<TestSuite> listAllTestSuites() {
        testSuiteService.list()
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Gets test suites based on the service name **Secure")
    @Secure(Roles.USER)
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    TestSuite getTestSuite(String id) {
        testSuiteService.get(id)
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Invoke the test suite **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @Post(value = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestSuite invokeTestSuite(String id) {
        TestSuite testSuite = testSuiteService.get(id)
        boolean result = testExecutorService.executeTestSuite(testSuite)
        if(!result){
            throw new RuntimeException("Unable to invoke test suite")
        }

        return testSuite
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Remove registered test suite **Secure")
    @Secure(Roles.USER)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    TestSuite removeTestSuite(String id) {
        testSuiteService.delete(id)
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Updates registered test suite **Secure")
    @Secure(Roles.USER)
    @Put(value = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestSuite updateTestSuite(String id, @Body TestSuite testSuite) {
        testSuiteService.update(id, testSuite)
    }

    @Tag(name = "Test Suite Operations")
    @Operation(summary = "Updates test suite based on last execution **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @Put(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestSuite updateTestSuite(@Body TestSuite testSuite) {
        WorkflowStatus status = githubClient.getWorkflowStatus(testSuite.source, new WorkflowRequest(testType: testSuite?.kind))
        TestSuite updated = testExecutorService.updateTestSuiteFromStatus(testSuite, status)
        if(!updated.lastRunSuccess){
            new ErrorsController().createError(new TestError(source: updated.source, message: "Failing test suite ${updated.id} - ${updated.name}", date: updated.lastRunDate))
        }
        updateTestSuite(updated.id, updated)

    }
}
