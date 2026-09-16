package com.shelf.core

import com.shelf.core.utils.StringUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {

    @Test
    fun cleanIsbn_removesDashesAndSpaces() {
        val formatted = "978-0-7352-1129-2 "
        val cleaned = StringUtils.cleanIsbn(formatted)
        assertEquals("9780735211292", cleaned)
    }

    @Test
    fun formatReadingPercentage_calculatesCorrectPercentage() {
        val percentage = StringUtils.formatReadingPercentage(180, 250)
        assertEquals(72.0, percentage)
    }

    @Test
    fun formatReadingPercentage_zeroTotalPages_returnsZero() {
        val percentage = StringUtils.formatReadingPercentage(10, 0)
        assertEquals(0.0, percentage)
    }

    @Test
    fun extractOpenLibraryId_removesPrefixes() {
        assertEquals("OL27517W", StringUtils.extractOpenLibraryId("/works/OL27517W"))
        assertEquals("OL7510103A", StringUtils.extractOpenLibraryId("/authors/OL7510103A"))
    }
}
