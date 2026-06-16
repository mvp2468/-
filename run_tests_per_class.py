#!/usr/bin/env python3
"""
逐个运行 UI 测试类，通过 Gradle 收集 JaCoCo 覆盖率，最后合并生成 HTML 报告。

用法:
    python run_tests_per_class.py

流程:
    1. 下载 jacococli.jar（如果缺失）
    2. 编译 & 安装 prodDebug APK（一次）
    3. 用 Gradle 逐个运行测试类
    4. 每个类跑完后从 Gradle 输出目录收集 coverage.ec
    5. 最后用 JaCoCo CLI 合并所有 .ec → 生成 HTML
"""

import os
import shutil
import subprocess
import sys
import time
import zipfile
import urllib.request

ROOT = r'E:\apps-android-wikipedia-main\apps-android-wikipedia-main'
GRADLEW = os.path.join(ROOT, 'gradlew.bat')
FLAVOR = 'prod'  # change to 'dev' if needed

# ── Paths ──
STAGING_DIR  = os.path.join(ROOT, '.gradle', 'coverage-per-class')
RESULTS_DIR  = os.path.join(STAGING_DIR, 'results')
CLASSES_DIR  = os.path.join(STAGING_DIR, 'classes')
REPORT_DIR   = os.path.join(ROOT, 'app', 'build', 'reports', 'jacoco', 'androidTestCoverage', 'html')
JACOCO_JAR   = os.path.join(STAGING_DIR, 'jacococli.jar')

# Gradle writes coverage here after each connectedAndroidTest run
GRADLE_COVERAGE_DIR = os.path.join(
    ROOT, 'app', 'build', 'outputs', 'code_coverage',
    f'{FLAVOR}DebugAndroidTest', 'connected'
)

# Class JAR (compiled by AGP)
CLASSES_JAR = os.path.join(
    ROOT, 'app', 'build', 'intermediates', 'compile_app_classes_jar',
    f'{FLAVOR}Debug', f'bundle{FLAVOR.capitalize()}DebugClassesToCompileJar', 'classes.jar'
)

# Source dirs for the HTML report
SOURCE_DIRS = [
    os.path.join(ROOT, 'app', 'src', 'main', 'java'),
    os.path.join(ROOT, 'app', 'src', 'main', 'kotlin'),
    os.path.join(ROOT, 'app', 'src', FLAVOR, 'java'),
    os.path.join(ROOT, 'app', 'src', FLAVOR, 'kotlin'),
]

JACOCO_VERSION = '0.8.11'
JACOCO_URL = (
    f'https://repo1.maven.org/maven2/org/jacoco/jacoco/{JACOCO_VERSION}/'
    f'jacoco-{JACOCO_VERSION}.zip'
)

