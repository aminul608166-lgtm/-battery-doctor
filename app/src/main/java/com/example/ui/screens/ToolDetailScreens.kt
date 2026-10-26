package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BatteryTelemetry
import com.example.data.ChargerBenchmarkResult
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolTopBar(
    title: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = EmeraldPrimary
        )
    )
}

@Composable
fun FullBatteryAlarmScreen(
    telemetry: BatteryTelemetry,
    alarmOn: Boolean,
    targetPercent: Int,
    isRinging: Boolean,
    onAlarmToggle: (Boolean) -> Unit,
    onTargetPercentChange: (Int) -> Unit,
    onTestOrStopAlarm: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Scaffold(
        topBar = { ToolTopBar(title = "Full Battery Alarm", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .testTag("full_battery_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(EmeraldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.BatteryFull,
                    contentDescription = "Full Battery Alarm",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(84.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Alarm will ring at $targetPercent%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Current Battery: ${telemetry.level}% • ${if (telemetry.isCharging) "Charging" else "Unplugged"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))
            Switch(
                checked = alarmOn,
                onCheckedChange = onAlarmToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldPrimary
                ),
                modifier = Modifier.testTag("full_battery_switch")
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (alarmOn) "Alarm ON" else "Alarm OFF",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (alarmOn) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Target Charge Level: $targetPercent%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Slider(
                        value = targetPercent.toFloat(),
                        onValueChange = { onTargetPercentChange(it.roundToInt()) },
                        valueRange = 80f..100f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary
                        )
                    )
                    Text(
                        text = "Tip: Setting alarm at 85%–90% prevents high-voltage trickle wear.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onTestOrStopAlarm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRinging) AlertRed else EmeraldPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("test_full_alarm_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = "Test Siren"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRinging) "Stop Ringing Alarm" else "Test Full Battery Alarm Now",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TheftAlarmScreen(
    telemetry: BatteryTelemetry,
    armed: Boolean,
    isRinging: Boolean,
    onArmToggle: (Boolean) -> Unit,
    onTestOrStopSiren: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Scaffold(
        topBar = { ToolTopBar(title = "Theft Alarm", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .testTag("theft_alarm_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(if (armed || isRinging) AlertRed.copy(alpha = 0.14f) else EmeraldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = "Theft Protection",
                    tint = if (armed || isRinging) AlertRed else EmeraldPrimary,
                    modifier = Modifier.size(84.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = if (armed) "Theft Protection Active" else "Theft Protection Standby",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (armed) AlertRed else MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "If charger is unplugged, loud alarm will ring",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
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
                            text = if (armed) "Anti-Theft Armed" else "Arm Unplug Guard",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Power Status: ${telemetry.plugType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = armed,
                        onCheckedChange = onArmToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AlertRed
                        ),
                        modifier = Modifier.testTag("theft_alarm_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onTestOrStopSiren,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRinging) AlertRed else EmeraldPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("test_theft_siren_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = "Test Unplug Siren"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRinging) "Disarm & Stop Siren" else "Simulate Unplug / Test Siren",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ChargerCheckerScreen(
    telemetry: BatteryTelemetry,
    isBenchmarkRunning: Boolean,
    benchmarkProgress: Float,
    benchmarkResult: ChargerBenchmarkResult?,
    onRunBenchmark: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Scaffold(
        topBar = { ToolTopBar(title = "Charger Checker", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("charger_checker_screen"),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Power,
                            contentDescription = "Charger Checker",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Charging Speed: ${telemetry.chargingSpeedLabel}\nCable: ${telemetry.cableCondition}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ChargerStatPill(label = "Wattage", value = "${telemetry.wattage} W")
                            ChargerStatPill(label = "Current", value = "${telemetry.currentMa} mA")
                            ChargerStatPill(label = "Voltage", value = String.format("%.2f V", telemetry.voltageVolts))
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onRunBenchmark,
                    enabled = !isBenchmarkRunning,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("run_charger_benchmark_button")
                ) {
                    Icon(imageVector = Icons.Filled.Speed, contentDescription = "Benchmark")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBenchmarkRunning) "Testing Charger & Cable..." else "Run Live Charger & Cable Benchmark",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isBenchmarkRunning) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = { benchmarkProgress },
                            color = EmeraldPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sampling voltage ripple & cable resistance (${(benchmarkProgress * 100).roundToInt()}%)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (benchmarkResult != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = EmeraldPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Benchmark Diagnostic Result",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text("• Peak Output: ${benchmarkResult.peakWatts} W (Avg: ${benchmarkResult.averageWatts} W)")
                            Text("• Average Current: ${benchmarkResult.averageCurrentMa} mA")
                            Text("• Voltage Stability: ${benchmarkResult.voltageStabilityPercent}% Clean DC")
                            Text("• Cable Health: ${benchmarkResult.cableVerdict}")
                            Text(
                                text = "• Adapter Verdict: ${benchmarkResult.adapterVerdict}",
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChargerStatPill(label: String, value: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = EmeraldDark.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AICameraScannerScreen(
    selectedBitmap: Bitmap?,
    selectedFocus: String,
    isLoading: Boolean,
    report: String?,
    onBitmapSelected: (Bitmap?) -> Unit,
    onFocusSelected: (String) -> Unit,
    onRunScan: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onBitmapSelected(bitmap)
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                onBitmapSelected(bmp)
            } catch (_: Exception) {
            }
        }
    }

    val focusOptions = listOf(
        "Original vs Fake 20W Charger Check",
        "Type-C Cable Fray / Pin Damage",
        "Battery Swelling & Overheat Check"
    )

    Scaffold(
        topBar = { ToolTopBar(title = "AI Camera Scanner", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("ai_scanner_screen"),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (selectedBitmap != null) {
                            Image(
                                bitmap = selectedBitmap.asImageBitmap(),
                                contentDescription = "Scanned Charger or Battery",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "AI Camera Scanner",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(76.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Capture or pick a photo of your charger adapter, cable connector, or battery for Gemini AI inspection",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = EmeraldDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { cameraLauncher.launch(null) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("capture_camera_button")
                            ) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = "Camera", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Camera")
                            }
                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pick_gallery_button")
                            ) {
                                Icon(Icons.Filled.PhotoLibrary, contentDescription = "Gallery", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Gallery")
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Select Inspection Focus:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        focusOptions.forEach { option ->
                            FilterChip(
                                selected = selectedFocus == option,
                                onClick = { onFocusSelected(option) },
                                label = { Text(option) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onRunScan,
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("scan_charger_ai_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Scanning with Gemini AI...")
                    } else {
                        Icon(Icons.Filled.Bolt, contentDescription = "Scan")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan Charger with AI", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (report != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = report,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AIBatteryDoctorScreen(
    telemetry: BatteryTelemetry,
    isLoading: Boolean,
    diagnosis: String?,
    onAskDoctor: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    var customQuestion by remember { mutableStateOf("") }

    Scaffold(
        topBar = { ToolTopBar(title = "AI Battery Doctor", onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("ai_doctor_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Essential Tips Card matching the user's code
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Psychology,
                                contentDescription = "AI Battery Doctor",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "AI Tips:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "• Don't charge overnight\n• Use original charger\n• Keep battery 20-80% for long life",
                            style = MaterialTheme.typography.bodyLarge,
                            color = EmeraldDark,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "বাংলা টিপস: সারারাত চার্জ দেবেন না • সর্বদা অরিজিনাল চার্জার ব্যবহার করুন • দীর্ঘস্থায়ী ব্যাটারির জন্য ২০-৮০% চার্জ রাখুন।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EmeraldDark.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Interactive Gemini AI Diagnostics
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ask Gemini AI Battery Doctor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Telemetry attached: ${telemetry.level}% • ${telemetry.temperatureCelsius}°C • ${telemetry.wattage}W",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = customQuestion,
                            onValueChange = { customQuestion = it },
                            label = { Text("Ask about overheating, slow charge, battery drain...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("doctor_question_input")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onAskDoctor(customQuestion) },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("ask_doctor_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Running AI Diagnostics...")
                            } else {
                                Icon(Icons.Filled.Send, contentDescription = "Diagnose", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Run Full AI Battery Diagnosis", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (diagnosis != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = diagnosis,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
