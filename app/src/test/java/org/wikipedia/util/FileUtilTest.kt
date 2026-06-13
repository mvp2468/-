package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.ByteArrayOutputStream

@RunWith(RobolectricTestRunner::class)
class FileUtilTest {

    @Test
    fun testSanitizeFileNameReplacesSpecialChars() {
        val result = FileUtil.sanitizeFileName("hello:world")
        assertEquals("hello_world", result)
    }

    @Test
    fun testSanitizeFileNameReplacesBackslash() {
        val result = FileUtil.sanitizeFileName("path\\to\\file")
        assertEquals("path_to_file", result)
    }

    @Test
    fun testSanitizeFileNameReplacesForwardSlash() {
        val result = FileUtil.sanitizeFileName("path/to/file")
        assertEquals("path_to_file", result)
    }

    @Test
    fun testSanitizeFileNameReplacesAsterisk() {
        val result = FileUtil.sanitizeFileName("file*name")
        assertEquals("file_name", result)
    }

    @Test
    fun testSanitizeFileNameReplacesQuestionMark() {
        val result = FileUtil.sanitizeFileName("what?")
        assertEquals("what_", result)
    }

    @Test
    fun testSanitizeFileNameReplacesDoubleQuote() {
        val result = FileUtil.sanitizeFileName("file\"name")
        assertEquals("file_name", result)
    }

    @Test
    fun testSanitizeFileNameReplacesAngleBrackets() {
        val result = FileUtil.sanitizeFileName("<file>")
        assertEquals("_file_", result)
    }

    @Test
    fun testSanitizeFileNameReplacesPipe() {
        val result = FileUtil.sanitizeFileName("a|b")
        assertEquals("a_b", result)
    }

    @Test
    fun testSanitizeFileNameReplacesSingleQuote() {
        val result = FileUtil.sanitizeFileName("it's")
        assertEquals("it_s", result)
    }

    @Test
    fun testSanitizeFileNamePreservesValidChars() {
        val result = FileUtil.sanitizeFileName("my_file-123.txt")
        assertEquals("my_file-123.txt", result)
    }

    @Test
    fun testIsVideoForOgg() {
        assertTrue(FileUtil.isVideo("audio/ogg"))
        assertTrue(FileUtil.isVideo("video/ogg"))
        assertTrue(FileUtil.isVideo("video/mp4"))
    }

    @Test
    fun testIsVideoForNonVideo() {
        assertFalse(FileUtil.isVideo("image/png"))
        assertFalse(FileUtil.isVideo("text/plain"))
    }

    @Test
    fun testIsAudio() {
        assertTrue(FileUtil.isAudio("audio/mpeg"))
        assertTrue(FileUtil.isAudio("audio/ogg"))
        assertTrue(FileUtil.isAudio("audio/wav"))
    }

    @Test
    fun testIsAudioForNonAudio() {
        assertFalse(FileUtil.isAudio("video/mp4"))
        assertFalse(FileUtil.isAudio("image/jpeg"))
    }

    @Test
    fun testIsImage() {
        assertTrue(FileUtil.isImage("image/png"))
        assertTrue(FileUtil.isImage("image/jpeg"))
        assertTrue(FileUtil.isImage("image/gif"))
        assertTrue(FileUtil.isImage("image/svg+xml"))
    }

    @Test
    fun testIsImageForNonImage() {
        assertFalse(FileUtil.isImage("video/mp4"))
        assertFalse(FileUtil.isImage("audio/mpeg"))
    }

    @Test
    fun testWriteToFile() {
        val bytes = ByteArrayOutputStream()
        bytes.write("hello".toByteArray())
        val file = java.io.File(java.io.File.createTempFile("test_", ".txt").parent, "test_output.txt")
        try {
            val result = FileUtil.writeToFile(bytes, file)
            assertTrue(result.exists())
            assertEquals("hello", result.readText())
        } finally {
            file.delete()
        }
    }
}
