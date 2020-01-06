package com.sanniou.multiitemkit.drawable

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable

class RoundDrawable(bitmap: Bitmap, cornerRadius: Int, corners: Int) : Drawable() {

    protected val cornerRadius: Float
    protected val mRect = RectF()
    protected val mBitmapRect: RectF
    protected val bitmapShader: BitmapShader
    protected val mPaint: Paint
    private val corners: Int
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRect[0f, 0f, bounds.width().toFloat()] = bounds.height().toFloat()
        val shaderMatrix = Matrix()
        shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL)
        bitmapShader.setLocalMatrix(shaderMatrix)
    }

    override fun draw(canvas: Canvas) { //先画一个圆角矩形将图片显示为圆角
        canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, mPaint)
        val notRoundedCorners = corners xor CORNER_ALL
        //哪个角不是圆角我再把你用矩形画出来
        if (notRoundedCorners and CORNER_TOP_LEFT != 0) {
            canvas.drawRect(0f, 0f, cornerRadius, cornerRadius, mPaint)
        }
        if (notRoundedCorners and CORNER_TOP_RIGHT != 0) {
            canvas.drawRect(mRect.right - cornerRadius, 0f, mRect.right, cornerRadius, mPaint)
        }
        if (notRoundedCorners and CORNER_BOTTOM_LEFT != 0) {
            canvas.drawRect(0f, mRect.bottom - cornerRadius, cornerRadius, mRect.bottom, mPaint)
        }
        if (notRoundedCorners and CORNER_BOTTOM_RIGHT != 0) {
            canvas.drawRect(
                mRect.right - cornerRadius, mRect.bottom - cornerRadius, mRect.right,
                mRect.bottom, mPaint
            )
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    companion object {
        const val CORNER_TOP_LEFT = 1
        const val CORNER_TOP_RIGHT = 1 shl 1
        const val CORNER_BOTTOM_LEFT = 1 shl 2
        const val CORNER_BOTTOM_RIGHT = 1 shl 3
        const val CORNER_ALL =
            CORNER_TOP_LEFT or CORNER_TOP_RIGHT or CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
    }

    init {
        this.cornerRadius = cornerRadius.toFloat()
        this.corners = corners
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapRect = RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat())
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.shader = bitmapShader
    }
}