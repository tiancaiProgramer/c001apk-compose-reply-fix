# 发布说明

## 维护者署名

本维护分支的后续开发、新增功能、问题修复与版本发布署名 **[tiancaiProgramer](https://github.com/tiancaiProgramer)**。README 与 Release 说明优先展示当前维护者和本次更新，原项目、上游版本及第三方组件集中放在文末“来源与致谢”。具体贡献以提交记录为准，保留原有文件头、版权和许可声明。

Release 说明统一以以下内容开头：

> **维护与新增功能：[tiancaiProgramer](https://github.com/tiancaiProgramer)**

随后写本次更新、安装与验证范围，最后列来源与致谢。只调整展示说明时，不替换已发布 APK 或其对应源码、签名及校验附件；涉及应用内展示的源码改动随下次版本构建发布。

## 仓库与附件

建议仓库名：c001apk-compose-reply-fix。发布者账号须以本人 GitHub 主页确认；用户名本身不提供写入授权。

优先在 GitHub 上 Fork frisk1127/c001apk-compose，保留 fork 来源关系；也可建立有明确作者归属和许可证的新公开仓库。不要覆盖已有不同内容的仓库或强制推送。

将保留上游历史的 main 分支推到本人仓库。首次发布设为 0.1.0-rc.1 预发布版，上传公开 APK、该 tag 的完整源码 ZIP、SHA256SUMS 和 VALIDATION.json。不要上传旧的私有实验 APK 或未脱敏证据。

发布前确认：

- LICENSE 与上游一致；README / AUTHORS / NOTICE 说明原作者、维护版来源、修改日期和修改者。
- APK 所对应源码可获得，并包含构建脚本和第三方许可信息。
- 关于页显示上游与本分支来源、AGPL 许可与无担保提示。
- 没有个人 ID、Cookie、登录 Token、私钥、签名密码、local.properties 或原始日志。
- 如实标明需要本人有效 ID 配置，尚非自动获取的通用安装即用版本。

规则参考：

- https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/licensing-a-repository
- https://docs.github.com/en/site-policy/acceptable-use-policies/github-acceptable-use-policies
- https://www.gnu.org/licenses/agpl-3.0-body.html

本仓库保留许可和归属；GitHub 不为项目背书，也不预先保证每个仓库符合全部第三方服务政策。发现具体权利或许可问题时应修正，不能靠免责声明取消原许可证义务。
