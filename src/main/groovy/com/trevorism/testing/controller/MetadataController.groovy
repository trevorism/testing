package com.trevorism.testing.controller

import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.service.DefaultTestMetadataService
import com.trevorism.testing.service.TestMetadataService

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/metadata")
class MetadataController {

    TestMetadataService testMetadataService = new DefaultTestMetadataService()

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Lists all test metadata **Secure")
    @Secure(Roles.USER)
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<TestMetadata> listMetadata() {
        testMetadataService.listMetadata()
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Gets metadata from a metadata id **Secure")
    @Secure(Roles.USER)
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TestMetadata getMetadata(String id) {
        testMetadataService.getMetadata(id)
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Creates a new metadata **Secure")
    @Secure(Roles.USER)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestMetadata saveMetadata(@Body TestMetadata metadata) {
        testMetadataService.saveMetadata(metadata)
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Remove metadata from a metadata id **Secure")
    @Secure(Roles.USER)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    TestMetadata removeMetadata(String id) {
        testMetadataService.removeMetadata(id)
    }

}
