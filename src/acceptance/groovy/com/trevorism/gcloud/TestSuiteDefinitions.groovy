package com.trevorism.gcloud

import com.google.gson.Gson
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks


this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

def responseObjects

When(/the list of test suites is requested/) {  ->
    SecureHttpClient secureHttpClient = new AppClientSecureHttpClient()
    String responseJson = secureHttpClient.get("https://testing.trevorism.com/api/suite")
    Gson gson = new Gson()
    responseObjects = gson.fromJson(responseJson, List.class)
}

Then(/more than {int} test suites are returned/) { Integer minSize ->
    assert responseObjects.size() > minSize
}