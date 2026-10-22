/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2026 Nextcloud GmbH and Nextcloud contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package com.nextcloud.client.player.util

import androidx.media3.common.C

fun seekPosition(currentPosition: Long, offset: Long, duration: Long): Long {
    val position = (currentPosition + offset).coerceAtLeast(0)
    return if (duration == C.TIME_UNSET) position else position.coerceAtMost(duration)
}
