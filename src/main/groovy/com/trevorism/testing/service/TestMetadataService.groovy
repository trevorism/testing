package com.trevorism.testing.service

import com.trevorism.testing.model.HasTests
import com.trevorism.testing.model.TestMetadata

interface TestMetadataService {

    TestMetadata lookupMetadata(HasTests tests)
    TestMetadata getMetadata(String id)
    TestMetadata saveMetadata(HasTests tests, TestMetadata metadata)
    TestMetadata removeMetadata(String id)
    TestMetadata updateMetadata(String id, TestMetadata metadata)
}