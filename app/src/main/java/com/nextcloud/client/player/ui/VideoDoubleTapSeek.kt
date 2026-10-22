/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2026 Nextcloud GmbH and Nextcloud contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package com.nextcloud.client.player.ui

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.nextcloud.client.player.media3.PlaybackModel
import com.owncloud.android.R
import kotlin.time.Duration.Companion.seconds

private val seekOffset = 10.seconds
private const val INDICATOR_VISIBLE_DURATION_MS = 500L
private const val INDICATOR_ALPHA_VISIBLE = 1f
private const val INDICATOR_ALPHA_HIDDEN = 0f

/**
 * Seeks backward or forward when the left or right half of [touchTarget] is double tapped. A single tap is
 * delivered as a click, so the click listener of [touchTarget] keeps working.
 */
class VideoDoubleTapSeek(
    private val touchTarget: View,
    private val indicator: TextView,
    private val playbackModel: PlaybackModel
) {

    private val gestureDetector = GestureDetector(
        touchTarget.context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean = touchTarget.performClick()

            override fun onDoubleTap(e: MotionEvent): Boolean {
                seek(if (e.x < touchTarget.width / 2f) SeekDirection.BACKWARD else SeekDirection.FORWARD)
                return true
            }
        }
    )

    @SuppressLint("ClickableViewAccessibility")
    fun attach() {
        touchTarget.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
    }

    private fun seek(direction: SeekDirection) {
        val offset = seekOffset.inWholeMilliseconds
        playbackModel.seekBy(if (direction == SeekDirection.FORWARD) offset else -offset)
        showIndicator(direction)
    }

    private fun showIndicator(direction: SeekDirection) {
        val (textRes, horizontalGravity) = when (direction) {
            SeekDirection.BACKWARD -> R.string.player_seek_backward_indicator to Gravity.START
            SeekDirection.FORWARD -> R.string.player_seek_forward_indicator to Gravity.END
        }

        indicator.run {
            text = context.getString(textRes, seekOffset.inWholeSeconds)
            updateLayoutParams<FrameLayout.LayoutParams> { gravity = horizontalGravity or Gravity.CENTER_VERTICAL }
            animate().cancel()
            alpha = INDICATOR_ALPHA_VISIBLE
            isVisible = true
            animate()
                .alpha(INDICATOR_ALPHA_HIDDEN)
                .setStartDelay(INDICATOR_VISIBLE_DURATION_MS)
                .withEndAction { isVisible = false }
        }
    }
}
