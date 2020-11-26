package com.trevorism.testing.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.model.TestSuiteKind

class DefaultTestMetadataService implements TestMetadataService{

    Repository<TestMetadata> testMetadataRepository = new PingingDatastoreRepository<>(TestMetadata)

    @Override
    List<TestMetadata> listMetadata() {
        testMetadataRepository.list()
    }

    @Override
    TestMetadata getMetadata(String id) {
        testMetadataRepository.get(id)
    }

    @Override
    TestMetadata saveMetadata(TestMetadata metadata) {
        testMetadataRepository.create(metadata)
    }

    @Override
    TestMetadata removeMetadata(String id) {
        testMetadataRepository.delete(id)
    }

    @Override
    TestMetadata updateMetadata(String id, TestMetadata metadata) {
        testMetadataRepository.update(id, metadata)
    }

    @Override
    TestMetadata findOrCreate(String testSource, TestSuiteKind testSuiteKind) {
        TestMetadata testSuiteMetadata = testMetadataRepository.list().find{
            it.testSuiteKind?.toString()?.toLowerCase() == testSuiteKind?.toString()?.toLowerCase() &&
                    it.testSource?.toLowerCase() == testSource?.toLowerCase()
        }

        if(testSuiteMetadata)
            return testSuiteMetadata

        TestMetadata newMetadata = new TestMetadata()
        newMetadata.testSource = testSource
        newMetadata.testSuiteKind = testSuiteKind
        return newMetadata

    }
}
