package com.trevorism.testing.service

import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.TestSuiteKind
import io.micronaut.runtime.http.scope.RequestScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RequestScope
class DefaultTestSuiteService implements TestSuiteService {

    private static final Logger log = LoggerFactory.getLogger(DefaultTestSuiteService)
    private Repository<TestSuite> testSuiteRepository

    DefaultTestSuiteService(SecureHttpClient secureHttpClient) {
        testSuiteRepository = new FastDatastoreRepository<>(TestSuite, secureHttpClient)
    }

    @Override
    TestSuite create(TestSuite testSuite) {
        try {
            validateInput(testSuite)
            TestSuite createdSuite = testSuiteRepository.create(testSuite)
            return createdSuite
        } catch (Exception e) {
            log.error("Failed to create test suite", e)
            throw e
        }
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

    private static void validateInput(TestSuite testSuite) {
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

}
