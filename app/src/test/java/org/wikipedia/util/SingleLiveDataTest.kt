package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SingleLiveDataTest {

    @Test
    fun testSingleLiveDataInitialValue() {
        val liveData = SingleLiveData<String>()
        assertFalse(liveData.hasObservers())
    }

    @Test
    fun testSetValueWithObserver() {
        val liveData = SingleLiveData<String>()
        var observed: String? = null
        liveData.observeForever { observed = it }
        liveData.value = "test"
        assertEquals("test", observed)
    }

    @Test
    fun testPostValueWithObserver() {
        val liveData = SingleLiveData<String>()
        var observed: String? = null
        liveData.observeForever { observed = it }
        liveData.postValue("post test")
        Shadows.shadowOf(android.os.Looper.getMainLooper()).idle()
        assertEquals("post test", observed)
    }

    @Test
    fun testObserverCalledOnlyOnce() {
        val liveData = SingleLiveData<String>()
        var callCount = 0
        liveData.observeForever { callCount++ }
        liveData.value = "first"
        assertEquals(1, callCount)
    }
}
