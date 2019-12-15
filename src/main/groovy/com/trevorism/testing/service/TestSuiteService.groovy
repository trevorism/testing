package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite

interface TestSuiteService {

    TestSuite createCollection(TestSuite collection)
    List<TestSuite> list()
    TestSuite get(String collectionId)
    TestSuite delete(String collectionId)
}