package com.riningan.wowmount.rule

import android.os.IBinder
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.lang.reflect.Method
import java.util.*


class DisableAnimationsRule : TestRule {
    private var mSetAnimationScalesMethod: Method
    private var mGetAnimationScalesMethod: Method
    private var mWindowManagerObject: Any


    init {
        try {
            val windowManagerStubClazz = Class.forName("android.view.IWindowManager\$Stub")
            val asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder::class.java)
            val serviceManagerClazz = Class.forName("android.os.ServiceManager")
            val getService = serviceManagerClazz.getDeclaredMethod("getService", String::class.java)
            val windowManagerClazz = Class.forName("android.view.IWindowManager")
            mSetAnimationScalesMethod = windowManagerClazz.getDeclaredMethod("setAnimationScales", FloatArray::class.java)
            mGetAnimationScalesMethod = windowManagerClazz.getDeclaredMethod("getAnimationScales")
            val windowManagerBinder = getService.invoke(null, "window") as IBinder
            mWindowManagerObject = asInterface.invoke(null, windowManagerBinder)
        } catch (e: Exception) {
            throw RuntimeException("Failed to access animation methods", e)
        }
    }


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            setAnimationScaleFactors(0.0f)
            try {
                base.evaluate()
            } finally {
                setAnimationScaleFactors(1.0f)
            }
        }
    }


    private fun setAnimationScaleFactors(scaleFactor: Float) {
        val scaleFactors = mGetAnimationScalesMethod.invoke(mWindowManagerObject) as FloatArray
        Arrays.fill(scaleFactors, scaleFactor)
        mSetAnimationScalesMethod.invoke(mWindowManagerObject, scaleFactors)
    }
}