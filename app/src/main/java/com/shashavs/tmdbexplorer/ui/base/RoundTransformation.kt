package com.shashavs.tmdbexplorer.ui.base

import android.graphics.*
import com.squareup.picasso.Transformation

class RoundTransformation(private val round: Float) : Transformation {

    override fun transform(source: Bitmap?): Bitmap? {
        if(source != null) {
            val bitmap = Bitmap.createBitmap(source.width, source.height, source.config)
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                isAntiAlias = true
            }
            canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), round, round, paint)
            source.recycle()
            return bitmap
        }
        return null
    }

    override fun key() = "roundCorner"
}