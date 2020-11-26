package com.trevorism.testing.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.testing.model.TestSuite

class DefaultTestSuiteService implements TestSuiteService{

    Repository<TestSuite> testSuiteRepository = new PingingDatastoreRepository<>(TestSuite)

    @Override
    TestSuite create(TestSuite collection) {
        testSuiteRepository.create(collection)
    }

    @Override
    List<TestSuite> list() {
        testSuiteRepository.list()
    }

    @Override
    TestSuite get(String collectionId) {
        testSuiteRepository.get(collectionId)
    }

    @Override
    TestSuite delete(String collectionId) {
        testSuiteRepository.delete(collectionId)
    }

    @Override
    TestSuite update(String collectionId, TestSuite testSuite) {
        testSuiteRepository.update(collectionId, testSuite)
    }
}
