package me.weishu.kernelsu.ui.screen.wallpaper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.weishu.kernelsu.R
import me.weishu.kernelsu.ui.component.wallpaper.WallpaperLayer
import me.weishu.kernelsu.ui.theme.LocalEnableBlur
import me.weishu.kernelsu.ui.util.BlurredBar
import me.weishu.kernelsu.ui.util.rememberBlurBackdrop
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlin.math.roundToInt

@Composable
fun WallpaperSettingsScreenMiuix(
    state: WallpaperSettingsUiState,
    onBack: () -> Unit,
    onSetEnabled: (Boolean) -> Unit,
    onPickImage: () -> Unit,
    onClearImage: () -> Unit,
    onSetBlur: (Float) -> Unit,
    onSetDim: (Float) -> Unit,
    onSetApplyHome: (Boolean) -> Unit,
    onSetApplyModule: (Boolean) -> Unit,
    onSetApplySettings: (Boolean) -> Unit,
    onSetModuleCardEnabled: (Boolean) -> Unit,
    error: String?,
) {
    val scrollBehavior = MiuixScrollBehavior()
    val enableBlur = LocalEnableBlur.current
    val backdrop = rememberBlurBackdrop(enableBlur)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop) {
                TopAppBar(
                    color = barColor,
                    title = stringResource(R.string.wallpaper_settings),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = MiuixIcons.Back,
                                contentDescription = null,
                                tint = colorScheme.onBackground
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        popupHost = { },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .scrollEndHaptic()
                    .overScrollVertical()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = 12.dp),
                contentPadding = innerPadding,
                overscrollEffect = null,
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // 壁纸预览
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(18.dp))
                    ) {
                        if (state.enabled && !state.path.isNullOrBlank()) {
                            WallpaperLayer(
                                path = state.path,
                                blur = state.blur,
                                dim = state.dim,
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colorScheme.surfaceVariant)
                            )
                        }
                        Text(
                            text = stringResource(R.string.wallpaper_preview),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp),
                            color = colorScheme.onBackground.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    ) {
                        SwitchPreference(
                            title = stringResource(R.string.wallpaper_enable),
                            summary = stringResource(R.string.wallpaper_enable_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Wallpaper,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_enable),
                                    tint = colorScheme.onBackground
                                )
                            },
                            checked = state.enabled,
                            onCheckedChange = onSetEnabled
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    ) {
                        ArrowPreference(
                            title = stringResource(R.string.wallpaper_select_image),
                            summary = if (state.path.isNullOrBlank()) {
                                stringResource(R.string.wallpaper_select_image_summary)
                            } else {
                                stringResource(R.string.wallpaper_use_image)
                            },
                            startAction = {
                                Icon(
                                    Icons.Rounded.Image,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_select_image),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            onClick = onPickImage
                        )
                        ArrowPreference(
                            title = stringResource(R.string.wallpaper_clear_image),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Delete,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_clear_image),
                                    tint = if (state.path.isNullOrBlank()) colorScheme.disabledOnSecondaryVariant else colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled && !state.path.isNullOrBlank(),
                            onClick = onClearImage
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    ) {
                        WallpaperSliderRow(
                            title = stringResource(R.string.wallpaper_blur_strength),
                            value = state.blur,
                            valueText = "${state.blur.roundToInt()}",
                            icon = {
                                Icon(
                                    Icons.Rounded.BlurOn,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_blur_strength),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            valueRange = 0f..40f,
                            onValueChange = onSetBlur
                        )
                        WallpaperSliderRow(
                            title = stringResource(R.string.wallpaper_darkening),
                            value = state.dim,
                            valueText = "${(state.dim * 100).roundToInt()}%",
                            icon = {
                                Icon(
                                    Icons.Rounded.DarkMode,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_darkening),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            valueRange = 0f..0.8f,
                            onValueChange = onSetDim
                        )
                    }

                    SectionTitle(stringResource(R.string.wallpaper_apply_to))
                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    ) {
                        SwitchPreference(
                            title = stringResource(R.string.wallpaper_apply_home),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Wallpaper,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_apply_home),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            checked = state.applyHome,
                            onCheckedChange = onSetApplyHome
                        )
                        SwitchPreference(
                            title = stringResource(R.string.wallpaper_apply_module),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Wallpaper,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_apply_module),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            checked = state.applyModule,
                            onCheckedChange = onSetApplyModule
                        )
                        SwitchPreference(
                            title = stringResource(R.string.wallpaper_apply_settings),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Wallpaper,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_apply_settings),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled,
                            checked = state.applySettings,
                            onCheckedChange = onSetApplySettings
                        )
                    }

                    SectionTitle(stringResource(R.string.wallpaper_module_card))
                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    ) {
                        SwitchPreference(
                            title = stringResource(R.string.wallpaper_module_card_enable),
                            summary = stringResource(R.string.wallpaper_module_card_enable_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Wallpaper,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(R.string.wallpaper_module_card_enable),
                                    tint = colorScheme.onBackground
                                )
                            },
                            enabled = state.enabled && state.applyModule,
                            checked = state.moduleCardEnabled,
                            onCheckedChange = onSetModuleCardEnabled
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun WallpaperSliderRow(
    title: String,
    value: Float,
    valueText: String,
    icon: @Composable () -> Unit,
    enabled: Boolean,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    var local by remember { mutableFloatStateOf(value) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = if (enabled) colorScheme.onBackground else colorScheme.disabledOnSecondaryVariant,
                fontSize = 15.sp,
            )
            Text(
                text = valueText,
                color = if (enabled) colorScheme.onBackground.copy(alpha = 0.6f) else colorScheme.disabledOnSecondaryVariant,
                fontSize = 13.sp,
            )
        }
        Slider(
            value = local,
            onValueChange = {
                local = it
                onValueChange(it)
            },
            enabled = enabled,
            valueRange = valueRange,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        color = colorScheme.onBackground.copy(alpha = 0.6f),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
    )
}
