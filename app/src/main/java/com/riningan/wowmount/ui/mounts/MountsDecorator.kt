package com.riningan.wowmount.ui.mounts

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.v7.widget.RecyclerView
import android.view.View
import com.riningan.wowmount.R
import com.riningan.wowmount.utils.ColorUtil
import com.riningan.wowmount.utils.fromDpToPx


class MountsDecorator : RecyclerView.ItemDecoration() {
    private val mDivider = ShapeDrawable(RectShape()).apply {
        paint.apply {
            color = ColorUtil.getColor(R.color.colorSurfacePressed)
            style = Paint.Style.FILL
            isAntiAlias = true
            flags = Paint.ANTI_ALIAS_FLAG
        }
        intrinsicHeight = 1.fromDpToPx()
        intrinsicWidth = 1.fromDpToPx()
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + 72.fromDpToPx()
        val right = parent.width - parent.paddingRight
        for (index in 0 until parent.childCount) {
            val view = parent.getChildAt(index)
            val nextView: View? = parent.getChildAt(index + 1)
            if (nextView != null) {
                val params = view.layoutParams as RecyclerView.LayoutParams
                val top = view.bottom + params.bottomMargin
                val bottom = top + mDivider.intrinsicHeight
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
        }
    }
}