package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonFormat

class TestError {

    String id
    String source
    String message
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date date
    Map details
}
