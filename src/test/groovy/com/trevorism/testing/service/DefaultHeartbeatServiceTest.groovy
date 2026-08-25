package com.trevorism.testing.service

import com.trevorism.http.HttpClient
import com.trevorism.testing.model.TestSuite
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DefaultHeartbeatServiceTest {

    private static final List<String> MONITORED_SUITE_NAMES = [
            "web_event-tester",
            "web_expiration-tester",
            "acceptance_auth-provider",
            "acceptance_data",
            "acceptance_github",
            "acceptance_list",
            "acceptance_schedule"
    ]

    private List<String> pingedUrls = []

    @Test
    void testFreshSuitesPingSuccess() {
        DefaultHeartbeatService service = createService(allMonitoredSuites(hoursAgo(100)))

        assert service.checkSuiteFreshness()
        assert pingedUrls == ["https://ping.test/uuid"]
    }

    @Test
    void testStaleSuitePingsFailure() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.last().lastRunDate = hoursAgo(400)
        DefaultHeartbeatService service = createService(suites)

        assert !service.checkSuiteFreshness()
        assert pingedUrls == ["https://ping.test/uuid/fail"]
    }

    @Test
    void testSuiteAtTheEdgeOfTheWindowIsFresh() {
        DefaultHeartbeatService service = createService(allMonitoredSuites(hoursAgo(191)))

        assert service.checkSuiteFreshness()
    }

    @Test
    void testSuiteWithoutRunDatePingsFailure() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.first().lastRunDate = null
        DefaultHeartbeatService service = createService(suites)

        assert !service.checkSuiteFreshness()
        assert pingedUrls == ["https://ping.test/uuid/fail"]
    }

    @Test
    void testUnregisteredMonitoredSuitePingsFailure() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.remove(0)
        DefaultHeartbeatService service = createService(suites)

        assert !service.checkSuiteFreshness()
        assert pingedUrls == ["https://ping.test/uuid/fail"]
    }

    @Test
    void testUnmonitoredSuitesAreIgnored() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites << new TestSuite(name: "unit_testing", kind: "unit", lastRunDate: hoursAgo(5000))
        DefaultHeartbeatService service = createService(suites)

        assert service.checkSuiteFreshness()
    }

    @Test
    void testUnconfiguredPingUrlIsSkipped() {
        DefaultHeartbeatService service = createService(allMonitoredSuites(hoursAgo(100)))
        service.@pingUrl = null

        assert service.checkSuiteFreshness()
        assert pingedUrls.isEmpty()
    }

    @Test
    void testFailedPingDoesNotThrow() {
        DefaultHeartbeatService service = createService(allMonitoredSuites(hoursAgo(100)))
        service.@httpClient = [get: { String url -> throw new RuntimeException("unreachable") }] as HttpClient

        assert service.checkSuiteFreshness()
    }

    private DefaultHeartbeatService createService(List<TestSuite> suites) {
        DefaultHeartbeatService service = new DefaultHeartbeatService([list: { suites }] as TestSuiteService)
        service.@pingUrl = "https://ping.test/uuid"
        service.@httpClient = [get: { String url -> pingedUrls << url; "OK" }] as HttpClient
        return service
    }

    private static List<TestSuite> allMonitoredSuites(Date lastRunDate) {
        MONITORED_SUITE_NAMES.collect { new TestSuite(name: it, kind: "web", source: it, lastRunDate: lastRunDate) }
    }

    private static Date hoursAgo(int hours) {
        Date.from(Instant.now().minus(hours, ChronoUnit.HOURS))
    }
}