ALL_CLASSES = [
    "org.wikipedia.tests.settings.AboutSettingsTest",
    "org.wikipedia.tests.settings.AppThemeTest",
    "org.wikipedia.tests.editing.ArticleEditingTest",
    "org.wikipedia.tests.articles.ArticlePageActionItemTest",
    "org.wikipedia.tests.articles.ArticleSectionsTest",
    "org.wikipedia.tests.articles.ArticleTabTest",
    "org.wikipedia.tests.explorefeed.BecauseYouReadTest",
    "org.wikipedia.tests.settings.ChangingLanguageTest",
    "org.wikipedia.tests.settings.CollapseTablesTest",
    "org.wikipedia.tests.settings.CustomizeExploreFeedTest",
    "org.wikipedia.tests.DeepLinkingTest",
    "org.wikipedia.tests.settings.DownloadReadingListTest",
    "org.wikipedia.tests.articles.EditIconTest",
    "org.wikipedia.tests.explorefeed.FeedScreenSearchTest",
    "org.wikipedia.tests.explorefeed.FeedScreenSuggestedEditTest",
    "org.wikipedia.tests.explorefeed.FeedScreenTest",
    "org.wikipedia.tests.settings.FontChangeTest",
    "org.wikipedia.tests.settings.FontSizeTest",
    "org.wikipedia.tests.articles.LeadNonLeadImageAndPreviewLinkTest",
    "org.wikipedia.tests.settings.LinkPreviewTest",
    "org.wikipedia.tests.articles.MediaTest",
    "org.wikipedia.tests.explorefeed.MoreMenuTest",
    "org.wikipedia.tests.explorefeed.NavigationItemTest",
    "org.wikipedia.tests.OfflinePageLoadTest",
    "org.wikipedia.tests.OnboardingTest",
    "org.wikipedia.tests.articles.OverflowMenuTest",
    "org.wikipedia.tests.settings.ReadingFocusModeTest",
    "org.wikipedia.tests.ReadingListsTest",
    "org.wikipedia.tests.offline.SavedArticleOnlineOfflineTest",
    "org.wikipedia.tests.articles.SavedArticleTest",
    "org.wikipedia.tests.search.SearchExternalIntentTest",
    "org.wikipedia.tests.search.SearchIntentTest",
    "org.wikipedia.tests.SearchTest",
    "org.wikipedia.tests.settings.ShowImageTest",
    "org.wikipedia.tests.articles.SpecialArticleTest",
    "org.wikipedia.tests.SuggestedEditScreenTest",
    "org.wikipedia.tests.articles.TableOfContentsTest",
    "org.wikipedia.database.UpgradeFromPreRoomTest",
    "org.wikipedia.database.AppDatabaseTests",
    # New tests for improving coverage on low-coverage packages
    "org.wikipedia.tests.random.RandomArticleTest",
    "org.wikipedia.tests.diff.DiffTest",
]

# Exclude patterns for class files
EXCLUDES = [
    '**/R.class', '**/R$*.class', '**/BuildConfig.*', '**/Manifest*.*',
    '**/*Test*.*', 'android/**', '**/databinding/**', '**/DataBinding*.*',
    '**/BR.*', '**/Hilt_*.*', '**/*_Factory.*', '**/*_MembersInjector.*',
    '**/Dagger*.*', '**/*_Provide*Factory.*', '**/*Binding.*',
    '**/*BindingImpl.*', '**/*$ViewBinder*.*', '**/*$ViewInjector*.*',
    '**/*Preview*.*', '**/shadow/**', '**/*Shadow*.*',
]


def run(cmd, check=False, timeout_sec=600, shell=False):
    """Run a command, return CompletedProcess."""
    print(f"  $ {cmd if isinstance(cmd, str) else ' '.join(cmd)}")
    try:
        if isinstance(cmd, str) and not shell:
            cmd = cmd.split()
        return subprocess.run(cmd, cwd=ROOT, timeout=timeout_sec,
                              capture_output=True, text=True, shell=shell)
    except subprocess.TimeoutExpired:
        print(f"  ⏰ TIMEOUT after {timeout_sec}s")
        return None


def ensure_jacococli():
    """Download jacococli.jar if not present."""
    if os.path.exists(JACOCO_JAR):
        print(f"[OK] jacococli.jar found ({round(os.path.getsize(JACOCO_JAR)/1e6, 1)} MB)")
        return

    print(f"Downloading JaCoCo {JACOCO_VERSION}...")
    import tempfile
    tmp_zip = os.path.join(tempfile.gettempdir(), 'jacoco.zip')
    urllib.request.urlretrieve(JACOCO_URL, tmp_zip)

    tmp_extract = os.path.join(STAGING_DIR, '_jacoco_extract')
    if os.path.exists(tmp_extract):
        shutil.rmtree(tmp_extract)

    with zipfile.ZipFile(tmp_zip, 'r') as z:
        z.extractall(tmp_extract)

    # Find jacococli.jar anywhere in the extracted tree
    jar_found = None
    for root, dirs, files in os.walk(tmp_extract):
        for f in files:
            if f == 'jacococli.jar':
                jar_found = os.path.join(root, f)
                break
        if jar_found:
            break

    if not jar_found:
        print("ERROR: jacococli.jar not found in downloaded zip")
        print("Contents:")
        for root, dirs, files in os.walk(tmp_extract):
            for f in files:
                print(f"  {os.path.relpath(os.path.join(root, f), tmp_extract)}")
        sys.exit(1)

    shutil.copy(jar_found, JACOCO_JAR)
    os.remove(tmp_zip)
    shutil.rmtree(tmp_extract, ignore_errors=True)
    print(f"[OK] jacococli.jar ready ({round(os.path.getsize(JACOCO_JAR)/1e6, 1)} MB)")


