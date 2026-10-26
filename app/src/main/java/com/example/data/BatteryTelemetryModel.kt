package com.example.data

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.DrawableRes
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppDestination {
    HOME,
    STATS,
    SETTINGS,
    FULL_BATTERY_ALARM,
    THEFT_ALARM,
    CHARGER_CHECKER,
    AI_CAMERA_SCANNER,
    AI_BATTERY_DOCTOR,
    SHOP
}

data class BatteryTelemetry(
    val level: Int = 87,
    val temperatureCelsius: Float = 32.0f,
    val voltageMv: Int = 4180,
    val currentMa: Int = 1850,
    val wattage: Float = 18.2f,
    val isCharging: Boolean = true,
    val plugType: String = "AC Fast Charger",
    val healthStatus: String = "Healthy",
    val technology: String = "Li-ion",
    val cableCondition: String = "Good Condition",
    val chargingSpeedLabel: String = "18W Fast Charging",
    val timestampLabel: String = "Just now"
) {
    val voltageVolts: Float
        get() = if (voltageMv > 100) voltageMv / 1000f else voltageMv.toFloat()

    fun formattedTemperature(useFahrenheit: Boolean): String {
        return if (useFahrenheit) {
            val f = temperatureCelsius * 9f / 5f + 32f
            String.format("%.1f°F", f)
        } else {
            String.format("%.1f°C", temperatureCelsius)
        }
    }

    val temperatureCondition: String
        get() = when {
            temperatureCelsius >= 43f -> "Overheating Warning"
            temperatureCelsius >= 38f -> "Warm Temperature"
            temperatureCelsius <= 10f -> "Cool Temperature"
            else -> "Good Temperature"
        }
}

data class TelemetryHistoryEntry(
    val timeLabel: String,
    val level: Int,
    val temperatureCelsius: Float,
    val wattage: Float,
    val plugType: String
)

data class ChargerBenchmarkResult(
    val peakWatts: Float,
    val averageWatts: Float,
    val averageCurrentMa: Int,
    val voltageStabilityPercent: Int,
    val cableVerdict: String,
    val adapterVerdict: String
)

data class ShopProduct(
    val id: String,
    val name: String,
    val price: String,
    val numericPriceTk: Int,
    val rating: String,
    val category: String,
    val badge: String,
    val specs: String,
    @DrawableRes val imageRes: Int
)

object ShopCatalog {
    const val WHATSAPP_NUMBER = "8801897040005"
    const val DISPLAY_PHONE = "01897040005"

    val products = listOf(
        ShopProduct(
            id = "charger_20w",
            name = "Original 20W Charger",
            price = "850 TK",
            numericPriceTk = 850,
            rating = "4.8 • 1.2k reviews",
            category = "Chargers",
            badge = "Best Seller",
            specs = "USB-C PD 3.0 Fast Charging • Smart IC Over-voltage Protection • 6 Months Warranty",
            imageRes = R.drawable.img_product_charger_1792969253818
        ),
        ShopProduct(
            id = "cable_typec",
            name = "Original Type-C Cable",
            price = "250 TK",
            numericPriceTk = 250,
            rating = "4.7 • 892 reviews",
            category = "Cables",
            badge = "5A Fast Charge",
            specs = "Braided Nylon 1m • 60W/5A High-Speed Data & Power • Reinforced Strain Relief",
            imageRes = R.drawable.img_product_cable_1792969254111
        ),
        ShopProduct(
            id = "mobile_cover",
            name = "Premium Mobile Cover",
            price = "350 TK",
            numericPriceTk = 350,
            rating = "4.9 • 2.3k reviews",
            category = "Covers",
            badge = "Shockproof",
            specs = "Air-Cushion Corners • Raised Camera Lip • Heat-Dissipating Inner Matrix",
            imageRes = R.drawable.img_product_cover_1792969254173
        ),
        ShopProduct(
            id = "charger_33w",
            name = "Original 33W Super Fast Charger",
            price = "1150 TK",
            numericPriceTk = 1150,
            rating = "4.9 • 640 reviews",
            category = "Chargers",
            badge = "33W Turbo",
            specs = "GaN Cool-Charge Tech • Supports PPS / PD / QC 4.0 • Includes Thermal Guard",
            imageRes = R.drawable.img_product_charger_1792969253818
        ),
        ShopProduct(
            id = "cable_pd_combo",
            name = "20W Charger + Type-C Cable Combo",
            price = "1000 TK",
            numericPriceTk = 1000,
            rating = "5.0 • 1.5k reviews",
            category = "Combos",
            badge = "Save 100 TK",
            specs = "Complete Original 20W Adapter + 5A Braided Type-C Cable • Cash on Delivery",
            imageRes = R.drawable.img_product_cable_1792969254111
        )
    )
}

class AlarmSoundController(private val context: Context) {
    private var alarmJob: Job? = null
    private var toneGenerator: ToneGenerator? = null

    fun startAlarm(enableSound: Boolean, enableVibration: Boolean, scope: CoroutineScope) {
        stopAlarm()
        alarmJob = scope.launch(Dispatchers.Default) {
            try {
                if (enableSound) {
                    toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                }
                val vibrator = getVibrator()
                while (isActive) {
                    if (enableSound) {
                        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 450)
                    }
                    if (enableVibration) {
                        vibrateOnce(vibrator, 450L)
                    }
                    delay(650L)
                }
            } catch (_: Exception) {
                // Ignore audio/vibrator hardware exceptions in restricted environments
            }
        }
    }

    fun stopAlarm() {
        alarmJob?.cancel()
        alarmJob = null
        try {
            toneGenerator?.stopTone()
            toneGenerator?.release()
        } catch (_: Exception) {
        }
        toneGenerator = null
        try {
            getVibrator()?.cancel()
        } catch (_: Exception) {
        }
    }

    private fun getVibrator(): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun vibrateOnce(vibrator: Vibrator?, durationMs: Long) {
        if (vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (_: Exception) {
        }
    }
}
