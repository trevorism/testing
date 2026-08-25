package com.trevorism.testing.service

import com.trevorism.ClasspathBasedPropertiesProvider
import com.trevorism.PropertiesProvider
import com.trevorism.http.BlankHttpClient
import com.trevorism.http.HttpClient
import com.trevorism.testing.model.TestSuite
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

@jakarta.inject.Singleton
class DefaultHeartbeatService implements HeartbeatService {

    private static final Logger log = LoggerFactory.getLogger(DefaultHeartbeatService)
    private static final int MAX_AGE_HOURS = 192
    static final List<String> MONITORED_SUITE_NAMES = [
            "web_event-tester",
            "web_expiration-tester",
            "acceptance_auth-provider",
            "acceptance_data",
            "acceptance_github",
            "acceptance_list",
            "acceptance_schedule"
    ].asImmutable()

    private PropertiesProvider propertiesProvider = new ClasspathBasedPropertiesProvider()
    private TestSuiteService testSuiteService
    private HttpClient httpClient = new BlankHttpClient()
    private String pingUrl = readPingUrl()

    DefaultHeartbeatService(TestSuiteService testSuiteService) {
        this.testSuiteService = testSuiteService
    }

    private String readPingUrl() {
        try {
            return propertiesProvider.getProperty("apiKey")
        } catch (Exception e) {
            log.warn("Unable to read the heartbeat ping url", e)
            return null
        }
    }

    @Override
    boolean checkSuiteFreshness() {
        boolean fresh = everyMonitoredSuiteIsFresh()
        sendPing(fresh)
        return fresh
    }

    private boolean everyMonitoredSuiteIsFresh() {
        List<TestSuite> monitoredSuites = testSuiteService.list().findAll { it.name in MONITORED_SUITE_NAMES }

        List<String> missing = MONITORED_SUITE_NAMES - monitoredSuites.collect { it.name }
        if (missing) {
            log.error("Monitored test suites are no longer registered: ${missing}")
            return false
        }

        List<TestSuite> stale = monitoredSuites.findAll { !isFresh(it) }
        if (stale) {
            log.error("Test suites have not run in ${MAX_AGE_HOURS} hours: ${stale.collect { it.name }}")
            return false
        }

        log.info("All ${monitoredSuites.size()} monitored test suites are fresh")
        return true
    }

    private static boolean isFresh(TestSuite testSuite) {
        if (!testSuite.lastRunDate) {
            return false
        }
        return testSuite.lastRunDate.after(Date.from(Instant.now().minus(MAX_AGE_HOURS, ChronoUnit.HOURS)))
    }

    private void sendPing(boolean fresh) {
        if (!pingUrl) {
            log.warn("No heartbeat ping url is configured, skipping the ping")
            return
        }
        try {
            httpClient.get(fresh ? pingUrl : "${pingUrl}/fail")
        } catch (Exception e) {
            log.error("Failed to send the heartbeat ping", e)
        }
    }
}
