package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class L10nUtilTest {

    @Test
    fun testGetDesiredLanguageCodeTraditionalChineseVariants() {
        assertEquals("zh-hant", L10nUtil.getDesiredLanguageCode("zh-hant"))
        assertEquals("zh-hant", L10nUtil.getDesiredLanguageCode("zh-tw"))
        assertEquals("zh-hant", L10nUtil.getDesiredLanguageCode("zh-hk"))
        assertEquals("zh-hant", L10nUtil.getDesiredLanguageCode("zh-mo"))
    }

    @Test
    fun testGetDesiredLanguageCodeSimplifiedChineseVariants() {
        assertEquals("zh-hans", L10nUtil.getDesiredLanguageCode("zh-hans"))
        assertEquals("zh-hans", L10nUtil.getDesiredLanguageCode("zh-cn"))
        assertEquals("zh-hans", L10nUtil.getDesiredLanguageCode("zh-sg"))
        assertEquals("zh-hans", L10nUtil.getDesiredLanguageCode("zh-my"))
    }

    @Test
    fun testGetDesiredLanguageCodeOtherLanguages() {
        assertEquals("en", L10nUtil.getDesiredLanguageCode("en"))
        assertEquals("fr", L10nUtil.getDesiredLanguageCode("fr"))
        assertEquals("ja", L10nUtil.getDesiredLanguageCode("ja"))
        assertEquals("", L10nUtil.getDesiredLanguageCode(""))
    }
}
