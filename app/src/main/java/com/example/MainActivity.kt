package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDestination
import com.example.ui.BatteryDoctorViewModel
import com.example.ui.screens.AIBatteryDoctorScreen
import com.example.ui.screens.AICameraScannerScreen
import com.example.ui.screens.ChargerCheckerScreen
import com.example.ui.screens.FullBatteryAlarmScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TheftAlarmScreen
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: BatteryDoctorViewModel = viewModel()
            val darkTheme by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = darkTheme) {
                BatteryDoctorAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BatteryDoctorAppContent(
    viewModel: BatteryDoctorViewModel = viewModel()
) {
    val destination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val telemetry by viewModel.telemetry.collectAsStateWithLifecycle()
    val history by viewModel.historyEntries.collectAsStateWithLifecycle()

    val fullAlarmOn by viewModel.fullBatteryAlarmOn.collectAsStateWithLifecycle()
    val targetChargePercent by viewModel.targetChargePercent.collectAsStateWithLifecycle()
    val isFullAlarmRinging by viewModel.isFullAlarmRinging.collectAsStateWithLifecycle()

    val theftArmed by viewModel.theftAlarmArmed.collectAsStateWithLifecycle()
    val isTheftRinging by viewModel.isTheftAlarmRinging.collectAsStateWithLifecycle()

    val isBenchmarkRunning by viewModel.isBenchmarkRunning.collectAsStateWithLifecycle()
    val benchmarkProgress by viewModel.benchmarkProgress.collectAsStateWithLifecycle()
    val benchmarkResult by viewModel.benchmarkResult.collectAsStateWithLifecycle()

    val scannerBitmap by viewModel.scannerBitmap.collectAsStateWithLifecycle()
    val scannerFocus by viewModel.scannerFocus.collectAsStateWithLifecycle()
    val isScannerLoading by viewModel.isScannerLoading.collectAsStateWithLifecycle()
    val scannerReport by viewModel.scannerReport.collectAsStateWithLifecycle()

    val isDoctorLoading by viewModel.isDoctorLoading.collectAsStateWithLifecycle()
    val doctorDiagnosis by viewModel.doctorDiagnosis.collectAsStateWithLifecycle()

    val selectedShopCategory by viewModel.selectedShopCategory.collectAsStateWithLifecycle()

    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsStateWithLifecycle()
    val useFahrenheit by viewModel.useFahrenheit.collectAsStateWithLifecycle()
    val darkThemeEnabled by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()

    val isMainTab = destination == AppDestination.HOME ||
        destination == AppDestination.STATS ||
        destination == AppDestination.SETTINGS

    if (isMainTab) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    NavigationBarItem(
                        selected = destination == AppDestination.HOME,
                        onClick = { viewModel.navigateTo(AppDestination.HOME) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = stringResource(R.string.nav_home)
                            )
                        },
                        label = { Text(stringResource(R.string.nav_home)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldPrimary,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldLight
                        ),
                        modifier = Modifier.testTag("nav_home_tab")
                    )
                    NavigationBarItem(
                        selected = destination == AppDestination.STATS,
                        onClick = { viewModel.navigateTo(AppDestination.STATS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.BarChart,
                                contentDescription = stringResource(R.string.nav_stats)
                            )
                        },
                        label = { Text(stringResource(R.string.nav_stats)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldPrimary,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldLight
                        ),
                        modifier = Modifier.testTag("nav_stats_tab")
                    )
                    NavigationBarItem(
                        selected = destination == AppDestination.SETTINGS,
                        onClick = { viewModel.navigateTo(AppDestination.SETTINGS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(R.string.nav_settings)
                            )
                        },
                        label = { Text(stringResource(R.string.nav_settings)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldPrimary,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldLight
                        ),
                        modifier = Modifier.testTag("nav_settings_tab")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (destination) {
                    AppDestination.HOME -> HomeScreen(
                        telemetry = telemetry,
                        useFahrenheit = useFahrenheit,
                        fullAlarmOn = fullAlarmOn,
                        theftAlarmArmed = theftArmed,
                        isAlarmRinging = isFullAlarmRinging || isTheftRinging,
                        onStopAlarm = { viewModel.stopAllAlarms() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    AppDestination.STATS -> StatsScreen(
                        telemetry = telemetry,
                        history = history,
                        useFahrenheit = useFahrenheit,
                        onBackToHome = { viewModel.navigateBackToHome() }
                    )
                    AppDestination.SETTINGS -> SettingsScreen(
                        targetChargePercent = targetChargePercent,
                        onTargetChargeChange = { viewModel.setTargetChargePercent(it) },
                        soundEnabled = soundEnabled,
                        onSoundChange = { viewModel.setSoundEnabled(it) },
                        vibrationEnabled = vibrationEnabled,
                        onVibrationChange = { viewModel.setVibrationEnabled(it) },
                        useFahrenheit = useFahrenheit,
                        onUseFahrenheitChange = { viewModel.setUseFahrenheit(it) },
                        darkThemeEnabled = darkThemeEnabled,
                        onDarkThemeChange = { viewModel.setDarkThemeEnabled(it) },
                        onBackToHome = { viewModel.navigateBackToHome() }
                    )
                    else -> {}
                }
            }
        }
    } else {
        when (destination) {
            AppDestination.FULL_BATTERY_ALARM -> FullBatteryAlarmScreen(
                telemetry = telemetry,
                alarmOn = fullAlarmOn,
                targetPercent = targetChargePercent,
                isRinging = isFullAlarmRinging,
                onAlarmToggle = { viewModel.setFullBatteryAlarmOn(it) },
                onTargetPercentChange = { viewModel.setTargetChargePercent(it) },
                onTestOrStopAlarm = { viewModel.triggerTestFullBatteryAlarm() },
                onBack = { viewModel.navigateBackToHome() }
            )
            AppDestination.THEFT_ALARM -> TheftAlarmScreen(
                telemetry = telemetry,
                armed = theftArmed,
                isRinging = isTheftRinging,
                onArmToggle = { viewModel.setTheftAlarmArmed(it) },
                onTestOrStopSiren = { viewModel.triggerTestTheftAlarm() },
                onBack = { viewModel.navigateBackToHome() }
            )
            AppDestination.CHARGER_CHECKER -> ChargerCheckerScreen(
                telemetry = telemetry,
                isBenchmarkRunning = isBenchmarkRunning,
                benchmarkProgress = benchmarkProgress,
                benchmarkResult = benchmarkResult,
                onRunBenchmark = { viewModel.runChargerBenchmark() },
                onBack = { viewModel.navigateBackToHome() }
            )
            AppDestination.AI_CAMERA_SCANNER -> AICameraScannerScreen(
                selectedBitmap = scannerBitmap,
                selectedFocus = scannerFocus,
                isLoading = isScannerLoading,
                report = scannerReport,
                onBitmapSelected = { viewModel.setScannerBitmap(it) },
                onFocusSelected = { viewModel.setScannerFocus(it) },
                onRunScan = { viewModel.runAiCameraScan() },
                onBack = { viewModel.navigateBackToHome() }
            )
            AppDestination.AI_BATTERY_DOCTOR -> AIBatteryDoctorScreen(
                telemetry = telemetry,
                isLoading = isDoctorLoading,
                diagnosis = doctorDiagnosis,
                onAskDoctor = { viewModel.askAiBatteryDoctor(it) },
                onBack = { viewModel.navigateBackToHome() }
            )
            AppDestination.SHOP -> ShopScreen(
                products = viewModel.getFilteredProducts(),
                selectedCategory = selectedShopCategory,
                onCategorySelected = { viewModel.setSelectedShopCategory(it) },
                onBack = { viewModel.navigateBackToHome() }
            )
            else -> {}
        }
    }
}
