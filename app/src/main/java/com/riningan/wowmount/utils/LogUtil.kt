package com.riningan.wowmount.utils

import android.annotation.SuppressLint
import android.util.Log
import com.riningan.wowmount.BuildConfig
import java.text.SimpleDateFormat
import java.util.*


object LogUtil {
    private const val TYPE_DEBUG = "DEBUG: "
    private const val TYPE_ERROR = "ERROR: "

    private val mLogQueue = LinkedList<String>()

    @SuppressLint("SimpleDateFormat")
    private val dt = SimpleDateFormat("yyyy-MM-dd: HH:mm:ss")


    private val dateTime: String
        get() = dt.format(Date(Calendar.getInstance().time.time))


    fun addDebug() {
        debug(getClassAndMethod())
    }

    fun addDebug(o: Any) {
        debug(getClassAndMethod(o))
    }

    fun addDebug(message: String) {
        debug(getClassAndMethod() + " - $message")
    }

    fun addDebug(o: Any, message: String) {
        debug(getClassAndMethod(o) + " - " + message)
    }

    fun addDebug(param: String, value: String) {
        debug(getClassAndMethod() + "($param = $value)")
    }

    fun addDebug(o: Any, param: String, value: String) {
        debug(getClassAndMethod(o) + "($param = $value)")
    }

    fun addDebug(param: String, value: Int) {
        debug(getClassAndMethod() + "($param = $value)")
    }

    fun addDebug(o: Any, param: String, value: Int) {
        debug(getClassAndMethod(o) + "($param = $value)")
    }

    fun addDebug(param: String, value: Boolean) {
        debug(getClassAndMethod() + "($param = $value)")
    }

    fun addDebug(o: Any, param: String, value: Boolean) {
        debug(getClassAndMethod(o) + "($param = $value)")
    }

    fun addDebug(param: String, value: Long) {
        debug(getClassAndMethod() + "($param = $value)")
    }

    fun addDebug(o: Any, param: String, value: Long) {
        debug(getClassAndMethod(o) + "($param = $value)")
    }

    fun addDebug(param: String, value: String, vararg params: String) {
        val msg = StringBuilder(getClassAndMethod() + "($param = $value)")
        var i = 0
        while (i < params.size - 1) {
            msg.append(", ").append(params[i]).append(" = ").append(params[i + 1])
            i += 2
        }
        if (params.size % 2 == 0) {
            msg.append(")")
        } else {
            msg.append(") - ").append(params[params.size - 1])
        }
        debug(msg.toString())
    }

    fun addDebug(o: Any, param: String, value: String, vararg params: String) {
        val msg = StringBuilder(getClassAndMethod(o) + "($param = $value)")
        var i = 0
        while (i < params.size - 1) {
            msg.append(", ").append(params[i]).append(" = ").append(params[i + 1])
            i += 2
        }
        if (params.size % 2 == 0) {
            msg.append(")")
        } else {
            msg.append(") - ").append(params[params.size - 1])
        }
        debug(msg.toString())
    }


    fun addError(message: String) {
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        val msg = getClassAndMethod() + "($lineNumber) - $message)"
        error(msg)
    }

    fun addError(throwable: Throwable) {
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        val msg = getClassAndMethod() + "($lineNumber) - $throwable.message)"
        error(msg)
    }

    fun addError(o: Any, throwable: Throwable) {
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        val msg = getClassAndMethod(o) + "($lineNumber) - $throwable.message)"
        error(msg)
    }


    private fun getClassAndMethod(): String {
        val lenght = BuildConfig.APPLICATION_ID.length + 1
        val className = Thread.currentThread().stackTrace[4].className.substring(lenght)
        val methodName = Thread.currentThread().stackTrace[4].methodName
        return className + "." + methodName.replace("lambda$", "")
    }

    private fun getClassAndMethod(o: Any): String {
        val lenght = BuildConfig.APPLICATION_ID.length + 1
        val className = o.javaClass.name.substring(lenght)
        val methodName = Thread.currentThread().stackTrace[4].methodName
        return className + "." + methodName.replace("lambda$", "")
    }


    private fun debug(msg: String) {
        val info = BuildConfig.VERSION_NAME
        Log.d(TYPE_DEBUG + info, dateTime + ": " + android.os.Process.myPid() + ": " + Thread.currentThread().id + "\n" + msg)
        if (!BuildConfig.DEBUG) {
            add("$TYPE_DEBUG$info: $msg")
        }
    }

    private fun error(msg: String) {
        val info = BuildConfig.VERSION_NAME
        Log.e(TYPE_ERROR + info, dateTime + ": " + android.os.Process.myPid() + ": " + Thread.currentThread().id + "\n" + msg)
        if (!BuildConfig.DEBUG) {
            add("$TYPE_ERROR$info: $msg")
        }
    }


    private fun add(msg: String) {
        synchronized(mLogQueue) {
            if (mLogQueue.size > 100) {
                mLogQueue.removeFirst()
            }
            mLogQueue.add(msg)
        }
    }
}