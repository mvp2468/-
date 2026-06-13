package org.wikipedia.database

import org.junit.Assert.*
import org.junit.Test
import org.wikipedia.page.Namespace

class NamespaceTypeConverterTest {
    private val converter = NamespaceTypeConverter()

    @Test
    fun testIntToNamespaceMain() {
        val result = converter.intToNamespace(0)
        assertEquals(Namespace.MAIN, result)
    }

    @Test
    fun testIntToNamespaceTalk() {
        val result = converter.intToNamespace(1)
        assertEquals(Namespace.TALK, result)
    }

    @Test
    fun testIntToNamespaceUser() {
        val result = converter.intToNamespace(2)
        assertEquals(Namespace.USER, result)
    }

    @Test
    fun testIntToNamespaceCategory() {
        val result = converter.intToNamespace(14)
        assertEquals(Namespace.CATEGORY, result)
    }

    @Test
    fun testIntToNamespaceDraft() {
        val result = converter.intToNamespace(118)
        assertEquals(Namespace.DRAFT, result)
    }

    @Test
    fun testIntToNamespaceReturnsNullForNull() {
        val result = converter.intToNamespace(null)
        assertNull(result)
    }

    @Test
    fun testNamespaceToInt() {
        val result = converter.namespaceToInt(Namespace.MAIN)
        assertEquals(0, result)
    }

    @Test
    fun testNamespaceToIntTalk() {
        val result = converter.namespaceToInt(Namespace.TALK)
        assertEquals(1, result)
    }

    @Test
    fun testNamespaceToIntReturnsNullForNull() {
        val result = converter.namespaceToInt(null)
        assertNull(result)
    }

    @Test
    fun testRoundTrip() {
        val namespace = Namespace.CATEGORY
        val code = converter.namespaceToInt(namespace)
        val restored = converter.intToNamespace(code)
        assertEquals(namespace, restored)
    }

    @Test
    fun testAllKnownNamespacesRoundTrip() {
        Namespace.entries.forEach { ns ->
            val code = converter.namespaceToInt(ns)
            val restored = converter.intToNamespace(code)
            assertEquals(ns, restored)
        }
    }
}
