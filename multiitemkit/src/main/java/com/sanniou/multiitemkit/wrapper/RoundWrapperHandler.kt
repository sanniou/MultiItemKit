package com.sanniou.multiitemkit.wrapper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.sanniou.multiitem.WrapperHandler
import com.sanniou.multiitemkit.drawable.RoundDrawable
import com.sanniou.multiitemkit.helper.setBackgroundKeepingPadding

class RoundWrapperHandler : WrapperHandler {

    @get:RoundType
    var roundType = RoundType.NULL
        private set
    @get:DrawableRes
    var roundDrawable = 0
        private set
    @get:ColorInt
    var roundColor: Int = defaultRoundColor
        private set
    var roundRadious = 30
        private set

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        RoundType.TOP,
        RoundType.TOP_START,
        RoundType.TOP_END,
        RoundType.NONE,
        RoundType.BOTTOM,
        RoundType.BOTTOM_START,
        RoundType.BOTTOM_END,
        RoundType.ALL,
        RoundType.NULL,
        RoundType.END,
        RoundType.START
    )
    annotation class RoundType {
        companion object {
            const val TOP: Int = RoundDrawable.CORNER_TOP_LEFT or RoundDrawable.CORNER_TOP_RIGHT
            const val TOP_START: Int = RoundDrawable.CORNER_TOP_LEFT
            const val TOP_END: Int = RoundDrawable.CORNER_TOP_RIGHT
            const val BOTTOM: Int =
                RoundDrawable.CORNER_BOTTOM_LEFT or RoundDrawable.CORNER_BOTTOM_RIGHT
            const val BOTTOM_START: Int = RoundDrawable.CORNER_BOTTOM_LEFT
            const val BOTTOM_END: Int = RoundDrawable.CORNER_BOTTOM_RIGHT
            const val ALL: Int = RoundDrawable.CORNER_ALL
            const val NONE = 0
            const val NULL = -1
            const val END: Int = RoundDrawable.CORNER_BOTTOM_RIGHT or RoundDrawable.CORNER_TOP_RIGHT
            const val START: Int = RoundDrawable.CORNER_TOP_LEFT or RoundDrawable.CORNER_BOTTOM_LEFT
        }
    }

    fun setRoundRadious(roundRadious: Int): RoundWrapperHandler {
        this.roundRadious = roundRadious
        return this
    }

    fun setRoundType(@RoundType roundType: Int): RoundWrapperHandler {
        this.roundType = roundType
        return this
    }

    fun setRoundDrawable(@DrawableRes roundDrawable: Int): RoundWrapperHandler {
        this.roundDrawable = roundDrawable
        return this
    }

    fun setRoundColor(@ColorInt roundColor: Int): RoundWrapperHandler {
        this.roundColor = roundColor
        return this
    }

    override fun wrapperHandle(view: View) {
        val roundDrawable =
            createRoundDrawable(this, view.context)
        if (roundDrawable != null) {
            setBackgroundKeepingPadding(view, roundDrawable)
        }
    }

    companion object {
        var defaultRoundColor = Color.WHITE

        private fun createRoundDrawable(
            obj: RoundWrapperHandler,
            context: Context
        ): Drawable? {
            val drawable = obj.roundDrawable
            val image: Bitmap?
            if (drawable != 0) {
                if (obj.roundType <= RoundType.NONE) {
                    return getDrawable(context, drawable)
                }
                image = BitmapFactory.decodeResource(context.resources, drawable)
                if (image == null) {
                    Log.e(
                        "RoundWrapperHandler",
                        "can not create RoundDrawable for drawable resources$drawable"
                    )
                    return getDrawable(context, drawable)
                }
            } else {
                val color = obj.roundColor
                if (color == Color.TRANSPARENT) {
                    return null
                }
                image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                image.eraseColor(color)
            }
            return when (obj.roundType) {
                RoundType.NULL -> null
                RoundType.NONE -> RoundDrawable(
                    image,
                    0,
                    RoundDrawable.CORNER_BOTTOM_LEFT
                )
                else -> RoundDrawable(image, 30, obj.roundType)
            }
        }
    }
}