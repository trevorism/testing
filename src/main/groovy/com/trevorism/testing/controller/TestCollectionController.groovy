package com.trevorism.testing.controller

import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.DefaultTestSuiteService
import com.trevorism.testing.service.TestSuiteService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Test Collection Operations")
@Path("collection")
class TestCollectionController {

    TestSuiteService service = new DefaultTestSuiteService()

    @ApiOperation(value = "Creates a new TestCollection")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite createCollection(TestSuite collection) {
        service.createCollection(collection)
    }

    @ApiOperation(value = "Get a list of all collections")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestSuite> list() {
        service.list()
    }

    @ApiOperation(value = "Gets a test collection by id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite get(@PathParam("id") String collectionId) {
        service.get(collectionId)
    }

    @ApiOperation(value = "Gets a test collection by id")
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite delete(@PathParam("id") String collectionId) {
        service.delete(collectionId)
    }
}
