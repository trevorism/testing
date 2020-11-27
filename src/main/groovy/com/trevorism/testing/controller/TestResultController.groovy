package com.trevorism.testing.controller

import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.DefaultTestExecutorService
import com.trevorism.testing.service.DefaultTestSuiteService
import com.trevorism.testing.service.TestExecutorService
import com.trevorism.testing.service.TestSuiteService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Test Results Operations")
@Path("result")
class TestResultController {

    TestExecutorService testExecutorService = new DefaultTestExecutorService()
    TestSuiteService testSuiteService = new DefaultTestSuiteService()

    @ApiOperation(value = "Invokes the test and creates a test result")
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean invokeTest(@PathParam("id") String id) {
        TestSuite testSuite = testSuiteService.get(id)
        return testExecutorService.executeTestSuite(testSuite)
    }

    @ApiOperation(value = "Gets a url for test results")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String viewTestResults(@PathParam("id") String id) {
        TestSuite testSuite = testSuiteService.get(id)
        testExecutorService.getLastTestResultUrl(testSuite)
    }
}
