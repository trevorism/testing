package com.trevorism.testing.service

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

}
