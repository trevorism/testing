package com.trevorism.testing.controller

import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.DefaultTestSuiteService
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

}
