package com.trevorism.testing.service

import com.google.gson.Gson
import com.trevorism.schedule.DefaultScheduleService
import com.trevorism.schedule.ScheduleService
import com.trevorism.schedule.factory.DefaultScheduledTaskFactory
import com.trevorism.schedule.factory.EndpointSpec
import com.trevorism.schedule.factory.ScheduledTaskFactory
import com.trevorism.schedule.model.HttpMethod
import com.trevorism.schedule.model.ScheduledTask
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.WorkflowRequest
import com.trevorism.testing.model.WorkflowStatus

import java.time.Instant

class DefaultTestExecutorService implements TestExecutorService {

    public static final int CHECK_TEST_RESULTS_MINUTES = 15
    private Gson gson = new Gson()
    private GithubClient githubClient = new DefaultGithubClient()
    private ScheduleService scheduleService = new DefaultScheduleService()

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        String testType = testSuite.kind.toLowerCase()

        if(testType == "unit" || testType == "cucumber"){
            boolean result = githubClient.invokeWorkflow(testSuite.source, new WorkflowRequest(unitTest: testType == "unit"))
            scheduleTestSuiteResultCheck(testSuite)
            return result
        }

        return false
    }

    private void scheduleTestSuiteResultCheck(TestSuite testSuite) {
        Instant tenMinutesFromNow = Instant.now().plusSeconds(60 * CHECK_TEST_RESULTS_MINUTES)
        String json = gson.toJson(testSuite)
        ScheduledTaskFactory scheduledTaskFactory = new DefaultScheduledTaskFactory()
        ScheduledTask scheduledTask = scheduledTaskFactory.createImmediateTask("${testSuite.name}_${tenMinutesFromNow}", tenMinutesFromNow.toDate(), new EndpointSpec("https://testing.trevorism.com/api/suite", HttpMethod.PUT, json))
        scheduleService.create(scheduledTask)
    }

    @Override
    TestSuite updateTestSuiteFromStatus(TestSuite testSuite, WorkflowStatus workflowStatus) {
        testSuite.lastRunDate = workflowStatus.updatedAt
        testSuite.lastRunSuccess = workflowStatus.result == "success"
        testSuite.lastRuntimeSeconds = Math.abs(workflowStatus.createdAt.getTime() - workflowStatus.updatedAt.getTime()) / 1000
        return testSuite
    }


}
