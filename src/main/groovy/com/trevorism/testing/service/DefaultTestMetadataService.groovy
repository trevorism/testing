package com.trevorism.testing.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.testing.model.TestMetadata

class DefaultTestMetadataService implements TestMetadataService{

    Repository<TestMetadata> testMetadataRepository = new PingingDatastoreRepository<>(TestMetadata.class)

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
}
