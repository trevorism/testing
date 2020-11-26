package com.trevorism.testing.controller

import com.trevorism.testing.model.TestLocator
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.DefaultTestSuiteService
import com.trevorism.testing.service.TestSuiteService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Test Results Operations")
class TestResultController {

    TestSuiteService testSuiteService = new DefaultTestSuiteService()

    @ApiOperation(value = "Gets results based on the locator")
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<TestSuite> findTests(TestLocator testLocator) {

    }

    @ApiOperation(value = "Gets results based on the locator")
    @POST
    @Path("execution")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Boolean> createResults() {

    }

}
