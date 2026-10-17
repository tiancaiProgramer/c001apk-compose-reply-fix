# c002apk 的 GitHub 更新

维护与开发：**tiancaiProgramer**。

## 用户使用

更新来源固定为 `tiancaiProgramer/c001apk-compose-reply-fix` 的公开 Releases，无需登录 GitHub。包含已公开的预览版，不检测草稿或只有 tag 的提交。

启动时自动检查（成功后间隔至少 6 小时），后台通过 Android JobScheduler 联网时约每 24 小时检查。后台通知同版本只发一次；打开通知可查看发布详情。系统省电、网络和强行停止会影响后台执行时间，不保证发布后即时通知。

在“设置 → 关于”可手动检查，查看更新说明并打开 APK 下载或发布页。“后台更新通知”入口可允许 Android 13 及以上的通知权限，或进入系统通知频道设置。拒绝通知不影响手动检查和前台弹窗。

## 后续发布约定

1. 按语义版本递增版本名，如 `0.1.3-rc.2`、`0.1.3`、`0.1.4`；同时递增 Android versionCode，不能仅重新上传同名版本。
2. 在本仓库创建并公开对应 Release，tag 为 `v` 加版本名；填写更新说明，正式版及预览版均支持。
3. 上传正式附件 `c002apk-<版本名>.apk`，沿用现有包名和原发布证书；独立测试、Debug、源码包不能作为更新 APK。兼容历史 `c001apk-reply-fix-<版本名>.apk` 命名。
4. 同时提供对应源码、许可和校验文件。建议先以草稿完成全部附件上传与校验，再公开发布，避免用户检测到未上传完的包。

更新器从最近 30 个公开 Release 中，比较版本而非发布日期，选择最高可用版本。过滤草稿、无 APK、未完成上传、空文件和其他仓库的 URL；不会将旧版本误认为升级。下载由浏览器和 Android 正常安装流程处理。

## 请求与验证

GitHub 专用 OkHttp 客户端与酷安拦截器分离，不发送酷安账号、Cookie、Token 或设备标识；20 秒超时，限制元数据体积。后台失败留待下次检查，手动失败显示网络提示。

单元测试覆盖正式/预览版本比较、数字段排序、草稿/缺失 APK/上传状态筛选、URL 与 tag 匹配、旧命名兼容。应用及媒体模块 26 项单元测试通过。

`GithubUpdateDeviceTest` 仅允许在 `.updatetest` 独立包运行，以低于已发布版本的版本名读取真实公开发布，验证通知、同版本去重和持久后台任务。它不会操作正式安装版账号配置。小米 15 上两项专项通过，真实检测到 `v0.1.2-rc.2`，并验证前台“发现新版本”弹窗。

```powershell
.\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest '-PtestApplicationId=com.example.c001apk.replyfix.updatetest' '-PreleaseVersionName=0.1.1-rc.1' '-PreleaseVersionCode=252'
```

## 参考

- [GitHub Releases API](https://docs.github.com/en/rest/releases/releases)。
- [Android JobScheduler](https://developer.android.com/reference/android/app/job/JobScheduler)。
- [Android 通知权限](https://developer.android.com/develop/ui/views/notifications/notification-permission)。
- 上游作者、来源与许可见 [AUTHORS.md](../AUTHORS.md)、[NOTICE](../NOTICE) 和 [LICENSE](../LICENSE)。
