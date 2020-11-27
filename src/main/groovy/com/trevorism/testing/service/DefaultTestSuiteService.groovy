package com.trevorism.testing.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteKind

class DefaultTestSuiteService implements TestSuiteService {

    Repository<TestSuite> testSuiteRepository = new PingingDatastoreRepository<>(TestSuite)

    @Override
    TestSuite create(TestSuite testSuite) {
        validateInput(testSuite)
        testSuite = updateInvocationParams(testSuite)
        testSuiteRepository.create(testSuite)
    }

    @Override
    List<TestSuite> list() {
        testSuiteRepository.list()
    }

    @Override
    TestSuite get(String id) {
        testSuiteRepository.get(id)
    }

    @Override
    TestSuite delete(String id) {
        testSuiteRepository.delete(id)
    }

    @Override
    TestSuite update(String id, TestSuite testSuite) {
        testSuiteRepository.update(id, testSuite)
    }

    void validateInput(TestSuite testSuite) {
        if (!testSuite || !testSuite.name) {
            throw new RuntimeException("Invalid test suite, must have a name")
        }
        if (!testSuite.source) {
            throw new RuntimeException("Invalid test suite, must come from some source")
        }
        try {
            TestSuiteKind.valueOf(testSuite.kind?.toUpperCase())
        } catch (Exception e) {
            throw new RuntimeException("Invalid test suite, must be a valid kind", e)
        }
    }

    TestSuite updateInvocationParams(TestSuite testSuite) {
        null
    }
}
