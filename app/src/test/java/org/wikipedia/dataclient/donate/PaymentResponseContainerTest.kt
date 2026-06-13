package org.wikipedia.dataclient.donate

import org.junit.Assert.*
import org.junit.Test

class PaymentResponseContainerTest {

    @Test
    fun testDefaultContainer() {
        val container = PaymentResponseContainer()
        assertNull(container.response)
    }

    @Test
    fun testContainerWithResponse() {
        val response = PaymentResponse(status = "pending")
        val container = PaymentResponseContainer(response = response)
        assertEquals("pending", container.response?.status)
    }

    @Test
    fun testPaymentResponseDefaults() {
        val response = PaymentResponse()
        assertEquals("", response.status)
        assertEquals("", response.errorMessage)
        assertEquals("", response.orderId)
        assertEquals("", response.gatewayTransactionId)
        assertTrue(response.paymentMethods.isEmpty())
    }

    @Test
    fun testPaymentResponseWithValues() {
        val response = PaymentResponse(
            status = "success",
            orderId = "order-123",
            gatewayTransactionId = "gtx-456"
        )
        assertEquals("success", response.status)
        assertEquals("order-123", response.orderId)
        assertEquals("gtx-456", response.gatewayTransactionId)
    }

    @Test
    fun testPaymentMethodDefaults() {
        val method = PaymentMethod()
        assertEquals("", method.name)
        assertEquals("", method.type)
        assertTrue(method.brands.isEmpty())
        assertNull(method.configuration)
    }

    @Test
    fun testPaymentMethodConfigurationDefaults() {
        val config = PaymentMethodConfiguration()
        assertEquals("", config.merchantId)
        assertEquals("", config.merchantName)
        assertEquals("", config.gatewayMerchantId)
        assertEquals("", config.storeId)
        assertEquals("", config.region)
        assertEquals("", config.publicKeyId)
    }
}
