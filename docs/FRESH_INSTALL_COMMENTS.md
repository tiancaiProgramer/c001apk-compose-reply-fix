# 新装设备评论区验证码错误

维护与修复：**tiancaiProgramer**。

## 已确认的原因

2026-10-17，在新设备小米 MIX Fold 3（2308CPXD0C，Android 15）的 0.1.3-rc.1 上，读取动态 `74406267` 的评论列表显示：

```text
java.lang.NumberFormatException: For input string: "err_request_captcha_v2"
```

服务端要求此次访问完成新版人机验证，返回 `messageStatus` 字符串以及 NEC 类型的验证码参数。客户端却将该字段声明为整数，因此在显示服务端解释前就解析失败。此前覆盖安装的设备没有触发该挑战，不足以验证新装设备的访问验证流程。

## 修复范围

- 评论响应允许字符串或数字形式的状态，支持 `code` 作为状态码字段，保留服务端说明。
- 按响应中的实际验证码配置显示易盾官方网页组件，由使用者手动完成验证。
- 验证成功后仅自动重试原评论 GET 一次，保留 URL、排序与分页；取消或验证未通过时显示可读提示。
- 验证凭据只在内存中短暂保留，绑定当前账号及设备，最多五分钟；账号或设备改变即失效。只提交到两个官方 API 域名的 HTTPS 评论列表 GET，不发送给 GitHub、其他域名、其他接口或评论发布请求。
- 不自动解验证码，不内置其他账号的设备标识，不将验证凭据写入日志、存储、源码或发布附件。

## 验证记录

原模型下新增的异常响应测试先复现 `JsonSyntaxException / NumberFormatException`；修复后，应用与媒体模块共 32 项单元测试通过，包括状态类型、响应附加参数、取消、会话变更及请求范围隔离。

原签名 `0.1.3-rc.2` 候选包（versionCode 256）已在上述新设备覆盖安装，保留现有数据。真实易盾组件正常加载，由使用者手动完成验证后，原评论 GET 自动重试，默认评论、评论图片和内嵌回复恢复显示；截图核对无解析异常及验证弹窗。该次验证没有发布公开评论。

本次未对所有验证码类型、全新账号、跨页加载或所有排序进行真机专项；仅接入实际返回的 NEC/空 captchaField 协议。服务端验证未通过时仍会显示错误并允许重试，不承诺所有账号或网络均自动放行。

## 协议与组件参考

- [易盾官方验证码组件](https://github.com/yidun/captcha-android-demo)；运行时加载官方 `cstaticdun.126.net` 网页组件。
- [TG-Twilight/c001apk 的验证码响应协议](https://github.com/TG-Twilight/c001apk/blob/1f2326567ff6314573b5a5a43fc2f865c173bde0/apps/flutter/lib/logic/network/captcha_challenge.dart)及[请求头提交约定](https://github.com/TG-Twilight/c001apk/blob/1f2326567ff6314573b5a5a43fc2f865c173bde0/apps/flutter/lib/logic/network/interceptor.dart)，用于核对 NEC 成功回调的格式与 `validate` Cookie。本维护分支按现有 Kotlin/Compose 网络结构独立实现人工验证及重试。
- 原项目作者、上游及许可继续见 [AUTHORS.md](../AUTHORS.md)、[NOTICE](../NOTICE) 和 [LICENSE](../LICENSE)。
