# Xinsu Manager

基于 [KernelSU](https://github.com/tiann/KernelSU) v3.3.0 二次开发的独立管理器（Xinsu）。

- **版本对齐**：Manager 与 KernelSU v3.3.0 完全一致（versionCode `32601` / versionName `3.3.0`），不会出现“版本不匹配”误报。
- **自更新通道**：检查更新不再依赖 KernelSU 云端，改为读取本仓库（`Xinsu-Manager`）的 GitHub Release，可完全独立升级。
- **独立包名**：`com.xinsu.manager`，可与原版 KernelSU Manager 共存。
- **ksud 内嵌**：`manager/app/src/main/jniLibs/` 内置了针对 `com.xinsu.manager` 编译的 `libksud.so`（arm64-v8a / x86_64）。

## 更新机制

`Downloader.kt` 中的 `checkNewVersion()` 会请求：

```
https://api.github.com/repos/<username>/Xinsu-Manager/releases/latest
```

Release 资产命名需符合 `v<版本名>_<versionCode>-release.apk`，例如：

```
Xinsu_v3.3.0_32601-release.apk
```

Manager 据此解析出最新 versionCode 并提示更新（APK 内自带安装器）。

## 发布新版本

1. 修改 `manager/build.gradle.kts`（`managerVersionCode` / `managerVersionName`）与 ksud 源码后重新编译 `libksud.so` 并替换 `manager/app/src/main/jniLibs/` 下的文件；
2. 打 Tag 推送：

```bash
git tag v3.3.0
git push origin v3.3.0
```

3. `.github/workflows/release.yml` 会自动构建、重命名并发布 Release。

> CI 签名需要在仓库 Secrets 中配置：`KEYSTORE`（base64 的 .jks）、`KEYSTORE_PASSWORD`、`KEY_ALIAS`、`KEY_PASSWORD`；未配置时构建产物为未签名 APK。

## 本地构建

```bash
# 1. 先编译 ksud（需要 Android NDK + Rust）
export ANDROID_NDK_HOME=<NDK路径>
export KSU_PACKAGE_NAME=com.xinsu.manager
source .github/scripts/setup-rust-build.sh aarch64-linux-android 26
cargo build --target aarch64-linux-android --release --manifest-path ./userspace/ksud/Cargo.toml
cp target/aarch64-linux-android/release/ksud manager/app/src/main/jniLibs/arm64-v8a/libksud.so

# 2. 构建 APK（JDK 21）
cd manager
./gradlew assembleRelease
```

产物位于 `manager/app/build/outputs/apk/release/`。

## 许可

本项目基于 KernelSU 二次开发，遵循 **GPL-3.0** 开源协议，详见 [LICENSE](LICENSE)。
