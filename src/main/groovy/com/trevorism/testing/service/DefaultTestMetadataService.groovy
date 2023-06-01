package com.trevorism.testing.service

import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestMetadata

@jakarta.inject.Singleton
class DefaultTestMetadataService implements TestMetadataService{

    Repository<TestMetadata> testMetadataRepository

    DefaultTestMetadataService(SecureHttpClient secureHttpClient){
        testMetadataRepository = new FastDatastoreRepository<>(TestMetadata, secureHttpClient)
    }

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
