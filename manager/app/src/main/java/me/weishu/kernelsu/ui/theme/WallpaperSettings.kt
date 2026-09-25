package me.weishu.kernelsu.ui.theme

import me.weishu.kernelsu.data.repository.SettingsRepository
import me.weishu.kernelsu.data.repository.SettingsRepositoryImpl

/**
 * 自定义壁纸设置（参考 FolkPatch：图片壁纸 + 模糊 + 暗化遮罩 + 按页面应用 + 模块卡片壁纸）。
 */
data class WallpaperSettings(
    val enabled: Boolean = false,
    val path: String? = null,
    val blur: Float = 12f,
    val dim: Float = 0.3f,
    val applyHome: Boolean = true,
    val applyModule: Boolean = true,
    val applySettings: Boolean = true,
    val moduleCardEnabled: Boolean = false,
) {
    /** 主页面索引：0 = Home, 2 = Module, 3 = Settings */
    fun appliedToPage(page: Int): Boolean = when (page) {
        0 -> applyHome
        2 -> applyModule
        3 -> applySettings
        else -> false
    }
}

object WallpaperController {
    fun getWallpaperSettings(repo: SettingsRepository = SettingsRepositoryImpl()): WallpaperSettings =
        WallpaperSettings(
            enabled = repo.wallpaperEnabled,
            path = repo.wallpaperPath,
            blur = repo.wallpaperBlur,
            dim = repo.wallpaperDim,
            applyHome = repo.wallpaperApplyHome,
            applyModule = repo.wallpaperApplyModule,
            applySettings = repo.wallpaperApplySettings,
            moduleCardEnabled = repo.moduleCardWallpaperEnabled,
        )
}
