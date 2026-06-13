package org.wikipedia.savedpages

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class PageComponentsUrlParserTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testParseWithCssAndJs() {
        val html = """
            <html>
            <head>
                <link rel="stylesheet" href="//en.wikipedia.org/w/load.php?modules=site.styles">
                <script src="//en.wikipedia.org/w/load.php?modules=startup"></script>
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertEquals(2, result.size)
        assertTrue(result.any { it.contains("site.styles") })
        assertTrue(result.any { it.contains("startup") })
    }

    @Test
    fun testParseWithEmptyHtml() {
        val html = ""
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseWithNoLinksOrScripts() {
        val html = "<html><head></head><body><p>Hello</p></body></html>"
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseWithOnlyCss() {
        val html = """
            <html>
            <head>
                <link rel="stylesheet" href="//en.wikipedia.org/w/load.php?modules=styles1">
                <link rel="stylesheet" href="//en.wikipedia.org/w/load.php?modules=styles2">
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertEquals(2, result.size)
        result.forEach { assertTrue(it.contains("load.php")) }
    }

    @Test
    fun testParseWithOnlyJs() {
        val html = """
            <html>
            <head>
                <script src="//en.wikipedia.org/w/load.php?modules=script1"></script>
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertEquals(1, result.size)
        assertTrue(result[0].contains("script1"))
    }

    @Test
    fun testParseFiltersEmptyHrefs() {
        val html = """
            <html>
            <head>
                <link rel="stylesheet" href="">
                <link rel="stylesheet" href="//en.wikipedia.org/w/load.php?modules=styles">
                <script src=""></script>
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertEquals(1, result.size)
    }

    @Test
    fun testParseResolvesProtocolRelativeUrls() {
        val html = """
            <html>
            <head>
                <link rel="stylesheet" href="//cdn.wikipedia.org/styles.css">
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        assertEquals(1, result.size)
        // Should be resolved to https://
        assertTrue(result[0].startsWith("https://"))
    }

    @Test
    fun testParseWithMalformedHtml() {
        val html = "<html><head><link rel=stylesheet href=/style.css><body>"
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        // Jsoup handles malformed HTML gracefully, should still parse
        assertNotNull(result)
    }

    @Test
    fun testParseWithStylesheetNotRel() {
        val html = """
            <html>
            <head>
                <link rel="icon" href="//en.wikipedia.org/favicon.ico">
            </head>
            <body></body>
            </html>
        """.trimIndent()
        val result = PageComponentsUrlParser.parse(html, wikiSite)
        // Only rel=stylesheet links should be included
        assertTrue(result.isEmpty())
    }
}
