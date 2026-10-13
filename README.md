# c001apk-compose Reply Fix

基于 [frisk1127/c001apk-compose](https://github.com/frisk1127/c001apk-compose) 的评论修复分支，原作者为 [bggRGjQaUbCoE](https://github.com/bggRGjQaUbCoE/c001apk-compose)。本分支维护账号：tiancaiProgramer。

这是独立的非官方客户端修复版本，与酷安官方及上游作者无隶属或背书关系。包名 `com.example.c001apk.replyfix`，不会覆盖原版 `com.example.c001apk.compose`。

## 修复内容

- Token 时间规范化为 `(System.currentTimeMillis() / 1000L).toString()`，不再转换为 float。这是保留的代码改进，不是本次恢复评论的必要条件。
- 保留原 v2 Token 算法。发表评论时，Device 第 0 段读取使用者自己的“数字联盟 ID”设置；Token 输入与实际发送的 Device 一致。
- 保存 ID 时同步更新 Device 首段，保留其余字段，避免随机更换 MAC 或把旧 ID 留在 Device 中。
- 保留原 Cookie、登录流程和评论 Body，包括 `replyAndForward=0`。
- APK 和源码不内置测试者的 ID、Cookie、账号或设备参数。

## 当前状态与使用

这是**需要本人有效 ID 配置的实验版本**，尚未实现自动获取数字联盟 ID。不要把它描述为“所有设备安装即用”。

1. 安装 Release 中的 APK，使用自己的账号登录。
2. 获取本人 ShuzlmID：酷安官方 App → 设置 → 关于 → 右上角三点 → 检测日志 → 连点“复制 OAID”4 下；粘贴复制出的内容，找到其中的 `ShuzlmID`。
3. 在本客户端设置 → 数字联盟 ID 中填入上述 **ShuzlmID**，保存后重新进入动态发布评论。虽然官方入口名为“复制 OAID”，这里需要填写的是日志中的 ShuzlmID，而非 OAID。不要使用别人分享的 ID、Cookie 或 Device。

没有有效标识时，单独修复时间格式仍可能收到 `err_request_need_upgrade_new_version`。任意随机 36 位文本是否有效没有得到验证。Root / Frida 用于开发验证，运行本版本不要求 Root / Frida。

## 验证依据

2026-10-13，使用者补充手动测试：原版 925abdb 保持原时间代码，仅将“数字联盟 ID”设置改为本人从官方检测日志取得的 36 位 ShuzlmID 后，评论成功。因此，在本次设备与账号测试中，此前失败的关键原因是该设置填写了错误标识；正确配置 ShuzlmID 即可恢复回复，无需修改原版时间代码。

同一小米 15、同一账号、网络、动态下的请求对照表明：整数秒时间版仍失败；仅替换 Device 第 0 段并重新生成配套 v2 Token 后成功；仅更换 MAC 段、最后一段或删除 replyAndForward=0 均未解决问题。

详见 [测试说明](docs/VALIDATION.md)。只验证过单一设备/账号组合，未验证跨设备普遍成功，也未完整确定服务端全部判定条件。

## 作者与许可

| 角色 | 作者 / 来源 |
|---|---|
| 原项目作者 | [bggRGjQaUbCoE](https://github.com/bggRGjQaUbCoE/c001apk-compose) |
| 当前维护版来源 | [frisk1127](https://github.com/frisk1127/c001apk-compose) |
| 此次修复与整理 | tiancaiProgramer，使用 AI 辅助修改与验证 |
| 其他贡献者与第三方代码 | Git 提交历史、[AUTHORS.md](AUTHORS.md)、[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) |

本衍生版本保留上游 **GNU AGPL version 3** 许可证，详见 [LICENSE](LICENSE)。第三方组件遵循各自许可证；原版权声明保留。本项目按现状提供，不提供担保。

## 构建与发布

见 [构建说明](docs/BUILDING.md)、[发布说明](docs/PUBLISHING.md) 和 [改动记录](CHANGELOG.md)。每份 Release APK 必须同时提供对应 commit 的完整源码和构建说明。
