package com.trevorism.testing.controller

import com.trevorism.AlertClient
import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.filtering.SimpleFilter
import com.trevorism.data.model.sorting.Sort
import com.trevorism.data.model.sorting.SortBuilder
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.model.Alert
import com.trevorism.secure.Permissions
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.model.TestError
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

import java.time.Instant
import java.time.temporal.ChronoUnit

@Controller("/api/error")
class ErrorsController {

    private Repository<TestError> errorRepository
    private SecureHttpClient secureHttpClient

    ErrorsController(SecureHttpClient secureHttpClient) {
        this.secureHttpClient = secureHttpClient
        errorRepository = new FastDatastoreRepository<>(TestError, secureHttpClient)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Lists all errors **Secure")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true, permissions = Permissions.READ)
    List<TestError> getLastErrors() {
        errorRepository.sort(new Sort("date", true))
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Gets error from an id **Secure")
    @Secure(value = Roles.USER, allowInternal = true, permissions = Permissions.READ)
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    TestError getError(String id) {
        errorRepository.get(id)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Creates a new error **Secure")
    @Secure(value = Roles.USER, allowInternal = true, permissions = Permissions.CREATE)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestError createError(@Body TestError error) {
        if (error.date == null)
            error.date = new Date()
        errorRepository.create(error)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Remove an error **Secure")
    @Secure(value = Roles.USER, permissions = Permissions.DELETE)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    TestError removeError(String id) {
        errorRepository.delete(id)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Creates a new error")
    @Post(value = "/webhook", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestError createErrorInDefaultTenant(@Body Map genericError) {
        TestError error = new TestError()
        error.source = "webhook"
        error.message = "See details"
        error.details = genericError
        error.date = new Date()
        return new FastDatastoreRepository<>(TestError, new AppClientSecureHttpClient()).create(error)
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Sends an alert if there are any active errors **Secure")
    @Secure(value = Roles.USER, allowInternal = true, permissions = "RE")
    @Get(value = "/alert", produces = MediaType.APPLICATION_JSON)
    boolean checkForErrors() {
        def list = errorRepository.list()
        if (list) {
            AlertClient alertClient = new AlertClient(secureHttpClient)
            String body = "There are ${list.size()} errors to resolve."
            if (list.size() == 1) {
                body = "There is 1 error to resolve."
            }
            alertClient.sendAlert(new Alert(subject: "Error Report", body: body))
            return true
        }
        return false
    }

    @Tag(name = "Error Operations")
    @Operation(summary = "Cleanup week long old errors **Secure")
    @Secure(value = Roles.USER, allowInternal = true, permissions = "RD")
    @Delete(value = "/scrub", produces = MediaType.APPLICATION_JSON)
    boolean cleanOldErrors() {
        def date = Date.from(Instant.now().minus(7, ChronoUnit.DAYS))
        def list = errorRepository.filter(new SimpleFilter("date", "<", date))
        if(!list)
            return false

        list.each {
            removeError(it.id)
        }
        return true
    }
}
