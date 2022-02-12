package com.trevorism.testing.service

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
        println service.updateDetailsFromJenkins("4854491161559040")

    }
}
