package com.example.playlistmaker.player.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes val defStyleAttr: Int = 0,
    @StyleRes val defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageBitmap: Bitmap? = null
    private var imageRect = RectF()

    private var playBitmap: Bitmap? = null
    private var pauseBitmap: Bitmap? = null

    private var isPlaying = false
    private var desiredWidth = 0
    private var desiredHeight = 0

    var onClickAction: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_playButtonReference)?.toBitmap()
                pauseBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_pauseButtonReference)?.toBitmap()

                desiredWidth = playBitmap?.width ?: pauseBitmap?.width ?: 0
                desiredHeight = playBitmap?.height ?: pauseBitmap?.height ?: 0

                imageBitmap = if (isPlaying) pauseBitmap else playBitmap

            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = desiredWidth
        var height = desiredHeight

        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                width = widthSize
            }

            MeasureSpec.AT_MOST -> {
                width = desiredWidth.coerceAtMost(widthSize)
            }

            MeasureSpec.UNSPECIFIED -> {
                width = desiredWidth
            }
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                height = heightSize
            }

            MeasureSpec.AT_MOST -> {
                height = desiredHeight.coerceAtMost(heightSize)
            }

            MeasureSpec.UNSPECIFIED -> {
                height = desiredHeight
            }
        }
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, imageRect, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                onClickAction?.invoke()
                toggle()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun showPlayButton() {
        imageBitmap = playBitmap
        invalidate()
    }

    fun showPauseButton() {
        imageBitmap = pauseBitmap
        invalidate()
    }

    private fun toggle() {
        isPlaying = !isPlaying

        if (isPlaying) {
            showPauseButton()
        } else {
            showPlayButton()
        }
    }
}