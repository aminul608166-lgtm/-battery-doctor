package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppDestination
import com.example.data.BatteryTelemetry
import com.example.data.ShopCatalog
import com.example.data.TelemetryHistoryEntry
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldCard
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldShopCard
import kotlin.math.roundToInt

private data class ToolCardItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val destination: AppDestination,
    val isShop: Boolean = false,
    val badgeText: String? = null,
    val testTag: String
)

@Composable
fun HomeScreen(
    telemetry: BatteryTelemetry,
    useFahrenheit: Boolean,
    fullAlarmOn: Boolean,
    theftAlarmArmed: Boolean,
    isAlarmRinging: Boolean,
    onStopAlarm: () -> Unit,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = listOf(
        ToolCardItem(
            icon = Icons.Filled.NotificationsActive,
            title = stringResource(R.string.tool_full_battery_title),
            subtitle = stringResource(R.string.tool_full_battery_subtitle),
            destination = AppDestination.FULL_BATTERY_ALARM,
            badgeText = if (fullAlarmOn) "ON" else null,
            testTag = "tool_full_battery"
        ),
        ToolCardItem(
            icon = Icons.Filled.Shield,
            title = stringResource(R.string.tool_theft_alarm_title),
            subtitle = stringResource(R.string.tool_theft_alarm_subtitle),
            destination = AppDestination.THEFT_ALARM,
            badgeText = if (theftAlarmArmed) "ARMED" else null,
            testTag = "tool_theft_alarm"
        ),
        ToolCardItem(
            icon = Icons.Filled.Power,
            title = stringResource(R.string.tool_charger_checker_title),
            subtitle = stringResource(R.string.tool_charger_checker_subtitle),
            destination = AppDestination.CHARGER_CHECKER,
            badgeText = "${telemetry.wattage.roundToInt()}W",
            testTag = "tool_charger_checker"
        ),
        ToolCardItem(
            icon = Icons.Filled.CameraAlt,
            title = stringResource(R.string.tool_ai_scanner_title),
            subtitle = stringResource(R.string.tool_ai_scanner_subtitle),
            destination = AppDestination.AI_CAMERA_SCANNER,
            badgeText = "AI",
            testTag = "tool_ai_scanner"
        ),
        ToolCardItem(
            icon = Icons.Filled.Psychology,
            title = stringResource(R.string.tool_ai_doctor_title),
            subtitle = stringResource(R.string.tool_ai_doctor_subtitle),
            destination = AppDestination.AI_BATTERY_DOCTOR,
            badgeText = "Doctor",
            testTag = "tool_ai_doctor"
        ),
        ToolCardItem(
            icon = Icons.Filled.ShoppingBag,
            title = stringResource(R.string.tool_shop_title),
            subtitle = stringResource(R.string.tool_shop_subtitle),
            destination = AppDestination.SHOP,
            isShop = true,
            badgeText = "COD",
            testTag = "tool_original_shop"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Ringing Alarm Banner
        item {
            AnimatedVisibility(visible = isAlarmRinging) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AlertRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_alarm_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ALARM RINGING!",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap Stop to silence siren & vibration",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Button(
                            onClick = onStopAlarm,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = AlertRed
                            ),
                            modifier = Modifier.testTag("stop_alarm_banner_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VolumeOff,
                                contentDescription = "Stop alarm",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Stop", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Top App Identity Header (matching Flutter layout)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(EmeraldPrimary, EmeraldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.BatteryChargingFull,
                        contentDescription = stringResource(R.string.app_name),
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.app_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Live Battery Status Hero Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("battery_status_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 1.dp
                        ) {
                            Text(
                                text = "${telemetry.level}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Battery ${telemetry.level}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "${telemetry.healthStatus} • ${telemetry.temperatureCondition} ${telemetry.formattedTemperature(useFahrenheit)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { onNavigate(AppDestination.SETTINGS) },
                            modifier = Modifier.testTag("hero_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Open Settings",
                                tint = EmeraldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (telemetry.level / 100f).coerceIn(0f, 1f) },
                        color = EmeraldPrimary,
                        trackColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⚡ ${telemetry.chargingSpeedLabel}",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${String.format("%.2fV", telemetry.voltageVolts)} • ${telemetry.plugType}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Tools Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tools_header),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.features_available),
                    style = MaterialTheme.typography.labelLarge,
                    color = EmeraldPrimary
                )
            }
        }

        // 2x3 Tools Grid
        items(tools.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (item in rowItems) {
                    ToolGridCard(
                        item = item,
                        onClick = { onNavigate(item.destination) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ToolGridCard(
    item: ToolCardItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = if (item.isShop) EmeraldShopCard else EmeraldCard
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isShop) 3.dp else 1.dp),
        modifier = modifier
            .height(142.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(item.testTag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            if (item.badgeText != null) {
                Surface(
                    color = EmeraldPrimary,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = item.badgeText,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.78f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A18),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2E4E37),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun StatsScreen(
    telemetry: BatteryTelemetry,
    history: List<TelemetryHistoryEntry>,
    useFahrenheit: Boolean,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)

    val remainingPercent = (100 - telemetry.level).coerceAtLeast(0)
    val estimatedMinutesToFull = if (telemetry.isCharging) {
        (remainingPercent * 1.4f).roundToInt()
    } else {
        (telemetry.level * 4.8f).roundToInt()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("stats_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Live Battery & Charger Stats",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Real-time sensor readings from Android BatteryManager",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard(
                    icon = Icons.Filled.Bolt,
                    label = "Power Flow",
                    value = "${telemetry.wattage} W",
                    subValue = "${telemetry.currentMa} mA • ${String.format("%.2fV", telemetry.voltageVolts)}",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = Icons.Filled.Thermostat,
                    label = "Temperature",
                    value = telemetry.formattedTemperature(useFahrenheit),
                    subValue = telemetry.temperatureCondition,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard(
                    icon = Icons.Filled.CheckCircle,
                    label = "Battery Health",
                    value = telemetry.healthStatus,
                    subValue = "${telemetry.technology} Cell",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = Icons.Filled.Power,
                    label = if (telemetry.isCharging) "Est. Full In" else "Est. Runtime",
                    value = "$estimatedMinutesToFull min",
                    subValue = telemetry.plugType,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Recent Telemetry Snapshots",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(history) { entry ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${entry.level}% • ${entry.plugType}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = entry.timeLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${entry.wattage}W • ${entry.temperatureCelsius}°C",
                        style = MaterialTheme.typography.labelLarge,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatMetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EmeraldPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Text(
                text = subValue,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsScreen(
    targetChargePercent: Int,
    onTargetChargeChange: (Int) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    useFahrenheit: Boolean,
    onUseFahrenheitChange: (Boolean) -> Unit,
    darkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Doctor Settings & Alerts",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Customize alarm thresholds, audio siren & display preferences",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Full Battery Alarm Threshold: $targetChargePercent%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = "80%–90% extends Li-ion cycle life; 100% maximizes single-charge capacity.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = targetChargePercent.toFloat(),
                        onValueChange = { onTargetChargeChange(it.roundToInt()) },
                        valueRange = 80f..100f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary
                        ),
                        modifier = Modifier.testTag("settings_threshold_slider")
                    )
                }
            }
        }

        item {
            SettingsToggleRow(
                title = "Alarm Siren Sound",
                subtitle = "Play loud tone on Full Battery or Theft Unplug",
                checked = soundEnabled,
                onCheckedChange = onSoundChange,
                testTag = "toggle_alarm_sound"
            )
        }

        item {
            SettingsToggleRow(
                title = "Vibration Pulse",
                subtitle = "Vibrate phone when alarm triggers",
                checked = vibrationEnabled,
                onCheckedChange = onVibrationChange,
                testTag = "toggle_vibration"
            )
        }

        item {
            SettingsToggleRow(
                title = "Display Temperature in °F",
                subtitle = "Switch between Celsius (°C) and Fahrenheit (°F)",
                checked = useFahrenheit,
                onCheckedChange = onUseFahrenheitChange,
                testTag = "toggle_fahrenheit"
            )
        }

        item {
            SettingsToggleRow(
                title = "Dark Mode Theme",
                subtitle = "AMOLED power-saving dark green theme",
                checked = darkThemeEnabled,
                onCheckedChange = onDarkThemeChange,
                testTag = "toggle_dark_theme"
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.SupportAgent,
                            contentDescription = "Customer Support",
                            tint = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Original Charger Shop Support",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                            Text(
                                text = "WhatsApp Helpline: ${ShopCatalog.DISPLAY_PHONE}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EmeraldDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val msg = Uri.encode("Salam vai, Battery & Charger Doctor app theke jogajog korchi.")
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/${ShopCatalog.WHATSAPP_NUMBER}?text=$msg")
                            )
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("whatsapp_support_button")
                    ) {
                        Text("Chat on WhatsApp (${ShopCatalog.DISPLAY_PHONE})")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldPrimary
                ),
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}
