package com.riningan.wowmount

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible


fun <T, R> setPrivateField(kClass: KClass<*>, fieldName: String, receiver: T, value: R) {
    kClass.declaredMembers.find {
        it.name == fieldName
    }?.let {
        it.isAccessible = true
        (it as KMutableProperty1<T, R>).set(receiver, value)
        it.isAccessible = false
    }
}