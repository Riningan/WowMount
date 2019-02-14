package com.riningan.wowmount

import android.os.Bundle
import android.support.test.runner.AndroidJUnitRunner
import com.linkedin.android.testbutler.TestButler


class WowmountTestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        TestButler.setup(targetContext)
        super.onStart()
    }

    override fun finish(resultCode: Int, results: Bundle) {
        TestButler.teardown(targetContext)
        super.finish(resultCode, results)
    }
}