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
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Test Suite Operations")
@Path("suite")
class TestSuiteController {

    TestSuiteService testSuiteService = new DefaultTestSuiteService()
    TestExecutorService testExecutorService = new DefaultTestExecutorService()
    GithubClient githubClient = new DefaultGithubClient()

    @ApiOperation(value = "Creates a new test suite **Secure")
    @Secure(Roles.USER)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite registerSuite(TestSuite testSuite) {
        testSuiteService.create(testSuite)
    }

    @ApiOperation(value = "Lists all test suites **Secure")
    @Secure(Roles.USER)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestSuite> listAllTestSuites() {
        testSuiteService.list()
    }

    @ApiOperation(value = "Gets test suites based on the service name **Secure")
    @Secure(Roles.USER)
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite getTestSuite(@PathParam("id") String id) {
        testSuiteService.get(id)
    }

    @ApiOperation(value = "Invoke the test suite **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite invokeTestSuite(@PathParam("id") String id) {
        TestSuite testSuite = testSuiteService.get(id)
        testExecutorService.executeTestSuite(testSuite)
        return testSuite
    }

    @ApiOperation(value = "Remove registered test suite **Secure")
    @Secure(Roles.USER)
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite removeTestSuite(@PathParam("id") String id) {
        testSuiteService.delete(id)
    }

    @ApiOperation(value = "Updates registered test suite **Secure")
    @Secure(Roles.USER)
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite updateTestSuite(@PathParam("id") String id, TestSuite testSuite) {
        testSuiteService.update(id, testSuite)
    }

    @ApiOperation(value = "Updates test suite based on last execution **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite updateTestSuite(TestSuite testSuite) {
        WorkflowStatus status = githubClient.getWorkflowStatus(testSuite.source, new WorkflowRequest(unitTest: testSuite?.kind?.toLowerCase() == "unit"))
        TestSuite updated = testExecutorService.updateTestSuiteFromStatus(testSuite, status)
        if(!updated.lastRunSuccess){
            new ErrorsController().createError(new TestError(source: updated.source, message: "Failing test suite ${updated.id} - ${updated.name}", date: updated.lastRunDate))
        }
        updateTestSuite(updated.id, updated)

    }
}
