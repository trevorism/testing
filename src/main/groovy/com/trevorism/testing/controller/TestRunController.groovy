package com.trevorism.testing.controller

import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.DefaultTestExecutorService
import com.trevorism.testing.service.TestExecutorService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Api("Test Run Operations")
@Path("testrun")
class TestRunController {

    TestExecutorService testService = new DefaultTestExecutorService()

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Invokes a named test suite")
    boolean invokeTests(TestSuite tests) {
        return testService.executeTestSuite(tests)
    }

}
