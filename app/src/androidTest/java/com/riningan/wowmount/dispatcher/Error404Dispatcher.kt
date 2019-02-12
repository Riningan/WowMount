package com.riningan.wowmount.dispatcher

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit


class Error404Dispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse = MockResponse()
            .setResponseCode(404)
            .setBody("{}")
            .throttleBody(1, 3, TimeUnit.SECONDS)
}