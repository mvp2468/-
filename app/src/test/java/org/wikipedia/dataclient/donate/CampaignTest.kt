package org.wikipedia.dataclient.donate

import org.junit.Assert.*
import org.junit.Test

class CampaignTest {

    @Test
    fun testDefaultConstructor() {
        val campaign = Campaign(version = 1)
        assertFalse(campaign.hasPlatform("Android"))
        assertTrue(campaign.countries.isEmpty())
    }

    @Test
    fun testHasPlatformReturnsTrue() {
        val campaign = Campaign(
            version = 1,
            platforms = mapOf("Android" to Campaign.PlatformParams())
        )
        assertTrue(campaign.hasPlatform("Android"))
        assertFalse(campaign.hasPlatform("iOS"))
    }

    @Test
    fun testGetIdForLangNoAssetsReturnsId() {
        val campaign = Campaign(version = 1, id = "C1")
        val result = campaign.getIdForLang("en")
        assertEquals("C1", result)
    }

    @Test
    fun testGetIdForLangWithSingleAsset() {
        val assets = Campaign.Assets(id = "A1", weight = 1f)
        val campaign = Campaign(version = 1, id = "C1", assets = mapOf("en" to listOf(assets)))
        val result = campaign.getIdForLang("en")
        assertEquals("C1_A1", result)
    }

    @Test
    fun testGetIdForLangUnknownLanguageReturnsId() {
        val campaign = Campaign(version = 1, id = "C1")
        val result = campaign.getIdForLang("fr")
        assertEquals("C1", result)
    }

    @Test
    fun testGetAssetsForLangReturnsNullForNonExistent() {
        val campaign = Campaign(version = 1)
        assertNull(campaign.getAssetsForLang("en"))
    }

    @Test
    fun testGetAssetsForLangReturnsNullForEmptyList() {
        val campaign = Campaign(version = 1, assets = mapOf("en" to emptyList()))
        assertNull(campaign.getAssetsForLang("en"))
    }

    @Test
    fun testGetAssetsForLangReturnsSingleAsset() {
        val asset = Campaign.Assets(id = "A1", weight = 1f)
        val campaign = Campaign(version = 1, assets = mapOf("en" to listOf(asset)))
        val result = campaign.getAssetsForLang("en")
        assertNotNull(result)
        assertEquals("A1", result!!.id)
    }

    @Test
    fun testAssetsDefaultValues() {
        val asset = Campaign.Assets()
        assertEquals("", asset.id)
        assertEquals(1f, asset.weight)
        assertEquals("", asset.text)
        assertEquals("", asset.footer)
        assertEquals(0, asset.actions.size)
    }

    @Test
    fun testActionDefaultValues() {
        val action = Campaign.Action()
        assertEquals("", action.title)
        assertNull(action.url)
    }

    @Test
    fun testActionWithValues() {
        val action = Campaign.Action(title = "Donate", url = "https://donate.example.com")
        assertEquals("Donate", action.title)
        assertEquals("https://donate.example.com", action.url)
    }

    @Test
    fun testPlatformParamsExists() {
        val params = Campaign.PlatformParams()
        assertNotNull(params)
    }
}
