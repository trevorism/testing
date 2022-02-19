package com.trevorism.testing.controller

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.sorting.Sort
import com.trevorism.data.model.sorting.SortBuilder
import com.trevorism.event.EventProducer
import com.trevorism.event.PingingEventProducer
import com.trevorism.testing.model.TestError
import com.trevorism.threshold.model.Alert
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
import java.time.Instant
import java.time.temporal.ChronoUnit

@Api("Error Operations")
@Path("error")
class ErrorsController {

    private Repository<TestError> errorRepository = new PingingDatastoreRepository<>(TestError)

    @ApiOperation(value = "Lists all errors")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TestError> getLastErrors() {
        errorRepository.sort(new SortBuilder().addSort(new Sort("date",true)).build())
    }

    @ApiOperation(value = "Gets error from an id")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestError getError(@PathParam("id") String id) {
        errorRepository.get(id)
    }

    @ApiOperation(value = "Creates a new error")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TestError createError(TestError error) {
        errorRepository.create(error)
    }

    @ApiOperation(value = "Remove an error")
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestError removeError(@PathParam("id") String id) {
        errorRepository.delete(id)
    }


    @ApiOperation(value = "Sends an alert if there are any active errors")
    @GET
    @Path("alert")
    @Produces(MediaType.APPLICATION_JSON)
    boolean checkForErrors() {
        def list = errorRepository.list()
        if(list){
            EventProducer<Alert> producer = new PingingEventProducer<>()
            producer.sendEvent("alert", new Alert(subject: "Error Report", body: "There are errors to resolve, https://testing.trevorism.com/api/error"))
            return true
        }
        return false
    }

    @ApiOperation(value = "")
    @DELETE
    @Path("scrub")
    @Produces(MediaType.APPLICATION_JSON)
    boolean cleanOldErrors() {
        def list = errorRepository.list()
        def date = Instant.now().minus(7, ChronoUnit.DAYS).toDate()
        def errors = list.findAll{ it.date < date}
        if(!errors){
            return false
        }
        errors.each {
            removeError(it.id)
        }
        return true
    }
}
