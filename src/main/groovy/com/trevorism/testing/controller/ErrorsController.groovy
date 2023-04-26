package com.trevorism.testing.controller

import com.trevorism.AlertClient
import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.sorting.Sort
import com.trevorism.data.model.sorting.SortBuilder
import com.trevorism.model.Alert
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.model.TestError
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Instant
import java.time.temporal.ChronoUnit

@Controller("/error")
class ErrorsController {

    private Repository<TestError> errorRepository = new PingingDatastoreRepository<>(TestError)

    @Tag(name = "Error Operations")
    @Operation(summary = "Lists all errors")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<TestError> getLastErrors() {
        errorRepository.sort(new SortBuilder().addSort(new Sort("date",true)).build())
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Gets error from an id **Secure")
    @Secure(Roles.USER)
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    TestError getError(String id) {
        errorRepository.get(id)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Creates a new error **Secure")
    @Secure(Roles.USER)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestError createError(@Body TestError error) {
        if(error.date == null)
            error.date = new Date()
        errorRepository.create(error)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Remove an error **Secure")
    @Secure(Roles.USER)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    TestError removeError(String id) {
        errorRepository.delete(id)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Sends an alert if there are any active errors **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @Get(value = "/alert", produces = MediaType.APPLICATION_JSON)
    boolean checkForErrors() {
        def list = errorRepository.list()
        if(list){
            AlertClient alertClient = new AlertClient()
            alertClient.sendAlert(new Alert(subject: "Error Report",
                    body: "There are errors to resolve, https://testing.trevorism.com/api/error"))
            return true
        }
        return false
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Cleanup week long old errors **Secure")
    @Secure(value = Roles.USER, allowInternal = true)
    @Delete(value = "/scrub", produces = MediaType.APPLICATION_JSON)
    boolean cleanOldErrors() {
        def list = errorRepository.list()
        def date = Date.from(Instant.now().minus(7, ChronoUnit.DAYS))
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