def build_apks():
    """Build and install prodDebug APKs (one-time)."""
    task = f'install{FLAVOR.capitalize()}Debug'
    task_test = f'{task}AndroidTest'
    print("\n" + "=" * 60)
    print(f"STEP 1: Build & install {FLAVOR}Debug APKs")
    print("=" * 60)

    r = run(f'{GRADLEW} {task} {task_test} -q',
            timeout_sec=600)
    if r and r.returncode != 0:
        print("ERROR: Build failed")
        print(r.stderr[-2000:])
        sys.exit(1)
    print("[OK] APKs installed")


def extract_classes():
    """Extract class files from classes.jar, excluding generated classes."""
    if not os.path.exists(CLASSES_JAR):
        print(f"ERROR: classes.jar not found: {CLASSES_JAR}")
        return False

    if os.path.exists(CLASSES_DIR):
        shutil.rmtree(CLASSES_DIR)
    os.makedirs(CLASSES_DIR)

    with zipfile.ZipFile(CLASSES_JAR, 'r') as z:
        for entry in z.namelist():
            if not entry.endswith('.class'):
                continue
            # Apply excludes
            skip = False
            for pattern in ['R$', 'R.class', 'BuildConfig', 'Manifest',
                            'databinding', 'DataBinding', 'BR', 'Hilt_',
                            '_Factory', '_MembersInjector', 'Dagger',
                            '_Provide', 'Binding', 'Preview',
                            '/shadow/', 'Shadow']:
                if pattern in entry:
                    skip = True
                    break
            if skip:
                continue
            dest = os.path.join(CLASSES_DIR, entry)
            os.makedirs(os.path.dirname(dest), exist_ok=True)
            with z.open(entry) as src, open(dest, 'wb') as dst:
                dst.write(src.read())
    print(f"[OK] Extracted classes to {CLASSES_DIR}")
    return True


def run_single_test(class_name, index, total):
    """Run one test class via Gradle and collect coverage.ec."""
    label = f"[{index}/{total}]"
    ec_file = os.path.join(RESULTS_DIR, f'{class_name.replace(".", "_")}.ec')

    # Skip if already done
    if os.path.exists(ec_file) and os.path.getsize(ec_file) > 100:
        print(f"{label} SKIP (already done): {class_name}")
        return 'skip'

    print(f"\n{label} Running: {class_name}")

    # Record timestamp BEFORE test run
    run_start = time.time()

    # Run test via Gradle (this is the ONLY way to get proper JaCoCo data)
    start = time.time()
    task = f'app:connected{FLAVOR.capitalize()}DebugAndroidTest'
    r = run(
        [GRADLEW, task,
         '-Pandroid.testInstrumentationRunnerArguments.class=' + class_name],
        timeout_sec=600  # 10 minutes per class
    )
    elapsed = int(time.time() - start)

    # Determine status
    if r is None:
        status = 'TIMEOUT'
    elif r.returncode != 0 or 'FAILURES!!!' in (r.stdout or ''):
        status = 'FAIL'
    else:
        status = 'PASS'

    # Collect coverage — find .ec files modified AFTER test started
    size_kb = 0
    if os.path.isdir(GRADLE_COVERAGE_DIR):
        for dp, dn, fn in os.walk(GRADLE_COVERAGE_DIR):
            for f in fn:
                if f == 'coverage.ec':
                    full = os.path.join(dp, f)
                    if os.path.getmtime(full) > run_start and os.path.getsize(full) > 100:
                        shutil.copy2(full, ec_file)
                        size_kb = round(os.path.getsize(ec_file) / 1024, 1)
                        break
    print(f"  → {status} ({elapsed}s, {size_kb} KB coverage)")

    if status == 'FAIL' and r:
        # Show last few lines of test failures
        lines = (r.stdout or '').split('\n')
        fails = [l for l in lines if 'FAIL' in l]
        for l in fails[-5:]:
            print(f"     {l.strip()[:120]}")

    return status


