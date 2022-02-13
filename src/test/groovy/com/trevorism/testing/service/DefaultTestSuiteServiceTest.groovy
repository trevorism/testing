package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite
import org.junit.Test

class DefaultTestSuiteServiceTest {
    void testCreate() {
    }

    @Test
    void testList() {
        DefaultTestSuiteService service = new DefaultTestSuiteService()
        def list = service.list()
        list.each {
            println it
        }
    }

    void testGet() {
    }

    void testDelete() {
    }

    void testUpdate() {
    }

    void testGetSuiteDetails() {
    }

    @Test
    void testGetJenkinsLastRunInfo(){
        DefaultTestSuiteService service = new DefaultTestSuiteService()
        TestSuite suite = service.get("5069459240779776")
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        testExecutorService.executeTestSuite(suite)

    }
}
