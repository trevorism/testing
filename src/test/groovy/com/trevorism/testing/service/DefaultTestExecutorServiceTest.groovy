package com.trevorism.testing.service

import com.trevorism.data.Repository
import com.trevorism.testing.model.TestError
import com.trevorism.testing.model.TestEvent
import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.model.TestSuite
import org.junit.jupiter.api.Test

import java.time.Instant

class DefaultTestExecutorServiceTest {

    @Test
    void testExecuteTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        testExecutorService.githubClient = { x, y -> true } as GithubClient
        testExecutorService.testMetadataService = {x -> new TestMetadata(disabled: false) } as TestMetadataService
        assert testExecutorService.executeTestSuite(new TestSuite(id: "4731055747629056", name: "acceptance_endpoint-tester", source: "endpoint-tester", kind: "cucumber"))
    }

    @Test
    void testExecuteDisabledTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        testExecutorService.githubClient = { x, y -> true } as GithubClient
        testExecutorService.testMetadataService = {x -> new TestMetadata(disabled: true) } as TestMetadataService
        assert !testExecutorService.executeTestSuite(new TestSuite(id: "4731055747629056", name: "acceptance_endpoint-tester", source: "endpoint-tester", kind: "cucumber"))
    }

    @Test
    void testUpdateTestSuiteFromStatus() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        Instant now = Instant.now()
        def testSuite = testExecutorService.updateTestSuiteFromEvent(new TestEvent())
        assert !testSuite
    }

    @Test
    void testNoErrorCreatedWhenFailureHasZeroTests() {
        DefaultTestExecutorService testExecutorService = new DefaultTestExecutorService()
        boolean errorCreated = false

        TestSuite suite = new TestSuite(id: "1", source: "my-service", kind: "cucumber", lastRunSuccess: false)
        testExecutorService.testSuiteRepository = [
                filter: { cf -> [suite] },
                update: { id, s -> new TestSuite(id: id, source: s.source, kind: s.kind, lastRunSuccess: false) }
        ] as Repository<TestSuite>
        testExecutorService.errorRepository = [
                create: { e -> errorCreated = true; e }
        ] as Repository<TestError>
        testExecutorService.testMetadataService = { id -> new TestMetadata(disabled: false, shouldFail: false) } as TestMetadataService

        TestEvent event = new TestEvent(service: "my-service", kind: "cucumber", success: false, numberOfTests: 0, date: new Date())
        testExecutorService.updateTestSuiteFromEvent(event)

        assert !errorCreated, "No error should be created when numberOfTests == 0"
    }

    @Test
    void testErrorCreatedWhenFailureHasTests() {
        DefaultTestExecutorService testExecutorService = new DefaultTestExecutorService()
        boolean errorCreated = false

        TestSuite suite = new TestSuite(id: "1", source: "my-service", kind: "cucumber", lastRunSuccess: false)
        testExecutorService.testSuiteRepository = [
                filter: { cf -> [suite] },
                update: { id, s -> new TestSuite(id: id, source: s.source, kind: s.kind, lastRunSuccess: false) }
        ] as Repository<TestSuite>
        testExecutorService.errorRepository = [
                create: { e -> errorCreated = true; e }
        ] as Repository<TestError>
        testExecutorService.testMetadataService = { id -> new TestMetadata(disabled: false, shouldFail: false) } as TestMetadataService

        TestEvent event = new TestEvent(service: "my-service", kind: "cucumber", success: false, numberOfTests: 5, date: new Date())
        testExecutorService.updateTestSuiteFromEvent(event)

        assert errorCreated, "An error should be created when numberOfTests > 0 and success is false"
    }

}
