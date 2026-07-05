package com.trevorism.testing.service

import com.trevorism.data.Repository
import com.trevorism.testing.model.TestError
import com.trevorism.testing.model.TestEvent
import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.model.TestSuite
import org.junit.jupiter.api.Test

class DefaultTestExecutorServiceTest {

    private static void setField(Object target, String fieldName, Object value) {
        def field = target.class.getDeclaredField(fieldName)
        field.accessible = true
        field.set(target, value)
    }

    @Test
    void testExecuteTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        setField(testExecutorService, "githubClient", { x, y -> true } as GithubClient)
        setField(testExecutorService, "testMetadataService", {x -> new TestMetadata(disabled: false) } as TestMetadataService)
        assert testExecutorService.executeTestSuite(new TestSuite(id: "4731055747629056", name: "acceptance_endpoint-tester", source: "endpoint-tester", kind: "cucumber"))
    }

    @Test
    void testExecuteDisabledTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        setField(testExecutorService, "githubClient", { x, y -> true } as GithubClient)
        setField(testExecutorService, "testMetadataService", {x -> new TestMetadata(disabled: true) } as TestMetadataService)
        assert !testExecutorService.executeTestSuite(new TestSuite(id: "4731055747629056", name: "acceptance_endpoint-tester", source: "endpoint-tester", kind: "cucumber"))
    }

    @Test
    void testUpdateTestSuiteFromStatus() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        def testSuite = testExecutorService.updateTestSuiteFromEvent(new TestEvent())
        assert !testSuite
    }

    @Test
    void testNoErrorCreatedWhenFailureHasZeroTests() {
        DefaultTestExecutorService testExecutorService = new DefaultTestExecutorService()
        int errorCount = 0

        TestSuite suite = new TestSuite(id: "1", source: "my-service", kind: "cucumber", lastRunSuccess: false)
        setField(testExecutorService, "testSuiteRepository", [
                filter: { cf -> [suite] },
                update: { id, s -> new TestSuite(id: id, source: s.source, kind: s.kind, lastRunSuccess: false) }
        ] as Repository<TestSuite>)
        setField(testExecutorService, "errorRepository", [
                create: { e -> errorCount++; e }
        ] as Repository<TestError>)
        setField(testExecutorService, "testMetadataService", { id -> new TestMetadata(disabled: false, shouldFail: false) } as TestMetadataService)

        TestEvent event = new TestEvent(service: "my-service", kind: "cucumber", success: false, numberOfTests: 0, date: new Date())
        testExecutorService.updateTestSuiteFromEvent(event)

        assert errorCount == 0
    }

    @Test
    void testErrorCreatedWhenFailureHasTests() {
        DefaultTestExecutorService testExecutorService = new DefaultTestExecutorService()
        int errorCount = 0

        TestSuite suite = new TestSuite(id: "1", source: "my-service", kind: "cucumber", lastRunSuccess: false)
        setField(testExecutorService, "testSuiteRepository", [
                filter: { cf -> [suite] },
                update: { id, s -> new TestSuite(id: id, source: s.source, kind: s.kind, lastRunSuccess: false) }
        ] as Repository<TestSuite>)
        setField(testExecutorService, "errorRepository", [
                create: { e -> errorCount++; e }
        ] as Repository<TestError>)
        setField(testExecutorService, "testMetadataService", { id -> new TestMetadata(disabled: false, shouldFail: false) } as TestMetadataService)

        TestEvent event = new TestEvent(service: "my-service", kind: "cucumber", success: false, numberOfTests: 5, date: new Date())
        testExecutorService.updateTestSuiteFromEvent(event)

        assert errorCount == 1
    }

    @Test
    void testDescriptiveErrorCreatedWhenNoSuiteRegistered() {
        DefaultTestExecutorService testExecutorService = new DefaultTestExecutorService()
        TestError created = null

        setField(testExecutorService, "testSuiteRepository", [filter: { cf -> [] }] as Repository<TestSuite>)
        setField(testExecutorService, "errorRepository", [create: { e -> created = e; e }] as Repository<TestError>)

        TestEvent event = new TestEvent(service: "health-dash", kind: "cucumber", success: true, numberOfTests: 8, date: new Date())
        def result = testExecutorService.updateTestSuiteFromEvent(event)

        assert result == null
        assert created != null
        assert created.source == "health-dash"
        assert created.message.contains("cucumber")
        assert created.message.contains("health-dash")
        assert created.details.reason == "unregistered-suite"
        assert created.details.kind == "cucumber"
        assert created.details.success
        assert created.details.numberOfTests == 8
    }

}
