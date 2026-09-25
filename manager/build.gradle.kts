plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}

extra["androidMinSdkVersion"] = 31
extra["androidTargetSdkVersion"] = 37
extra["androidCompileSdkVersion"] = 37
extra["androidCompileSdkVersionMinor"] = 0
extra["androidBuildToolsVersion"] = "37.0.0"
extra["androidCompileNdkVersion"] = libs.versions.ndk.get()
extra["androidSourceCompatibility"] = JavaVersion.VERSION_21
extra["androidTargetCompatibility"] = JavaVersion.VERSION_21
// Xinsu 基于 KernelSU v3.3.0：版本号与最新 ksu 对齐
// versionCode 32601 = 30000 + 2601（v3.3.0 的提交数），与内核版本匹配避免误报版本不匹配
extra["managerVersionCode"] = 32601
extra["managerVersionName"] = "3.3.0"
