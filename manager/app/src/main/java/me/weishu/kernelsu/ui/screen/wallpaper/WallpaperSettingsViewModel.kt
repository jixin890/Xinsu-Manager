package me.weishu.kernelsu.ui.screen.wallpaper

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import me.weishu.kernelsu.data.repository.SettingsRepository
import me.weishu.kernelsu.data.repository.SettingsRepositoryImpl

@Immutable
data class WallpaperSettingsUiState(
    val enabled: Boolean = false,
    val path: String? = null,
    val blur: Float = 12f,
    val dim: Float = 0.3f,
    val applyHome: Boolean = true,
    val applyModule: Boolean = true,
    val applySettings: Boolean = true,
    val moduleCardEnabled: Boolean = false,
)

class WallpaperSettingsViewModel(
    private val repo: SettingsRepository = SettingsRepositoryImpl(),
) : ViewModel() {

    private var _uiState = load()
    fun uiState() = _uiState

    private fun load(): WallpaperSettingsUiState = WallpaperSettingsUiState(
        enabled = repo.wallpaperEnabled,
        path = repo.wallpaperPath,
        blur = repo.wallpaperBlur,
        dim = repo.wallpaperDim,
        applyHome = repo.wallpaperApplyHome,
        applyModule = repo.wallpaperApplyModule,
        applySettings = repo.wallpaperApplySettings,
        moduleCardEnabled = repo.moduleCardWallpaperEnabled,
    )

    private fun update(transform: (WallpaperSettingsUiState) -> WallpaperSettingsUiState) {
        _uiState = transform(_uiState)
    }

    fun setEnabled(enabled: Boolean) {
        repo.wallpaperEnabled = enabled
        update { it.copy(enabled = enabled) }
    }

    fun setPath(path: String?) {
        repo.wallpaperPath = path
        update { it.copy(path = path) }
    }

    fun setBlur(blur: Float) {
        repo.wallpaperBlur = blur
        update { it.copy(blur = blur) }
    }

    fun setDim(dim: Float) {
        repo.wallpaperDim = dim
        update { it.copy(dim = dim) }
    }

    fun setApplyHome(apply: Boolean) {
        repo.wallpaperApplyHome = apply
        update { it.copy(applyHome = apply) }
    }

    fun setApplyModule(apply: Boolean) {
        repo.wallpaperApplyModule = apply
        update { it.copy(applyModule = apply) }
    }

    fun setApplySettings(apply: Boolean) {
        repo.wallpaperApplySettings = apply
        update { it.copy(applySettings = apply) }
    }

    fun setModuleCardEnabled(enabled: Boolean) {
        repo.moduleCardWallpaperEnabled = enabled
        update { it.copy(moduleCardEnabled = enabled) }
    }
}
