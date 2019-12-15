package com.trevorism.testing.controller

import com.trevorism.testing.model.HasTests
import com.trevorism.testing.model.TestCollection
import com.trevorism.testing.model.TestMetadata
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Test Collection Operations")
@Path("collection")
class TestCollectionController {

    @ApiOperation(value = "Creates a new TestCollection")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestCollection createCollection(TestCollection collection) {
        return null
    }

    @ApiOperation(value = "Adds a Test, TestSuite or TestCollection to a TestCollection, identified by ID")
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestCollection createCollection(@PathParam("id") String collectionId, HasTests collection) {
        return null
    }

    @ApiOperation(value = "Get a list of all collections")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestCollection> list() {
        return null
    }

    @ApiOperation(value = "Gets a test collection by id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestCollection get(@PathParam("id") String collectionId) {
        return null
    }
}
