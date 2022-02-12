package com.trevorism.testing.service

import com.trevorism.schedule.DefaultScheduleService
import com.trevorism.schedule.ScheduleService
import com.trevorism.schedule.factory.DefaultScheduledTaskFactory
import com.trevorism.schedule.factory.EndpointSpec
import com.trevorism.schedule.factory.ScheduledTaskFactory
import com.trevorism.schedule.model.HttpMethod
import com.trevorism.schedule.model.ScheduledTask
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.run.ExecuteTestStrategy
import com.trevorism.testing.service.run.ExecuteTestStrategyFactory

import java.time.Instant

class DefaultTestExecutorService implements TestExecutorService {

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        ExecuteTestStrategy strategy = ExecuteTestStrategyFactory.create(testSuite.kind)
        boolean success = strategy?.runTest(testSuite)

        if(success){
            scheduleDetailsUpdate(testSuite)
        }

        return success
    }


    void scheduleDetailsUpdate(TestSuite testSuite) {
        ScheduledTaskFactory factory = new DefaultScheduledTaskFactory()
        EndpointSpec endpointSpec = new EndpointSpec("https://testing.trevorism.com/api/suite/$testSuite.id/detail", HttpMethod.PUT, "{}")
        ScheduledTask st = factory.createImmediateTask("detailupdate-${testSuite.id}", Instant.now().plus(60 * 10).toDate(), endpointSpec)
        ScheduleService service = new DefaultScheduleService()
        service.create(st)
    }
}
