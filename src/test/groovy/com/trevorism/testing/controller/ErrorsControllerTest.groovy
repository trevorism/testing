package com.trevorism.testing.controller

import com.trevorism.data.Repository
import com.trevorism.https.SecureHttpClient
import com.trevorism.testing.model.TestError
import org.junit.jupiter.api.Test

class ErrorsControllerTest {

    @Test
    void testCheckForErrors() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [filter: { cf -> [] }] as Repository<TestError>
        assert !ec.checkForErrors()
    }

    @Test
    void testCheckForErrorsWithRecentErrors() {
        ErrorsController ec = new ErrorsController()
        def recentError = new TestError(id: "1", source: "test", message: "recent error", date: new Date())
        ec.errorRepository = [filter: { cf -> [recentError] }] as Repository<TestError>
        ec.secureHttpClient = [post: { String url, String body -> "" }] as SecureHttpClient
        assert ec.checkForErrors()
    }

    @Test
    void testCleanErrors() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [filter: { cf -> [] }] as Repository<TestError>
        assert !ec.cleanOldErrors()
    }

    @Test
    void testGetLastErrors() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [sort: { s -> [] }] as Repository<TestError>
        assert ec.getLastErrors() instanceof List
    }

    @Test
    void testGetError() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [get: { it -> new TestError(id: it) }] as Repository<TestError>
        assert ec.getError("4")
    }

    @Test
    void testCreateError() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [create: { it -> it }] as Repository<TestError>
        assert ec.createError(new TestError())
    }

    @Test
    void testDeleteError() {
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [delete: { it -> new TestError(id: it) }] as Repository<TestError>
        assert ec.removeError("4")
    }

}
