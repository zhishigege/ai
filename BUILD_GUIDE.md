# AI效率管家 - 构建指南

本文档说明如何从源代码构建APK文件。

## 前置要求

### 系统要求
- Windows、macOS 或 Linux
- JDK 17 或更高版本
- 至少 8GB RAM
- 10GB 可用磁盘空间

### 软件要求
- Android Studio (推荐最新版本)
- Android SDK (API 34)
- Gradle 8.5 或更高版本

## 安装步骤

### 1. 安装JDK
确保已安装JDK 17或更高版本：
```bash
java -version
```

### 2. 安装Android Studio
从 https://developer.android.com/studio 下载并安装Android Studio。

### 3. 配置Android SDK
在Android Studio中：
1. 打开 Settings → Appearance & Behavior → System Settings → Android SDK
2. 确保已安装以下SDK：
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.x.x
   - Android Emulator (可选)

### 4. 克隆或下载项目
```bash
git clone <repository-url>
cd AIEfficiencyApp
```

## 构建方法

### 方法一: 使用Android Studio (推荐)

1. **打开项目**
   - 启动Android Studio
   - 选择 "Open an existing project"
   - 选择 AIEfficiencyApp 文件夹

2. **等待Gradle同步**
   - Android Studio 会自动下载依赖
   - 等待同步完成

3. **构建APK**
   - 菜单 → Build → Build Bundle(s) / APK(s) → Build APK(s)
   - 等待构建完成

4. **找到APK**
   - APK文件位置: `app/build/outputs/apk/debug/app-debug.apk`

### 方法二: 使用命令行

#### 在Windows上:
```bash
cd AIEfficiencyApp
gradlew.bat assembleDebug
```

#### 在macOS/Linux上:
```bash
cd AIEfficiencyApp
chmod +x gradlew
./gradlew assembleDebug
```

APK文件将生成在: `app/build/outputs/apk/debug/app-debug.apk`

### 方法三: 构建Release APK

#### 使用Android Studio:
1. 菜单 → Build → Generate Signed Bundle / APK
2. 选择 APK
3. 配置签名密钥
4. 选择 Release 构建类型
5. 完成构建

#### 使用命令行:
```bash
./gradlew assembleRelease
```

## 签名配置

### 创建签名密钥 (仅需一次)

#### 使用Android Studio:
1. 菜单 → Build → Generate Signed Bundle / APK
2. 选择 APK
3. 点击 "Create new..." 创建新密钥
4. 填写以下信息:
   - Key store path: 选择保存位置
   - Password: 设置密钥库密码
   - Key alias: 输入别名
   - Key password: 设置密钥密码
   - Validity: 设置有效期 (建议25年以上)
   - Certificate: 填写证书信息

#### 使用命令行:
```bash
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias release
```

### 配置签名信息

在 `app/build.gradle.kts` 中配置:
```kotlin
signingConfigs {
    release {
        storeFile = file("path/to/release.keystore")
        storePassword = "your_store_password"
        keyAlias = "release"
        keyPassword = "your_key_password"
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.release
        isMinifyEnabled = true
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
```

## 常见问题

### Q1: Gradle同步失败
**A**: 
- 检查网络连接
- 尝试更新Gradle: `./gradlew wrapper --gradle-version 8.5`
- 清除缓存: `./gradlew clean`

### Q2: 编译错误
**A**:
- 确保JDK版本正确: `java -version`
- 清除构建缓存: `./gradlew clean`
- 重新同步Gradle: 在Android Studio中点击 "Sync Now"

### Q3: APK文件过大
**A**:
- 启用混淆和压缩
- 移除不必要的依赖
- 使用App Bundle而不是APK

### Q4: 签名错误
**A**:
- 检查签名密钥文件是否存在
- 验证密码是否正确
- 确保密钥库文件未损坏

## 优化构建

### 启用增量构建
在 `gradle.properties` 中添加:
```properties
org.gradle.parallel=true
org.gradle.caching=true
```

### 减少APK大小
1. 启用ProGuard混淆
2. 启用资源压缩
3. 移除未使用的库
4. 使用WebP格式的图片

### 加快构建速度
1. 使用本地Gradle守护进程
2. 启用并行构建
3. 增加Gradle堆大小:
```properties
org.gradle.jvmargs=-Xmx4096m
```

## 测试APK

### 在模拟器上测试
1. 打开Android Emulator
2. 菜单 → Run → Run 'app'
3. 选择模拟器设备
4. 等待应用启动

### 在真实设备上测试
1. 通过USB连接Android设备
2. 启用开发者模式和USB调试
3. 菜单 → Run → Run 'app'
4. 选择连接的设备
5. 等待应用启动

## 发布APK

### 准备发布
1. 更新版本号: `app/build.gradle.kts`
2. 更新changelog
3. 进行充分测试
4. 构建Release APK

### 分发方式
1. **GitHub Releases**: 上传APK到GitHub
2. **Google Play Store**: 通过Google Play Console发布
3. **直接分发**: 通过网站或邮件分发APK

## 持续集成

### GitHub Actions示例
```yaml
name: Build APK

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: chmod +x gradlew
      - run: ./gradlew assembleDebug
      - uses: actions/upload-artifact@v2
        with:
          name: app-debug.apk
          path: app/build/outputs/apk/debug/app-debug.apk
```

## 故障排除

### 清除所有缓存
```bash
./gradlew clean
rm -rf .gradle
rm -rf build
```

### 重新同步依赖
```bash
./gradlew --refresh-dependencies
```

### 查看详细日志
```bash
./gradlew assembleDebug --stacktrace --debug
```

## 参考资源

- [Android官方文档](https://developer.android.com/docs)
- [Gradle官方文档](https://gradle.org/docs/)
- [Kotlin官方文档](https://kotlinlang.org/docs/)
- [Jetpack Compose文档](https://developer.android.com/jetpack/compose)

---

**最后更新**: 2025年10月31日
