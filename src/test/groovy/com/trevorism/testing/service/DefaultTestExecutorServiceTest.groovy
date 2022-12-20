package com.trevorism.testing.service

import com.trevorism.schedule.ScheduleService
import com.trevorism.schedule.model.ScheduledTask
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.WorkflowStatus
import org.junit.Test

import java.time.Instant

class DefaultTestExecutorServiceTest {

    @Test
    void testExecuteTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        testExecutorService.githubClient = {x,y -> true} as GithubClient
        testExecutorService.scheduleService = {x -> new ScheduledTask()} as ScheduleService
        assert testExecutorService.executeTestSuite(new TestSuite(id: "4731055747629056", name: "acceptance_endpoint-tester", source: "endpoint-tester", kind: "cucumber"))
    }

    @Test
    void testUpdateTestSuiteFromStatus() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        Instant now = Instant.now()
        def testSuite = testExecutorService.updateTestSuiteFromStatus(new TestSuite(id: "4792240945758208", name: "acceptance_action", source: "action", kind: "cucumber"), new WorkflowStatus(result: "queued", createdAt: now.plusSeconds(-10).toDate(), updatedAt: now.toDate()))
        assert now.toDate() == testSuite.lastRunDate
        assert !testSuite.lastRunSuccess
        assert testSuite.lastRuntimeSeconds == 10
    }
}
