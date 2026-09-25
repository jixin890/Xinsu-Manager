package me.weishu.kernelsu.ui.screen.wallpaper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.weishu.kernelsu.R
import me.weishu.kernelsu.ui.component.material.ExpressiveScaffold
import me.weishu.kernelsu.ui.component.material.SegmentedColumn
import me.weishu.kernelsu.ui.component.material.SegmentedListItem
import me.weishu.kernelsu.ui.component.material.SegmentedSwitchItem
import me.weishu.kernelsu.ui.component.material.expressiveTopAppBarColors
import me.weishu.kernelsu.ui.component.wallpaper.WallpaperLayer
import top.yukonga.miuix.kmp.basic.Slider
import kotlin.math.roundToInt

@Composable
fun WallpaperSettingsScreenMaterial(
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    ExpressiveScaffold(
        topBar = {
            TopBar(scrollBehavior = scrollBehavior, onBack = onBack)
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
        ) {
            // 壁纸预览
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
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
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    )
                }
                Text(
                    text = stringResource(R.string.wallpaper_preview),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            SegmentedColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 13.dp, top = 13.dp),
                content = listOf(
                    {
                        SegmentedSwitchItem(
                            icon = Icons.Filled.Wallpaper,
                            title = stringResource(R.string.wallpaper_enable),
                            summary = stringResource(R.string.wallpaper_enable_summary),
                            checked = state.enabled,
                            onCheckedChange = onSetEnabled
                        )
                    }
                )
            )

            SegmentedColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 13.dp),
                content = buildList {
                    add {
                        SegmentedListItem(
                            onClick = onPickImage,
                            enabled = state.enabled,
                            headlineContent = { Text(stringResource(R.string.wallpaper_select_image)) },
                            supportingContent = {
                                Text(
                                    if (state.path.isNullOrBlank()) {
                                        stringResource(R.string.wallpaper_select_image_summary)
                                    } else {
                                        stringResource(R.string.wallpaper_use_image)
                                    }
                                )
                            },
                            leadingContent = {
                                Icon(Icons.Filled.Image, stringResource(R.string.wallpaper_select_image))
                            },
                            trailingContent = {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                            }
                        )
                    }
                    if (!state.path.isNullOrBlank()) {
                        add {
                            SegmentedListItem(
                                onClick = onClearImage,
                                enabled = state.enabled,
                                headlineContent = { Text(stringResource(R.string.wallpaper_clear_image)) },
                                leadingContent = {
                                    Icon(Icons.Filled.Delete, stringResource(R.string.wallpaper_clear_image))
                                },
                                trailingContent = {
                                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                                }
                            )
                        }
                    }
                }
            )

            SegmentedColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 13.dp),
                content = listOf(
                    {
                        WallpaperSliderItem(
                            icon = Icons.Filled.BlurOn,
                            title = stringResource(R.string.wallpaper_blur_strength),
                            value = state.blur,
                            valueText = "${state.blur.roundToInt()}",
                            enabled = state.enabled,
                            valueRange = 0f..40f,
                            onValueChange = onSetBlur
                        )
                    },
                    {
                        WallpaperSliderItem(
                            icon = Icons.Filled.DarkMode,
                            title = stringResource(R.string.wallpaper_darkening),
                            value = state.dim,
                            valueText = "${(state.dim * 100).roundToInt()}%",
                            enabled = state.enabled,
                            valueRange = 0f..0.8f,
                            onValueChange = onSetDim
                        )
                    }
                )
            )

            SectionTitleMaterial(stringResource(R.string.wallpaper_apply_to))
            SegmentedColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 13.dp, top = 4.dp),
                content = listOf(
                    {
                        SegmentedSwitchItem(
                            icon = Icons.Filled.Wallpaper,
                            title = stringResource(R.string.wallpaper_apply_home),
                            enabled = state.enabled,
                            checked = state.applyHome,
                            onCheckedChange = onSetApplyHome
                        )
                    },
                    {
                        SegmentedSwitchItem(
                            icon = Icons.Filled.Wallpaper,
                            title = stringResource(R.string.wallpaper_apply_module),
                            enabled = state.enabled,
                            checked = state.applyModule,
                            onCheckedChange = onSetApplyModule
                        )
                    },
                    {
                        SegmentedSwitchItem(
                            icon = Icons.Filled.Wallpaper,
                            title = stringResource(R.string.wallpaper_apply_settings),
                            enabled = state.enabled,
                            checked = state.applySettings,
                            onCheckedChange = onSetApplySettings
                        )
                    }
                )
            )

            SectionTitleMaterial(stringResource(R.string.wallpaper_module_card))
            SegmentedColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 13.dp, top = 4.dp),
                content = listOf(
                    {
                        SegmentedSwitchItem(
                            icon = Icons.Filled.Wallpaper,
                            title = stringResource(R.string.wallpaper_module_card_enable),
                            summary = stringResource(R.string.wallpaper_module_card_enable_summary),
                            enabled = state.enabled && state.applyModule,
                            checked = state.moduleCardEnabled,
                            onCheckedChange = onSetModuleCardEnabled
                        )
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WallpaperSliderItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: Float,
    valueText: String,
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
            Icon(icon, title, modifier = Modifier.padding(end = 8.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
private fun SectionTitleMaterial(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBack: () -> Unit,
) {
    LargeFlexibleTopAppBar(
        title = { Text(stringResource(R.string.wallpaper_settings)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        colors = expressiveTopAppBarColors(),
        windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        scrollBehavior = scrollBehavior
    )
}
