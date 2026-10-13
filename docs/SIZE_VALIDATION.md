# APK 体积修复

日期：2026-10-13。单位 MB 为十进制；MiB 为 1024 × 1024 字节。

## 实测结果

| APK | 字节 | MB | DEX 文件 | 类数量 |
|---|---:|---:|---:|---:|
| 手机原版 Compose：925abdb，versionCode 69 | 5,768,871 | 5.77 | 1 | 6,897 |
| 用户提供的 reply-fix 0.1.0-rc.1 | 22,047,976 | 22.05 | 4 | 41,483 |
| rc.1 图片/交易 Debug 测试包 | 30,536,490 | 30.54 | 36 | 41,539 |
| rc.2 图片/交易优化 Release 包 | 6,055,236 | 6.06 | 1 | 7,234 |

最终包比用户提供的包减少约 72.5%，比手机原版增加约 0.29 MB。

## 原因

修复版原发布脚本 `scripts/build-release.sh` 传入了 `-PdisableR8=true`，而 `app/build.gradle.kts` 使用这个参数同时关闭代码精简与资源精简。提供的 22.05 MB APK 中，DEX 压缩后就有 18.55 MB，主要增量来自未被删除的依赖代码，而不是这次增加的少量功能代码。

此前交付的图片/交易包为 Debug 包。除了没有 R8 精简，还包含 Compose 调试工具等开发依赖，不适合作为正常发布产物。

原版构建明确开启 `isMinifyEnabled` / `isShrinkResources`，迭代版在默认情况下也开启，只有显式 `disableR8=true` 才关闭。对照来源：

- [原版 app/build.gradle.kts](https://github.com/bggRGjQaUbCoE/c001apk-compose/blob/main/app/build.gradle.kts)
- [迭代版 app/build.gradle.kts](https://github.com/frisk1127/c001apk-compose/blob/main/app/build.gradle.kts)
- [Android 官方 R8 开启说明](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization)

## 修复

1. 发布脚本默认使用开启 R8 / 资源精简的 Release，移除强制关闭 R8 的参数。
2. 增加 Windows PowerShell 发布脚本，并按真实 APK 版本号导出产物及 SHA256SUMS。
3. 应用当前只提供默认中文字符串。保留默认资源、中文变体和依赖库英文回退，排除其它依赖翻译，将 `resources.arsc` 从 1,383,324 字节降到 589,512 字节。
4. 默认发布脚本在 APK 超过 10 MiB 时停止导出，避免意外发布未优化大包。
5. 标明 Debug / 无 R8 工作流属于诊断构建；正常 Release 工作流改用修正后的发布脚本。

## 验证

- `:app:assembleRelease` 完成 `minifyReleaseWithR8`、资源精简、Release lint 和 APK 打包。
- PowerShell 发布脚本实跑成功，输出 6,055,236 字节 APK。
- Bash 发布脚本及其内嵌 Python 通过语法检查。
- app / media-support 共 17 项单元测试通过，0 失败。
- APK 签名校验通过，versionCode 250；独立包 `com.example.c001apk.replyfix.preview`，应用名“c001apk 图片交易测试”。
- APK 未设置 debuggable，仍包含 arm64-v8a、armeabi-v7a、x86、x86_64 四种原有原生库。
- R8 保留配置包含 Gson 数据模型字段、Parcelable CREATOR、DataStore Protobuf 字段、Retrofit 接口和 JNI 方法；检查输出保留列表包含新交易模型与媒体播放会话。

Release 运行与具体问题图片的真机复测仍待手机安装许可，不能用单元测试或成功打包代替真机功能验证。对应源码包含之前的图片与二手交易改动，详见 IMAGE_TRADE_VALIDATION.md。

上述为 rc.2 当时状态。后续 rc.3 独立版已安装并完成专项验证，仍约 6.06 MB，详见 DEVICE_VALIDATION.md。
