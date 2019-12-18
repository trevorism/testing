package com.trevorism.testing.service

import com.trevorism.http.headers.HeadersHttpClient
import com.trevorism.http.headers.HeadersJsonHttpClient
import com.trevorism.http.util.ResponseUtils
import com.trevorism.secure.PasswordProvider
import com.trevorism.testing.model.TestSuite

import java.util.logging.Logger

class DefaultTestExecutorService implements TestExecutorService {

    HeadersHttpClient client = new HeadersJsonHttpClient()
    PasswordProvider passwordProvider = new PasswordProvider()
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
            String jobName = "unit-${parseJobNameFromGitUrl(testSuite.location)}"
            log.info("CInvoking job $jobName")
            postToCinvoke(jobName)
            return true
        }catch(Exception e) {
            log.warning("Exception invoking job ${e.message}")
            return false
        }
    }

    boolean invokeKarmaTests(TestSuite testSuite) {
        invokeJunitTests(testSuite)
    }

    boolean invokeCucumberTests(TestSuite testSuite) {
        try{
            String jobName = parseJobNameFromGitUrl(testSuite.location)
            log.info("CInvoking job acceptance-$jobName")
            postToCinvoke("acceptance-$jobName")
            return true
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
        String urlToPost = "http://cinvoke.datastore.trevorism.com/job/$jobName/build"
        def response = client.post(urlToPost, "{}", ["Authorization": passwordProvider.password])
        ResponseUtils.closeSilently(response)
    }
}
