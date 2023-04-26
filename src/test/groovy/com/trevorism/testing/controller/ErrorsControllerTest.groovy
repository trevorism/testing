package com.trevorism.testing.controller

import com.trevorism.data.Repository
import com.trevorism.testing.model.TestError
import org.junit.jupiter.api.Test

class ErrorsControllerTest {

    @Test
    void testCheckForErrors(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [list:{}] as Repository<TestError>
        assert !ec.checkForErrors()
    }

    @Test
    void testCleanErrors(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [list:{}] as Repository<TestError>
        assert !ec.cleanOldErrors()

    }

    @Test
    void testGetLastErrors(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [sort:{}] as Repository<TestError>
        assert !ec.getLastErrors()
    }

    @Test
    void testGetError(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [get:{}] as Repository<TestError>
        assert !ec.getError("4")
    }

    @Test
    void testCreateError(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [create:{ it -> it}] as Repository<TestError>
        assert ec.createError(new TestError())
    }

    @Test
    void testDeleteError(){
        ErrorsController ec = new ErrorsController()
        ec.errorRepository = [delete:{ it -> new TestError()}] as Repository<TestError>
        assert ec.removeError("4")
    }
}

