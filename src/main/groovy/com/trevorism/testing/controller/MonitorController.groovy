package com.trevorism.testing.controller

import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.testing.service.HeartbeatService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Controller("/api/monitor")
class MonitorController {

    @Inject
    HeartbeatService heartbeatService

    @Tag(name = "Monitor Operations")
    @Operation(summary = "Pings the external monitor when every monitored suite has run within its expected interval **Secure")
    @Secure(value = Roles.USER, allowInternal = true, permissions = "RE")
    @Get(value = "/heartbeat", produces = MediaType.APPLICATION_JSON)
    boolean checkSuiteFreshness() {
        heartbeatService.checkSuiteFreshness()
    }
}
