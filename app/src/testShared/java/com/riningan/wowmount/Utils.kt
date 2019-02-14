package com.riningan.wowmount

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible


@Suppress("UNCHECKED_CAST")
fun <T, R> setPrivateField(kClass: KClass<*>, fieldName: String, receiver: T, value: R) {
    kClass.declaredMembers.find {
        it.name == fieldName
    }?.let {
        if (it is KMutableProperty1<*, *>) {
            it.isAccessible = true
            (it as KMutableProperty1<T, R>).set(receiver, value)
            it.isAccessible = false
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, R> getPrivateField(kClass: KClass<*>, fieldName: String, receiver: T, action: (field: R) -> Unit) {
    kClass.declaredMembers.find {
        it.name == fieldName
    }?.let {
        if (it is KMutableProperty1<*, *>) {
            it.isAccessible = true
            val field = (it as KMutableProperty1<T, R>).get(receiver)
            action(field)
            it.isAccessible = false
        }
    }
}