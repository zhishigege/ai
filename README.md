# AI效率管家 - 智能任务管理应用

一款创新的Android应用，利用AI技术帮助用户智能分配任务、合理安排时间、提高工作效率。

## ✨ 核心功能

### 🤖 AI智能任务拆解
- 用户输入大任务和截止日期
- AI自动拆解为每日可执行的小任务
- 支持自定义OpenAI兼容API

### 📅 任务管理
- 创建、编辑、删除任务
- 任务优先级设置（低、中、高）
- 任务状态追踪（待处理、进行中、已完成）
- 进度可视化

### ⏰ 时间管理
- 任务到期提醒
- 番茄工作法计时（预留接口）
- 实际耗时记录
- 时间估计准确度分析

### 📊 效率分析
- 任务完成率统计
- 效率评分（0-100）
- 时间估计准确度分析
- AI优化建议

### 💾 本地数据存储
- 使用Room数据库
- 所有数据存储在本地设备
- 完全隐私保护

## 🛠️ 技术栈

| 组件 | 技术 | 版本 |
| :--- | :--- | :--- |
| **开发语言** | Kotlin | 1.9.20 |
| **UI框架** | Jetpack Compose | 1.6.0 |
| **数据库** | Room | 2.6.1 |
| **网络请求** | Retrofit + OkHttp | 2.9.0 / 4.11.0 |
| **异步处理** | Coroutines | 1.7.3 |
| **JSON解析** | Gson | 2.10.1 |
| **后台任务** | WorkManager | 2.8.1 |
| **最低SDK** | Android 7.0 (API 24) | - |
| **目标SDK** | Android 14 (API 34) | - |

## 📱 应用截图

（应用截图将在首次运行后展示）

## 🚀 快速开始

### 前置要求
- Android 7.0 (API 24) 或更高版本
- OpenAI API Key（用于AI功能）

### 安装步骤

1. **获取APK文件**
   - 从Release页面下载最新版本的APK

2. **安装应用**
   - 在Android设备上打开APK文件
   - 按照提示完成安装
   - 允许必要的权限（网络、通知、闹钟）

3. **配置API**
   - 打开应用
   - 进入设置页面
   - 填写OpenAI兼容API的信息：
     - API地址：例如 `https://api.openai.com/v1/`
     - API Key：您的OpenAI API密钥
     - 模型名称：例如 `gpt-3.5-turbo` 或 `gpt-4`

4. **开始使用**
   - 点击右下角的 + 按钮添加新任务
   - 输入任务信息和可用时间
   - 选择"使用AI拆解任务"让AI帮你规划
   - 在"今日任务"中查看和完成任务

## 📖 使用指南

### 添加任务
1. 点击主屏幕右下角的 **+** 按钮
2. 填写以下信息：
   - **任务名称**：清晰的任务标题
   - **任务描述**：详细的任务说明
   - **可用天数**：完成任务的天数
   - **每天可用小时数**：每天能投入的时间
   - **优先级**：任务的重要程度

3. 选择操作：
   - **使用AI拆解任务**：让AI自动规划每日任务
   - **创建任务**：手动创建任务（不使用AI）

### 管理任务
- **查看任务**：在主屏幕查看所有任务
- **更新进度**：点击任务卡片，使用进度条更新完成度
- **标记完成**：点击"标记完成"按钮
- **删除任务**：向左滑动任务卡片（如支持）

### 查看效率分析
1. 点击主屏幕右上角的 **趋势** 图标
2. 查看以下指标：
   - 任务完成率
   - 效率评分
   - 时间估计准确度
3. 点击"获取AI优化建议"获取个性化建议

### 配置API
1. 点击主屏幕右上角的 **设置** 图标
2. 在"OpenAI API 配置"部分填写：
   - **API 地址**：OpenAI兼容API的基础URL
   - **API Key**：您的API密钥
   - **模型名称**：使用的模型（如gpt-3.5-turbo）
3. 点击"保存配置"

## ⚙️ API配置详解

### 支持的API服务
- **OpenAI官方API**：https://api.openai.com/v1/
- **其他兼容服务**：任何OpenAI兼容的API服务

### 获取API Key
1. **OpenAI官方**：访问 https://platform.openai.com/api-keys
2. **其他服务**：根据服务提供商的说明获取

### 默认配置
```
API地址: https://api.openai.com/v1/
模型: gpt-3.5-turbo
```

## 🔒 隐私和安全

- **本地存储**：所有任务数据存储在设备本地，不上传到服务器
- **API密钥**：API密钥仅用于调用OpenAI API，不会被分享或存储在其他地方
- **权限最小化**：应用仅请求必要的权限（网络、通知、闹钟）

## 📁 项目结构

```
AIEfficiencyApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/aiefficiency/
│   │   │   ├── ui/              # UI层（Compose界面）
│   │   │   ├── viewmodel/       # ViewModel（状态管理）
│   │   │   ├── model/           # 数据模型
│   │   │   ├── database/        # Room数据库
│   │   │   ├── api/             # API接口定义
│   │   │   ├── service/         # 业务服务（AI、提醒）
│   │   │   ├── repository/      # 数据仓库
│   │   │   └── util/            # 工具类
│   │   ├── res/                 # 资源文件
│   │   └── AndroidManifest.xml  # 应用清单
│   ├── build.gradle.kts         # 模块构建配置
│   └── proguard-rules.pro       # 混淆规则
├── build.gradle.kts             # 项目构建配置
├── settings.gradle.kts          # 项目设置
└── README.md                    # 本文件
```

## 🤝 贡献

欢迎提交Issue和改进建议！

## 📄 开源协议

本项目采用 **MIT 协议** 开源

## 👨‍💻 开发者

由 **Manus AI** 辅助开发

## 🙏 致谢

感谢以下开源项目和技术：
- [Material Design](https://material.io/design) - UI设计规范
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代UI框架
- [Retrofit](https://square.github.io/retrofit/) - 网络请求库
- [Room](https://developer.android.com/training/data-storage/room) - 数据库框架
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - 异步编程
- [OpenAI](https://openai.com/) - AI技术支持

## 📮 反馈和支持

如有问题或建议，欢迎通过以下方式联系：
- 提交GitHub Issue
- 发送邮件反馈

---

**Made with ❤️ by AI效率管家团队**

**最后更新**: 2025年10月31日
