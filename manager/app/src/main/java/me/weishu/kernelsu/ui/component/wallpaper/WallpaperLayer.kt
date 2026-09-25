package me.weishu.kernelsu.ui.component.wallpaper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.weishu.kernelsu.ui.theme.WallpaperSettings
import java.io.File

private const val DECODE_MAX_SIDE = 1600

/** 将相册图片复制到应用私有目录并返回路径（用于持久化壁纸）。 */
fun saveWallpaperFromUri(context: Context, uri: Uri): String? {
    val dir = File(context.filesDir, "wallpaper").apply { mkdirs() }
    // 清理旧壁纸，避免累积占用空间
    dir.listFiles()?.forEach { it.delete() }
    val dest = File(dir, "wallpaper.img")
    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        }
        dest.absolutePath
    } catch (_: Exception) {
        null
    }
}

/** 清除壁纸文件。 */
fun clearWallpaperFile(context: Context) {
    val dir = File(context.filesDir, "wallpaper")
    dir.listFiles()?.forEach { it.delete() }
}

/** 按目标尺寸采样解码图片，避免大图 OOM。 */
private fun decodeSampledBitmap(path: String?, targetSize: Int): Bitmap? {
    if (path.isNullOrBlank()) return null
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(path, bounds)
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
    var sampleSize = 1
    while (bounds.outWidth / sampleSize > targetSize || bounds.outHeight / sampleSize > targetSize) {
        sampleSize *= 2
    }
    return BitmapFactory.decodeFile(
        path,
        BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
    )
}

@Composable
private fun rememberWallpaperBitmap(path: String?): ImageBitmap? {
    var bitmap by remember(path) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(path) {
        bitmap = withContext(Dispatchers.IO) {
            decodeSampledBitmap(path, DECODE_MAX_SIDE)?.asImageBitmap()
        }
    }
    return bitmap
}

/** Android 12+ 使用高斯模糊；低版本自动降级为不模糊。 */
private fun Modifier.blurEffect(radiusDp: Float): Modifier =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && radiusDp > 0f) {
        this.blur(radiusDp.dp)
    } else {
        this
    }

/**
 * 壁纸图层：图片 + 高斯模糊 + 暗化遮罩。
 * 适用于页面背景与模块卡片背景。
 */
@Composable
fun WallpaperLayer(
    path: String?,
    blur: Float,
    dim: Float,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = 1f,
) {
    val bitmap = rememberWallpaperBitmap(path)
    Box(modifier) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .matchParentSize()
                    .blurEffect(blur)
                    .alpha(alpha),
            )
        }
        if (dim > 0f) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = dim * alpha))
            )
        }
    }
}

/**
 * 主页面背景壁纸：按当前页面决定是否显示。
 * 页面索引：0 = Home, 1 = SuperUser, 2 = Module, 3 = Settings
 */
@Composable
fun WallpaperBackground(
    wallpaper: WallpaperSettings,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    if (!wallpaper.enabled || wallpaper.path.isNullOrBlank() || !wallpaper.appliedToPage(currentPage)) return
    WallpaperLayer(
        path = wallpaper.path,
        blur = wallpaper.blur,
        dim = wallpaper.dim,
        modifier = modifier.fillMaxSize(),
    )
}

/** 主题色预设（FolkPatch 风格，点击即换肤）。 */
val PresetThemeColors: List<Long> = listOf(
    0xFF1E1B4B, // KernelSU 靛蓝
    0xFF6750A4, // Material 紫
    0xFF0D47A1, // 蓝
    0xFF00838F, // 青
    0xFF00796B, // 蓝绿
    0xFF2E7D32, // 绿
    0xFFF9A825, // 琥珀
    0xFFE64A19, // 橙
    0xFFC62828, // 红
    0xFFB71C1C, // 深红
    0xFF6A1B9A, // 深紫
    0xFF4A148C, // 罗兰紫
    0xFF37474F, // 蓝灰
    0xFF00695C, // 深青绿
)
