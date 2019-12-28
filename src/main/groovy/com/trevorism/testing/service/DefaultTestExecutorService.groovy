package com.trevorism.testing.service

import com.trevorism.event.EventProducer
import com.trevorism.event.PingingEventProducer
import com.trevorism.http.util.ResponseUtils
import com.trevorism.testing.model.CinvokeJob
import com.trevorism.testing.model.TestSuite

import java.util.logging.Logger

class DefaultTestExecutorService implements TestExecutorService {

    EventProducer<CinvokeJob> eventhubProducer = new PingingEventProducer<>()
    private static final Logger log = Logger.getLogger(DefaultTestExecutorService.class.name)

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        TestSuite.TestSuiteType type = testSuite.getTestSuiteType().toUpperCase() as TestSuite.TestSuiteType
        switch(type){
            case TestSuite.TestSuiteType.JUNIT:
                return invokeJunitTests(testSuite)
            case TestSuite.TestSuiteType.KARMA:
                return invokeKarmaTests(testSuite)
            case TestSuite.TestSuiteType.CUCUMBER:
                return invokeCucumberTests(testSuite)
            case TestSuite.TestSuiteType.WEB:
                return invokeWebTests(testSuite)
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

    boolean invokeWebTests(TestSuite testSuite) {
        try{
            String responseJson = ResponseUtils.getEntity client.get(testSuite.location, ["Authorization": passwordProvider.password])
            log.info("HTTP GET on $testSuite.location")
            if(responseJson && responseJson != "false")
                return true
        }catch(Exception e) {
            log.warning("Exception invoking job ${e.message}")
        }
        return false
    }

    private static String parseJobNameFromGitUrl(String gitUrl) {
        int indexOfDotGit = gitUrl.indexOf(".git")
        int lengthOfPrefix = "https://github.com/trevorism/".length()
        return gitUrl[lengthOfPrefix..indexOfDotGit-1]
    }

    private void postToCinvoke(String jobName) {
        String correlationId = UUID.randomUUID().toString()
        eventhubProducer.sendEvent("cinvoke", new CinvokeJob(jobName), correlationId)
    }

    private boolean invokeTestJob(String prefix, TestSuite testSuite) {
        String jobName = "$prefix-${parseJobNameFromGitUrl(testSuite.location)}"
        log.info("CInvoking job $jobName")
        postToCinvoke(jobName)
        return true
    }
}
