package com.riningan.wowmount.widget

import android.view.MotionEvent
import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet


class ControlledViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    private var mInitialXValue: Float = 0.toFloat()
    private var mDirection = SwipeDirection.All


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent) =
            if (isSwipeAllowed(event)) {
                try {
                    super.onTouchEvent(event)
                } catch (e: IllegalArgumentException) {
                    false
                }
            } else {
                false
            }

    override fun onInterceptTouchEvent(event: MotionEvent) =
            if (isSwipeAllowed(event)) {
                try {
                    super.onInterceptTouchEvent(event)
                } catch (e: IllegalArgumentException) {
                    false
                }
            } else {
                false
            }


    fun setAllowedSwipeDirection(direction: SwipeDirection) {
        mDirection = direction
    }


    private fun isSwipeAllowed(event: MotionEvent) =
            when {
                mDirection == SwipeDirection.All -> true
                mDirection == SwipeDirection.NONE -> {
                    //disable any swipe
                    false
                }
                event.action == MotionEvent.ACTION_DOWN -> {
                    mInitialXValue = event.x
                    true
                }
                event.action == MotionEvent.ACTION_MOVE -> try {
                    val diffX = event.x - mInitialXValue
                    if (diffX > 0 && mDirection == SwipeDirection.RIGHT) {
                        // swipe from left to right detected
                        false
                    } else if (diffX < 0 && mDirection == SwipeDirection.LEFT) {
                        // swipe from right to left detected
                        false
                    } else {
                        true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    true
                }
                else -> true
            }


    enum class SwipeDirection {
        All, LEFT, RIGHT, NONE
    }
}