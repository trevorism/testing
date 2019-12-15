package com.trevorism.testing.service

import com.trevorism.testing.model.HasTests
import com.trevorism.testing.model.TestMetadata

import java.util.concurrent.CompletableFuture

interface TestExecutorService {

    boolean executeTestSuite(HasTests tests)
    CompletableFuture<Boolean> executeTestSuiteAsync(HasTests tests)

}