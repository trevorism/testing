package com.trevorism.testing.controller

import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteDetails
import com.trevorism.testing.service.DefaultTestExecutorService
import com.trevorism.testing.service.DefaultTestSuiteService
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

    @ApiOperation(value = "Creates a new test suite")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite registerSuite(TestSuite testSuite) {
        testSuiteService.create(testSuite)
    }

    @ApiOperation(value = "Lists all test suites")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestSuite> listAllTestSuites() {
        testSuiteService.list()
    }

    @ApiOperation(value = "Gets test suites based on the service name")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite getTestSuite(@PathParam("id") String id) {
        testSuiteService.get(id)
    }

    @ApiOperation(value = "Invoke the test suite")
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuiteDetails invokeTestSuite(@PathParam("id") String id) {
        TestSuite testSuite = testSuiteService.get(id)
        testExecutorService.executeTestSuite(testSuite)
        getTestSuiteDetails(id)
    }

    @ApiOperation(value = "Gets test suite details")
    @GET
    @Path("{id}/detail")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuiteDetails getTestSuiteDetails(@PathParam("id") String id) {
        testSuiteService.getSuiteDetails(id)
    }

    @ApiOperation(value = "Remove registered test suite")
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite removeMetadata(@PathParam("id") String id) {
        testSuiteService.delete(id)
    }

    @ApiOperation(value = "Updates registered test suite")
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite updateMetadata(@PathParam("id") String id, TestSuite testSuite) {
        testSuiteService.update(id, testSuite)
    }

    @ApiOperation(value = "Updates test suite details")
    @PUT
    @Path("{id}/detail")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuiteDetails updateTestSuiteDetails(@PathParam("id") String id) {
        return testSuiteService.updateDetailsFromJenkins(id)
    }
}
