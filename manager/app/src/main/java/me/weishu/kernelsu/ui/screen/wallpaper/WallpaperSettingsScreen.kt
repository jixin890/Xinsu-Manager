package me.weishu.kernelsu.ui.screen.wallpaper

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import me.weishu.kernelsu.ui.LocalUiMode
import me.weishu.kernelsu.ui.UiMode
import me.weishu.kernelsu.ui.navigation3.LocalNavigator
import me.weishu.kernelsu.ui.component.wallpaper.saveWallpaperFromUri
import me.weishu.kernelsu.ui.component.wallpaper.clearWallpaperFile

/**
 * 自定义壁纸设置页（参考 FolkPatch）：
 * 图片壁纸 + 模糊/暗化 + 按页面应用 + 模块卡片壁纸。
 */
@Composable
fun WallpaperSettingsScreen() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    val viewModel = viewModel<WallpaperSettingsViewModel>()
    val state = viewModel.uiState()

    var pendingError by remember { mutableStateOf<String?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val saved = saveWallpaperFromUri(context, uri)
        if (saved != null) {
            viewModel.setPath(saved)
        } else {
            pendingError = "Failed to save wallpaper"
        }
    }

    when (LocalUiMode.current) {
        UiMode.Miuix -> WallpaperSettingsScreenMiuix(
            state = state,
            onBack = { navigator.pop() },
            onSetEnabled = viewModel::setEnabled,
            onPickImage = { pickImage.launch("image/*") },
            onClearImage = {
                clearWallpaperFile(context)
                viewModel.setPath(null)
            },
            onSetBlur = viewModel::setBlur,
            onSetDim = viewModel::setDim,
            onSetApplyHome = viewModel::setApplyHome,
            onSetApplyModule = viewModel::setApplyModule,
            onSetApplySettings = viewModel::setApplySettings,
            onSetModuleCardEnabled = viewModel::setModuleCardEnabled,
            error = pendingError,
        )

        UiMode.Material -> WallpaperSettingsScreenMaterial(
            state = state,
            onBack = { navigator.pop() },
            onSetEnabled = viewModel::setEnabled,
            onPickImage = { pickImage.launch("image/*") },
            onClearImage = {
                clearWallpaperFile(context)
                viewModel.setPath(null)
            },
            onSetBlur = viewModel::setBlur,
            onSetDim = viewModel::setDim,
            onSetApplyHome = viewModel::setApplyHome,
            onSetApplyModule = viewModel::setApplyModule,
            onSetApplySettings = viewModel::setApplySettings,
            onSetModuleCardEnabled = viewModel::setModuleCardEnabled,
            error = pendingError,
        )
    }
}
