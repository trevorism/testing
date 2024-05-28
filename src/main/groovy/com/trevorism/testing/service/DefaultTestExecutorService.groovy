package com.trevorism.testing.service


import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.filtering.ComplexFilter
import com.trevorism.data.model.filtering.FilterBuilder
import com.trevorism.data.model.filtering.SimpleFilter
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestEvent
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.WorkflowRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@jakarta.inject.Singleton
class DefaultTestExecutorService implements TestExecutorService {

    private static final Logger log = LoggerFactory.getLogger(DefaultTestExecutorService)
    private GithubClient githubClient
    private Repository<TestSuite> testSuiteRepository

    DefaultTestExecutorService(SecureHttpClient secureHttpClient) {
        githubClient = new DefaultGithubClient(secureHttpClient)
        testSuiteRepository = new FastDatastoreRepository<>(TestSuite, secureHttpClient)
    }

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        String testType = testSuite.kind.toLowerCase()
        boolean result = githubClient.invokeWorkflow(testSuite.source, new WorkflowRequest(workflowInputs: ["TEST_TYPE": testType]))
        return result
    }

    @Override
    TestSuite updateTestSuiteFromEvent(TestEvent testEvent) {
        if(!testEvent.service || !testEvent.kind) {
            log.warn("Test event is missing service or kind")
            return null
        }

        ComplexFilter complexFilter = new FilterBuilder().addFilter(new SimpleFilter("source", "=", testEvent.service), new SimpleFilter("kind", "=", testEvent.kind)).build()
        TestSuite suite = testSuiteRepository.filter(complexFilter).first()
        if (!suite) {
            log.warn("No test suite found for event ${testEvent.kind}_${testEvent.service}")
            return null
        }

        suite.lastRunDate = testEvent.date
        suite.lastRunSuccess = testEvent.success
        suite.lastRuntimeSeconds = (long) (testEvent.durationMillis / 1000)
        return suite
    }


}
