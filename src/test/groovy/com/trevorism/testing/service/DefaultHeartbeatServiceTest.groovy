package com.trevorism.testing.service

import com.trevorism.http.HttpClient
import com.trevorism.testing.model.TestSuite
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DefaultHeartbeatServiceTest {

    private List<String> pingedUrls = []

    @Test
    void testFreshSuitesAreFresh() {
        assert createService(allMonitoredSuites(hoursAgo(100))).checkSuiteFreshness()
    }

    @Test
    void testSuiteAtTheEdgeOfTheWindowIsFresh() {
        assert createService(allMonitoredSuites(hoursAgo(191))).checkSuiteFreshness()
    }

    @Test
    void testStaleSuiteIsNotFresh() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.last().lastRunDate = hoursAgo(400)

        assert !createService(suites).checkSuiteFreshness()
    }

    @Test
    void testSuiteWithoutRunDateIsNotFresh() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.first().lastRunDate = null

        assert !createService(suites).checkSuiteFreshness()
    }

    @Test
    void testUnregisteredMonitoredSuiteIsNotFresh() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.remove(0)

        assert !createService(suites).checkSuiteFreshness()
    }

    @Test
    void testUnmonitoredSuitesAreIgnored() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites << new TestSuite(name: "unit_testing", kind: "unit", lastRunDate: hoursAgo(5000))

        assert createService(suites).checkSuiteFreshness()
    }

    @Test
    void testFreshSuitesPingSuccess() {
        createService(allMonitoredSuites(hoursAgo(100))).checkSuiteFreshness()

        assert pingedUrls == ["https://ping.invalid/uuid"]
    }

    @Test
    void testStaleSuitesPingFailure() {
        List<TestSuite> suites = allMonitoredSuites(hoursAgo(100))
        suites.last().lastRunDate = hoursAgo(400)

        createService(suites).checkSuiteFreshness()

        assert pingedUrls == ["https://ping.invalid/uuid/fail"]
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
        service.@pingUrl = "https://ping.invalid/uuid"
        service.@httpClient = [get: { String url -> pingedUrls << url; "OK" }] as HttpClient
        return service
    }

    private static List<TestSuite> allMonitoredSuites(Date lastRunDate) {
        DefaultHeartbeatService.MONITORED_SUITE_NAMES.collect {
            new TestSuite(name: it, kind: "web", source: it, lastRunDate: lastRunDate)
        }
    }

    private static Date hoursAgo(int hours) {
        Date.from(Instant.now().minus(hours, ChronoUnit.HOURS))
    }
}
