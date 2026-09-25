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
// Xinsu 基于 KernelSU v3.3.0：内核版本对齐 v3.3.0，管理器自身版本独立递增以便自更新
// 内核/管理器兼容性由 Natives.requireNewKernel()（内核最低版本 + UAPI）判断，与 managerVersionCode 无关
extra["managerVersionCode"] = 32602
extra["managerVersionName"] = "3.3.1"