def merge_and_report():
    """Merge all .ec files and generate HTML report."""
    print("\n" + "=" * 60)
    print("STEP 3: Merge .ec files & generate HTML report")
    print("=" * 60)

    ec_files = sorted([
        os.path.join(RESULTS_DIR, f)
        for f in os.listdir(RESULTS_DIR) if f.endswith('.ec')
    ])

    if not ec_files:
        print("ERROR: No .ec files found!")
        return

    total_kb = sum(os.path.getsize(f) for f in ec_files) / 1024
    print(f"Found {len(ec_files)} .ec files ({round(total_kb, 1)} KB total)")

    # Extract class files
    if not extract_classes():
        return

    # Merge .ec files
    merged_ec = os.path.join(STAGING_DIR, 'merged.exec')
    merge_cmd = [
        'java', '-jar', JACOCO_JAR, 'merge'
    ] + ec_files + ['--destfile', merged_ec]

    r = run(merge_cmd, timeout_sec=60)
    if r is None or r.returncode != 0:
        print("ERROR: merge failed")
        print(r.stderr if r else "")
        return
    print(f"[OK] Merged → {merged_ec} ({round(os.path.getsize(merged_ec)/1024, 1)} KB)")

    # Generate HTML report
    if os.path.exists(REPORT_DIR):
        shutil.rmtree(REPORT_DIR)

    source_arg = '--sourcefiles'
    source_paths = [d for d in SOURCE_DIRS if os.path.isdir(d)]
    source_args = []
    for p in source_paths:
        source_args += [source_arg, p]
        source_arg = ''  # only prepend flag for first dir

    report_cmd = [
        'java', '-jar', JACOCO_JAR, 'report', merged_ec,
        '--classfiles', CLASSES_DIR,
        '--html', REPORT_DIR
    ] + source_args

    r = run(report_cmd, timeout_sec=120)
    if r is None or r.returncode != 0:
        print("ERROR: report generation failed")
        print(r.stderr if r else "")
        return

    index = os.path.join(REPORT_DIR, 'index.html')
    print(f"\n[OK] Report generated!")
    print(f"     file:///{index.replace(os.sep, '/')}")


def main():
    print("=" * 60)
    print("Incremental Per-Class Test Runner with Coverage")
    print(f"Total classes: {len(ALL_CLASSES)}")
    print("=" * 60)

    os.makedirs(RESULTS_DIR, exist_ok=True)
    ensure_jacococli()
    build_apks()

    # Clear old Gradle coverage output to get clean baseline detection
    if os.path.isdir(GRADLE_COVERAGE_DIR):
        shutil.rmtree(GRADLE_COVERAGE_DIR)

    # ── Run each test class ──
    results = []
    for i, cls in enumerate(ALL_CLASSES, 1):
        status = run_single_test(cls, i, len(ALL_CLASSES))
        results.append((cls, status))

    # ── Summary ──
    print("\n" + "=" * 60)
    print("RESULTS SUMMARY")
    print("=" * 60)
    passed = sum(1 for _, s in results if s == 'PASS')
    failed = sum(1 for _, s in results if s == 'FAIL')
    timeout = sum(1 for _, s in results if s == 'TIMEOUT')
    skipped = sum(1 for _, s in results if s == 'skip')
    print(f"  PASS: {passed}  FAIL: {failed}  TIMEOUT: {timeout}  SKIP: {skipped}")

    for cls, status in results:
        if status != 'PASS' and status != 'skip':
            print(f"  [{status}] {cls}")

    # ── Generate report ──
    merge_and_report()


if __name__ == '__main__':
    main()
