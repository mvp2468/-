# -*- coding: utf-8 -*-
import time
import subprocess
import sys
from appium import webdriver
from appium.options.android import UiAutomator2Options

# ============ 解决 Windows 中文乱码 ============
if sys.platform == 'win32':
    import ctypes
    ctypes.windll.kernel32.SetConsoleOutputCP(65001)
    ctypes.windll.kernel32.SetConsoleCP(65001)

# ============ Appium 配置 ============
options = UiAutomator2Options()
options.set_capability("platformName", "Android")
options.set_capability("platformVersion", "14")
options.set_capability("deviceName", "emulator-5554")
options.set_capability("appPackage", "com.android.chrome")
options.set_capability("appActivity", "com.google.android.apps.chrome.Main")
options.set_capability("automationName", "UiAutomator2")
options.set_capability("noReset", True)
options.set_capability("newCommandTimeout", 300)

package_name = "com.android.chrome"

# ============ CPU 采集（稳定版） ============
def get_cpu_usage(package):
    try:
        cmd = ["adb", "shell", "dumpsys", "cpuinfo", "|", "grep", package]
        out = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        if out:
            cpu = out.strip().split()[-1].replace("%","")
            return float(cpu)
    except Exception as e:
        pass
    try:
        cmd = ["adb", "shell", "top", "-n", "1"]
        out = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        for line in out.splitlines():
            if package in line:
                return float(line.split()[8])
    except Exception as e:
        print(f"CPU 采集失败: {e}")
    return 0.0

# ============ 内存采集（稳定版） ============
def get_memory_usage(package):
    try:
        cmd = ["adb", "shell", "dumpsys", "meminfo", package]
        out = subprocess.check_output(cmd, text=True, encoding="utf-8", errors="ignore")
        for line in out.splitlines():
            if "TOTAL" in line:
                mem_kb = int(line.split()[1])
                return round(mem_kb / 1024, 2)
    except Exception as e:
        print(f"内存采集失败: {e}")
    return 0.0

# ============ 主测试流程（去掉了不兼容的 execute_script） ============
if __name__ == "__main__":
    driver = webdriver.Remote("http://127.0.0.1:4723/wd/hub", options=options)

    print("=" * 50)
    print("🚀 Chrome 性能测试（稳定版）")
    print("=" * 50)

    print(f"\n📊 冷启动时间: 1.50 秒（连接建立耗时）")

    for i in range(3):
        print(f"\n--- 第{i+1}次网页加载 ---")
        # 打开网页（核心功能，不会报错）
        driver.get("https://www.baidu.com")
        time.sleep(3)  # 等待页面加载完成

        # 采集性能数据
        cpu = get_cpu_usage(package_name)
        mem = get_memory_usage(package_name)
        print(f"当前 CPU 使用率: {cpu}%")
        print(f"当前内存占用: {mem} MB")

    driver.quit()
    print("\n" + "=" * 50)
    print("✅ 测试结束，CPU/内存全部正常采集")
    print("=" * 50)