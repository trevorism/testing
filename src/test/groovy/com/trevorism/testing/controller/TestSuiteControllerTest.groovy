package com.trevorism.testing.controller

import com.trevorism.testing.model.TestEvent
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.TestExecutorService
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Test

class TestSuiteControllerTest {

    @Test
    void testUpdateWebhookAcksWhenSuiteMatched() {
        TestSuiteController controller = new TestSuiteController()
        TestSuite suite = new TestSuite(id: "1", source: "health-dash", kind: "javascript")
        controller.testExecutorService = [updateTestSuiteFromEvent: { e -> suite }] as TestExecutorService

        def response = controller.updateTestSuite(new TestEvent(service: "health-dash", kind: "javascript"))

        assert response.status() == HttpStatus.OK
        assert response.body().id == "1"
    }

    @Test
    void testUpdateWebhookAcksWhenNoSuiteMatched() {
        TestSuiteController controller = new TestSuiteController()
        controller.testExecutorService = [updateTestSuiteFromEvent: { e -> null }] as TestExecutorService

        def response = controller.updateTestSuite(new TestEvent(service: "health-dash", kind: "cucumber"))

        assert response.status() == HttpStatus.OK
        assert response.body() == null
    }
}
