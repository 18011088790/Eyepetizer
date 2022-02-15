package com.example.eye.util
/**
 * @Author:gxs
 *
 * @Date 2022/2/10 20:25
 *
 * @Description:自定义字体工具类
 *
 */
import android.graphics.Typeface
import com.example.eye.EyeApplication

object TypeFaceUtil {

    const val FZLL_TYPEFACE = 1

    const val FZDB1_TYPEFACE = 2

    const val FUTURA_TYPEFACE = 3

    const val DIN_TYPEFACE = 4

    const val LOBSTER_TYPEFACE = 5

    private var fzlLTypeface: Typeface? = null

    private var fzdb1Typeface: Typeface? = null

    private var futuraTypeface: Typeface? = null

    private var dinTypeface: Typeface? = null

    private var lobsterTypeface: Typeface? = null

    fun getFzlLTypeface() = if (fzlLTypeface == null) {
        try {
            Typeface.createFromAsset(EyeApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        fzlLTypeface!!
    }

    fun getFzdb1Typeface() = if (fzdb1Typeface == null) {
        try {
            Typeface.createFromAsset(EyeApplication.context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        fzdb1Typeface!!
    }

    fun getFuturaTypeface() = if (futuraTypeface == null) {
        try {
            Typeface.createFromAsset(EyeApplication.context.assets, "fonts/Futura-CondensedMedium.ttf")
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        futuraTypeface!!
    }

    fun getDinTypeface() = if (dinTypeface == null) {
        try {
            Typeface.createFromAsset(EyeApplication.context.assets, "fonts/DIN-Condensed-Bold.ttf")
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }

    } else {
        dinTypeface!!
    }

    fun getLobsterTypeface() = if (lobsterTypeface == null) {
        try {
            Typeface.createFromAsset(EyeApplication.context.assets, "fonts/Lobster-1.4.otf")
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }

    } else {
        lobsterTypeface!!
    }

}