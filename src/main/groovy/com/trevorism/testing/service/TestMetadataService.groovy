package com.trevorism.testing.service


import com.trevorism.testing.model.TestMetadata

interface TestMetadataService {

    TestMetadata lookupMetadata(String id)
    TestMetadata getMetadata(String id)
    TestMetadata saveMetadata(TestMetadata metadata)
    TestMetadata removeMetadata(String id)
    TestMetadata updateMetadata(String id, TestMetadata metadata)
}