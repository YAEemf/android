/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2026 Nextcloud GmbH and Nextcloud contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package com.nextcloud.client.player.util

import androidx.media3.common.C
import org.junit.Assert.assertEquals
import org.junit.Test

class SeekPositionTest {

    @Test
    fun `seeking forward adds the offset`() {
        assertEquals(40_000L, seekPosition(currentPosition = 30_000L, offset = 10_000L, duration = 90_000L))
    }

    @Test
    fun `seeking backward subtracts the offset`() {
        assertEquals(20_000L, seekPosition(currentPosition = 30_000L, offset = -10_000L, duration = 90_000L))
    }

    @Test
    fun `seeking backward near the start stops at the start`() {
        assertEquals(0L, seekPosition(currentPosition = 4_000L, offset = -10_000L, duration = 90_000L))
    }

    @Test
    fun `seeking forward near the end stops at the end`() {
        assertEquals(90_000L, seekPosition(currentPosition = 85_000L, offset = 10_000L, duration = 90_000L))
    }

    @Test
    fun `seeking forward with unknown duration is not limited`() {
        assertEquals(95_000L, seekPosition(currentPosition = 85_000L, offset = 10_000L, duration = C.TIME_UNSET))
    }
}
