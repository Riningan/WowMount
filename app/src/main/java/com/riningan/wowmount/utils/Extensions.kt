package com.riningan.wowmount.utils


public inline fun <T> Iterable<T>.isContain(predicate: (T) -> Boolean): Boolean {
    return firstOrNull(predicate) != null
}