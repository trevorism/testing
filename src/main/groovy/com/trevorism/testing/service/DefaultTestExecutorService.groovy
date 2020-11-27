package com.trevorism.testing.service

import com.trevorism.event.EventProducer
import com.trevorism.event.PingingEventProducer
import com.trevorism.http.util.ResponseUtils
import com.trevorism.testing.model.CinvokeJob
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteKind

import java.util.logging.Logger

class DefaultTestExecutorService implements TestExecutorService {

    EventProducer<CinvokeJob> eventhubProducer = new PingingEventProducer<>()
    private static final Logger log = Logger.getLogger(DefaultTestExecutorService.class.name)

    String getLastTestResultUrl(TestSuite testSuite) {
        return "http://trevorism-build.eastus.cloudapp.azure.com/job/${testSuite.kind.toLowerCase()}-${testSuite.source}/"
    }

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        TestSuiteKind kind = testSuite.kind.toUpperCase() as TestSuiteKind
        switch(kind){
            case TestSuiteKind.UNIT:
                return invokeJunitTests(testSuite)
            case TestSuiteKind.KARMA:
                return invokeKarmaTests(testSuite)
            case TestSuiteKind.CUCUMBER:
                return invokeCucumberTests(testSuite)
            case TestSuiteKind.WEB:
                return invokeWebTests(testSuite)
            case TestSuiteKind.POWERSHELL:
                return invokePowershellTests(testSuite)
            case TestSuiteKind.SELENIUM:
                return invokeSeleniumTests(testSuite)
        }
        return false
    }

    boolean invokeJunitTests(TestSuite testSuite) {
        try{
            return invokeTestJob("unit", testSuite)
        }catch(Exception e) {
            log.warning("Exception invoking job ${e.message}")
            return false
        }
    }

    boolean invokeKarmaTests(TestSuite testSuite) {
        try{
            return invokeTestJob("karma", testSuite)
        }catch(Exception e) {
            log.warning("Exception invoking job ${e.message}")
            return false
        }
    }

    boolean invokeCucumberTests(TestSuite testSuite) {
        try{
            return invokeTestJob("acceptance", testSuite)
        }catch(Exception e) {
            log.warning("Exception invoking job ${e.message}")
            return false
        }
    }


    private void postToCinvoke(String jobName) {
        String correlationId = UUID.randomUUID().toString()
        eventhubProducer.sendEvent("cinvoke", new CinvokeJob(jobName), correlationId)
    }

    private boolean invokeTestJob(String prefix, TestSuite testSuite) {
        String jobName = "$prefix-${testSuite.source}"
        log.info("CInvoking job $jobName")
        postToCinvoke(jobName)
        return true
    }


    boolean invokeWebTests(TestSuite testSuite) {
        return false
    }

    private boolean invokePowershellTests(TestSuite testSuite) {
        //Not implemented
        false
    }

    private boolean invokeSeleniumTests(TestSuite testSuite) {
        //Not implemented
        false
    }

}
