package com.trevorism.gcloud

import com.google.gson.Gson
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks


this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

String baseUrl = System.getenv("ACCEPTANCE_BASE_URL") ?: "https://testing.trevorism.com"

def responseObjects

When(/the list of test suites is requested/) {  ->
    SecureHttpClient secureHttpClient = new AppClientSecureHttpClient()
    String responseJson = secureHttpClient.get("${baseUrl}/api/suite")
    Gson gson = new Gson()
    responseObjects = gson.fromJson(responseJson, List.class)
}

Then(/more than {int} test suites are returned/) { Integer minSize ->
    assert responseObjects.size() > minSize
}