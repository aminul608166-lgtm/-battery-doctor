package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ShopCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name and verify shop catalog`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Battery & Charger Doctor", appName)
        assertEquals("8801897040005", ShopCatalog.WHATSAPP_NUMBER)
        assertTrue(ShopCatalog.products.any { it.name == "Original 20W Charger" && it.price == "850 TK" })
        assertTrue(ShopCatalog.products.any { it.name == "Original Type-C Cable" && it.price == "250 TK" })
        assertTrue(ShopCatalog.products.any { it.name == "Premium Mobile Cover" && it.price == "350 TK" })
    }
}
