package com.trevorism.testing.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.https.SecureHttpClient
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

@jakarta.inject.Singleton
class DefaultTestExecutorService implements TestExecutorService {

    public static final int CHECK_TEST_RESULTS_MINUTES = 15
    private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()
    private GithubClient githubClient
    private ScheduleService scheduleService

    DefaultTestExecutorService(SecureHttpClient secureHttpClient){
        githubClient = new DefaultGithubClient(secureHttpClient)
        scheduleService = new DefaultScheduleService(secureHttpClient)
    }

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        String testType = testSuite.kind.toLowerCase()
        boolean result = githubClient.invokeWorkflow(testSuite.source, new WorkflowRequest(workflowInputs: ["TEST_TYPE":testType]))
        if (result) {
            scheduleTestSuiteResultCheck(testSuite)
        }
        return result
    }

    private void scheduleTestSuiteResultCheck(TestSuite testSuite) {
        Instant tenMinutesFromNow = Instant.now().plusSeconds(60 * CHECK_TEST_RESULTS_MINUTES)
        String json = gson.toJson(testSuite)
        ScheduledTaskFactory scheduledTaskFactory = new DefaultScheduledTaskFactory()
        ScheduledTask scheduledTask = scheduledTaskFactory.createImmediateTask(
                "${testSuite.name}_${tenMinutesFromNow}", Date.from(tenMinutesFromNow),
                new EndpointSpec("https://testing.trevorism.com/api/suite", HttpMethod.PUT, json))

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
