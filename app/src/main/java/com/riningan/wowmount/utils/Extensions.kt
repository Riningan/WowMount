package com.riningan.wowmount.utils

import android.support.v4.view.ViewPager
import android.util.TypedValue
import com.riningan.wowmount.app.WowMountApp


fun Int.isZero() = this == 0

fun Int.isNotZero() = this != 0


fun Int.fromDpToPx(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), WowMountApp.getContext().resources.displayMetrics).toInt()
}


inline fun <T> Iterable<T>.isContain(predicate: (T) -> Boolean): Boolean {
    return firstOrNull(predicate) != null
}


inline fun ViewPager.onPageScrollStateChanged(crossinline action: (state: Int) -> Unit) =
        setOnPageChangeListener(onPageScrollStateChanged = action)

inline fun ViewPager.onPageScrolled(crossinline action: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) =
        setOnPageChangeListener(onPageScrolled = action)

inline fun ViewPager.onPageSelected(crossinline action: (position: Int) -> Unit) =
        setOnPageChangeListener(onPageSelected = action)

inline fun ViewPager.setOnPageChangeListener(
        crossinline onPageScrollStateChanged: (state: Int) -> Unit = {},
        crossinline onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> },
        crossinline onPageSelected: (position: Int) -> Unit = { }
) {
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }
    }
    addOnPageChangeListener(listener)
}