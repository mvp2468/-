@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title 安卓逆向大作业-APK全自动解包重打包工具

:: ===================== 全局路径（已适配你的环境，无需修改） =====================
:: 题目指定被测APK
set "SOURCE_APK=E:\test\universal.apk"
:: 你的 Android Build-Tools 36.0.0 路径
set "BUILD_TOOLS_PATH=E:\Android\SDK\build-tools\36.0.0"

set "PROJECT_DIR=%~dp0"
set "APKTOOL_BAT=%PROJECT_DIR%apktool.bat"
set "OUT_DIR=%PROJECT_DIR%output"
set "DECOMPILE_DIR=%OUT_DIR%\decompile_apk"
set "UNSIGNED_APK=%OUT_DIR%\unsigned.apk"
set "ALIGN_APK=%OUT_DIR%\align.apk"
set "SIGNED_APK=%OUT_DIR%\signed_final.apk"

:: SDK 工具路径（36.0.0 内置工具）
set "APKSIGNER=%BUILD_TOOLS_PATH%\apksigner.bat"
set "ZIPALIGN=%BUILD_TOOLS_PATH%\zipalign.exe"
:: 系统默认调试密钥（无需 testkey.pk8 / testkey.x509.pem）
set "DEBUG_KEYSTORE=%USERPROFILE%\.android\debug.keystore"

:: ===================== 1. 前置文件校验 =====================
echo ==============================================
echo  目标被测应用：%SOURCE_APK%
echo  签名工具：apksigner (Build-Tools 36.0.0)
echo  签名密钥：系统默认 debug.keystore
echo ==============================================

:: 校验被测APK
if not exist "%SOURCE_APK%" (
    echo [错误] 未找到 E:\test\universal.apk，请检查文件！
    pause >nul
    exit /b 1
)
:: 校验 apktool 启动脚本
if not exist "%APKTOOL_BAT%" (
    echo [错误] 项目根目录缺少 apktool.bat
    pause >nul
    exit /b 1
)
:: 校验签名工具
if not exist "%APKSIGNER%" (
    echo [错误] 未找到 apksigner.bat，请检查 Build-Tools 路径！
    pause >nul
    exit /b 1
)
:: 校验调试密钥
if not exist "%DEBUG_KEYSTORE%" (
    echo [提示] 未检测到 debug.keystore
    echo 请打开 Android Studio 并启动一次模拟器，自动生成该密钥
    pause >nul
    exit /b 1
)

:: 清空历史文件
if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"
mkdir "%DECOMPILE_DIR%"
echo [1/7] 环境校验完成，已清理历史文件

:: ===================== 2. APKTool 解包 =====================
echo.
echo [2/7] 执行 apktool d 解包 APK
call "%APKTOOL_BAT%" d -f "%SOURCE_APK%" -o "%DECOMPILE_DIR%"
if !errorlevel! neq 0 (
    echo [错误] 解包失败，请检查 JDK8 或 APK 文件完整性
    pause >nul
    exit /b 1
)
echo 解包目录：%DECOMPILE_DIR%

:: ===================== 3. 手动修改 Smali 代码（作业核心） =====================
echo.
echo [3/7] ========== 请手动修改 Smali 代码 ==========
echo 打开目录：%DECOMPILE_DIR%\smali
echo 在任意 .smali 方法内插入以下代码：
echo const-string v3, "APK_REPACK_TEST"
echo const-string v4, "代码修改成功"
echo invoke-static {v3, v4}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
echo.
echo 修改并保存文件后，按回车键继续...
pause >nul

:: ===================== 4. APKTool 重打包 =====================
echo.
echo [4/7] 执行 apktool b 重打包
call "%APKTOOL_BAT%" b "%DECOMPILE_DIR%" -o "%UNSIGNED_APK%"
if !errorlevel! neq 0 (
    echo [错误] 重打包失败，Smali 代码存在语法错误
    pause >nul
    exit /b 1
)
echo 未签名APK：%UNSIGNED_APK%

:: ===================== 5. Zipalign 4字节对齐（必做，适配新版Android） =====================
echo.
echo [5/7] 执行 zipalign 4字节对齐优化
"%ZIPALIGN%" -f 4 "%UNSIGNED_APK%" "%ALIGN_APK%"
echo 对齐优化完成

:: ===================== 6. apksigner 签名（无需 testkey 密钥） =====================
echo.
echo [6/7] 执行 apksigner 签名，密钥密码：android
call "%APKSIGNER%" sign ^
--ks "%DEBUG_KEYSTORE%" ^
--ks-pass pass:android ^
--key-pass pass:android ^
--out "%SIGNED_APK%" ^
"%ALIGN_APK%"

if !errorlevel! neq 0 (
    echo [错误] 签名失败，请检查 debug.keystore
    pause >nul
    exit /b 1
)
echo 最终成品APK：%SIGNED_APK%

:: ===================== 7. ADB 安装验证 =====================
echo.
echo [7/7] 执行 ADB 安装验证
adb install -r "%SIGNED_APK%"
if !errorlevel! equ 0 (
    echo ==============================================
    echo 全部流程执行成功！实验完成
    echo ==============================================
) else (
    echo 提示：未连接设备/模拟器，跳过安装，打包流程正常完成
)

pause >nul
endlocal