package com.trevorism.testing.controller

import com.trevorism.testing.model.TestSuite
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Test Collection Operations")
@Path("collection")
class TestCollectionController {

    @ApiOperation(value = "Creates a new TestCollection")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite createCollection(TestSuite collection) {
        return null
    }

    @ApiOperation(value = "Adds a Test, TestSuite or TestCollection to a TestCollection, identified by ID")
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestSuite addToCollection(@PathParam("id") String collectionId, TestSuite collection) {
        return null
    }

    @ApiOperation(value = "Get a list of all collections")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestSuite> list() {
        return null
    }

    @ApiOperation(value = "Gets a test collection by id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestSuite get(@PathParam("id") String collectionId) {
        return null
    }
}
