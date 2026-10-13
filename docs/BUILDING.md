# 构建说明

## 环境

- JDK 17。
- Android SDK Platform 35、Build Tools 36.0.0。
- Gradle Wrapper 9.1.0，配置见 gradle/wrapper/gradle-wrapper.properties。
- 首次构建需要访问 Google Maven、Maven Central、Gradle Plugin Portal 和 JitPack。已有完整依赖缓存时可追加 --offline。

克隆仓库并进入根目录，配置 Android SDK 路径（local.properties 或 ANDROID_HOME）。

```sh
bash scripts/build-release.sh
```

脚本构建签名 release，关闭 R8，使用上游 AGP 兼容参数。手动构建等价命令：

```sh
bash gradlew :app:assembleRelease -PdisableR8=true -Pandroid.newDsl=false -Pandroid.builtInKotlin=false -PreleaseVersionName=0.1.0-rc.1 -PreleaseVersionCode=248
```

源码 ZIP 不需要 .git 也可构建；显式版本参数避免依赖本地提交数。

## 签名

本地未配置 release 密钥时沿用 Android 调试签名，适合自己的构建，不能用于覆盖采用不同证书签名的已安装 APK。

正式维护者应创建自己的密钥并长期保存。可在不提交的 local.properties 中填写 KEYSTORE_PATH、KEYSTORE_PASSWORD、KEY_ALIAS、KEY_PASSWORD。私钥、密码、local.properties 均不得上传 GitHub。

发布 APK 使用本次维护者构建的证书；源码不包含该私钥。自行构建可用自己的签名和独立包安装，无需原签名密钥。

## 产物

脚本把 APK 和 SHA256SUMS 放入 dist/。完整代码、Gradle 文件、Wrapper、内含模块与许可文本属于对应源码；发布时请上传同一 commit 导出的完整源码 ZIP。APK 不包含任何个人首段标识；用户自己的运行数据不属于发布源码的一部分。

## GitHub Actions

保留上游现有工作流。另附 `docs/build-workflow.yml`，它是使用固定提交版本的候选构建模板；可由仓库维护者在 GitHub 网页将其保存为 `.github/workflows/build.yml`。模板本身不自动运行，也不需要为了本次源码发布扩大 CLI 权限。
