package com.riningan.wowmount.dispatcher

import android.support.test.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class RequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest) = MockResponse().apply {
        when {
            request.path.contains("/wow/mount") -> setResponseCode(200).setBody(readAsset("mounts.json"))
            request.path.contains("/wow/character") -> setResponseCode(200).setBody(readAsset("character.json"))
            request.path.contains("/spreadsheets/d") -> setResponseCode(200).setBody(readAsset("spreadsheet.csv"))
            else -> setResponseCode(404)
        }
        throttleBody(1024 * 50, 1, TimeUnit.SECONDS)
    }


    private fun readAsset(name: String): String? {
        return try {
            val inputStream = InstrumentationRegistry.getContext().assets.open(name)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}