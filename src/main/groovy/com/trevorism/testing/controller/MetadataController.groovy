package com.trevorism.testing.controller

import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.service.DefaultTestMetadataService
import com.trevorism.testing.service.TestMetadataService
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

@Api("Metadata Operations")
@Path("metadata")
class MetadataController {

    TestMetadataService testMetadataService = new DefaultTestMetadataService()

    @ApiOperation(value = "Lists all test metadata")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestMetadata> listMetadata() {
        testMetadataService.listMetadata()
    }

    @ApiOperation(value = "Gets metadata from a metadata id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestMetadata getMetadata(@PathParam("id") String id) {
        testMetadataService.getMetadata(id)
    }

    @ApiOperation(value = "Creates a new metadata")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestMetadata saveMetadata(TestMetadata metadata) {
        testMetadataService.saveMetadata(metadata)
    }

    @ApiOperation(value = "Remove metadata from a metadata id")
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestMetadata removeMetadata(@PathParam("id") String id) {
        testMetadataService.removeMetadata(id)
    }

    @ApiOperation(value = "Updates metadata from a metadata id")
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestMetadata updateMetadata(@PathParam("id") String id, TestMetadata metadata) {
        testMetadataService.updateMetadata(id, metadata)
    }
}
