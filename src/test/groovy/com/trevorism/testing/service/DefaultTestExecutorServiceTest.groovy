package com.trevorism.testing.service

import org.junit.Test

class DefaultTestExecutorServiceTest {

    @Test
    void testParseJobNameFromGitUrl() {
        String jobName = DefaultTestExecutorService.parseJobNameFromGitUrl("https://github.com/trevorism/http-utils.git")
        assert jobName == "http-utils"

        jobName = DefaultTestExecutorService.parseJobNameFromGitUrl("https://github.com/trevorism/predict.git")
        assert jobName == "predict"
    }
}
