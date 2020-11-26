package com.trevorism.testing.service

import com.trevorism.testing.model.TestMetadata
import com.trevorism.testing.model.TestSuiteKind

interface TestMetadataService {

    List<TestMetadata> listMetadata()

    TestMetadata getMetadata(String id)
    TestMetadata saveMetadata(TestMetadata metadata)
    TestMetadata removeMetadata(String id)
    TestMetadata updateMetadata(String id, TestMetadata metadata)

    TestMetadata findOrCreate(String serviceName, TestSuiteKind testSuiteKind)

}