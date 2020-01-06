package com.sanniou.multiitemkit.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.annotation.IntRange

/**
 * 用于自定义 ItemDecoration 的分割线，可以定义高度和颜色
 */
class DividerDrawable(
    var heightPx: Int,
    var dividerColor: Int? = null,
    var padding: Float = 0F,
    var backgroundColor: Int = Color.WHITE
) : Drawable() {

    private val paint = Paint()

    override fun draw(canvas: Canvas) {
        val rect = bounds
        paint.color = backgroundColor
        canvas.drawRect(
            rect.left.toFloat(),
            rect.top.toFloat(),
            rect.right.toFloat(),
            rect.bottom.toFloat(),
            paint
        )

        dividerColor?.let {
            paint.color = it
            canvas.drawRect(
                rect.left + padding,
                rect.top.toFloat(),
                rect.right - padding,
                rect.bottom.toFloat(),
                paint
            )
        }
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) { // Do nothing
    }

    override fun setColorFilter(colorFilter: ColorFilter?) { // Do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun getIntrinsicHeight(): Int {
        return heightPx
    }

    override fun getIntrinsicWidth(): Int {
        return heightPx
    }
}