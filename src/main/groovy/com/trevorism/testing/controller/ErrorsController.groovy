package com.trevorism.testing.controller

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.sorting.Sort
import com.trevorism.data.model.sorting.SortBuilder
import com.trevorism.testing.model.Error
import com.trevorism.testing.model.TestMetadata
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Error Operations")
@Path("error")
class ErrorsController {

    private Repository<Error> errorRepository = new PingingDatastoreRepository<>(Error)

    @ApiOperation(value = "Lists all errors")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Error> getLastErrors() {
        errorRepository.sort(new SortBuilder().addSort(new Sort("date",true)).build())
    }

    @ApiOperation(value = "Gets error from an id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Error getError(@PathParam("id") String id) {
        errorRepository.get(id)
    }

    @ApiOperation(value = "Creates a new error")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Error createError(Error error) {
        errorRepository.create(error)
    }

    @ApiOperation(value = "Remove an error")
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Error removeError(@PathParam("id") String id) {
        errorRepository.delete(id)
    }
}
