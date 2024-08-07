package com.trevorism.testing.controller

import com.trevorism.secure.Permissions
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.service.TestMetadataService

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject


@Controller("/api/metadata")
class MetadataController {

    @Inject
    TestMetadataService testMetadataService

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Lists all test metadata **Secure")
    @Secure(value = Roles.USER, permissions = Permissions.READ)
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<TestMetadata> listMetadata() {
        testMetadataService.listMetadata()
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Gets metadata from a metadata id **Secure")
    @Secure(value = Roles.USER, permissions = Permissions.READ)
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    TestMetadata getMetadata(String id) {
        testMetadataService.getMetadata(id)
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Creates a new metadata **Secure")
    @Secure(value = Roles.USER, permissions = Permissions.CREATE)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    TestMetadata saveMetadata(@Body TestMetadata metadata) {
        testMetadataService.saveMetadata(metadata)
    }

    @Tag(name = "Metadata Operations")
    @Operation(summary = "Remove metadata from a metadata id **Secure")
    @Secure(value = Roles.USER, permissions = Permissions.DELETE)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    TestMetadata removeMetadata(String id) {
        testMetadataService.removeMetadata(id)
    }

}
