"""
Per-test coverage report generator for new instrumented tests.
Runs each test class individually, collects .ec files, generates per-test and merged reports.
"""
import os
import shutil
import subprocess
import sys
import glob
import time

BASE_DIR = r"e:\apps-android-wikipedia-main\apps-android-wikipedia-main"
GRADLEW = os.path.join(BASE_DIR, "gradlew.bat")
CONNECTED_DIR = os.path.join(BASE_DIR, "app", "build", "outputs", "code_coverage", "devDebugAndroidTest", "connected")
STAGING_DIR = os.path.join(BASE_DIR, "coverage_staging")
REPORT_TASK = ":app:jacocoAndroidTestReport"
TEST_TASK = ":app:connectedDevDebugAndroidTest"

# Test classes for per-test coverage
TEST_CLASSES = [
    "org.wikipedia.database.AppDatabaseTests",
    "org.wikipedia.tests.categories.CategoryActivityTest",
    "org.wikipedia.tests.wiktionary.WiktionaryDialogComposeTest",
    "org.wikipedia.tests.talk.TalkTopicsActivityTest",
    "org.wikipedia.tests.watchlist.WatchlistActivityTest",
]

def run_cmd(cmd, timeout_sec=300):
    """Run a command and return success/failure with output."""
    print(f"\n{'='*70}")
    print(f"RUN: {cmd}")
    print(f"{'='*70}")
    result = subprocess.run(
        cmd, shell=True, cwd=BASE_DIR,
        capture_output=False,  # show live output
        timeout=timeout_sec
    )
    return result.returncode == 0

def clean_connected_dir():
    """Remove all .ec files from the connected directory."""
    if os.path.exists(CONNECTED_DIR):
        shutil.rmtree(CONNECTED_DIR)
        print(f"Cleaned: {CONNECTED_DIR}")
    os.makedirs(CONNECTED_DIR, exist_ok=True)

def copy_ec_files(src_dir, dest_dir):
    """Copy all .ec files from src to dest."""
    os.makedirs(dest_dir, exist_ok=True)
    ec_files = glob.glob(os.path.join(src_dir, "**", "*.ec"), recursive=True)
    if not ec_files:
        print(f"WARNING: No .ec files found in {src_dir}")
        return False
    for f in ec_files:
        shutil.copy2(f, dest_dir)
        print(f"  Copied: {os.path.basename(f)}")
    return True

def copy_report_to_staging(report_name, class_dir):
    """Copy generated HTML report to staging directory."""
    report_src = os.path.join(BASE_DIR, "app", "build", "reports", "jacoco", "androidTestCoverage", "html")
    report_dest = os.path.join(class_dir, "report")
    if os.path.exists(report_dest):
        shutil.rmtree(report_dest)
    if os.path.exists(report_src):
        shutil.copytree(report_src, report_dest)
        print(f"  Report saved to: {report_dest}")
    else:
        print(f"  WARNING: Report not found at {report_src}")

def parse_coverage_from_html(report_dir):
    """Extract coverage numbers from JaCoCo HTML report."""
    index_file = os.path.join(report_dir, "index.html")
    if not os.path.exists(index_file):
        return None
    # Read the index.html and look for the total row in the footer
    with open(index_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # JaCoCo puts totals in a tfoot with ct1/ct2 classes
    import re
    # Pattern: look for tfoot rows, they contain the total
    tfoot_match = re.search(r'<tfoot>(.*?)</tfoot>', content, re.DOTALL)
    if not tfoot_match:
        return None
    
    tfoot = tfoot_match.group(1)
    cells = re.findall(r'<td[^>]*>(.*?)</td>', tfoot, re.DOTALL)
    
    # JaCoCo footer: Element, Missed Instructions, Cov., Missed Branches, Cov., Missed, Cxty, Missed, Lines, Missed, Methods
    # We care about instruction coverage (index ~1-2) and line coverage (index ~6-8 where we check the bar)
    result = {}
    # Better: get the footer row with class="bar"
    bar_match = re.search(r'<td class="bar">(\d+) of (\d+)</td>', tfoot)
    if bar_match:
        result['lines_missed'] = int(bar_match.group(1))
        result['lines_total'] = int(bar_match.group(2))
    
    # Get percentage from the bar fill
    pct_match = re.search(r'ctr2.*?>(\d+)%<', tfoot)
    if pct_match:
        result['instruction_pct'] = int(pct_match.group(1))
    
    return result

def main():
    print("=" * 70)
    print("Per-Test Coverage Report Generator")
    print("=" * 70)
    print(f"Test classes ({len(TEST_CLASSES)}):")
    for tc in TEST_CLASSES:
        print(f"  - {tc}")
    
    # Clean staging directory
    if os.path.exists(STAGING_DIR):
        shutil.rmtree(STAGING_DIR)
    os.makedirs(STAGING_DIR, exist_ok=True)
    
    # Phase 1: Run each test class and collect .ec files
    collected_ec_dirs = []
    for i, test_class in enumerate(TEST_CLASSES):
        print(f"\n{'#'*70}")
        print(f"# [{i+1}/{len(TEST_CLASSES)}] Testing: {test_class}")
        print(f"{'#'*70}")
        
        short_name = test_class.split(".")[-1]
        class_dir = os.path.join(STAGING_DIR, short_name)
        ec_dir = os.path.join(class_dir, "ec")
        
        # Step 1: Clean old .ec files
        clean_connected_dir()
        
        # Step 2: Run the test class
        cmd = f'{GRADLEW} {TEST_TASK} -Pandroid.testInstrumentationRunnerArguments.class={test_class} --no-daemon'
        success = run_cmd(cmd, timeout_sec=300)
        if not success:
            print(f"ERROR: Test {test_class} failed!")
            # Continue anyway to collect whatever .ec data exists
        
        # Step 3: Copy .ec files to staging
        print(f"\nCollecting .ec files...")
        has_ec = copy_ec_files(CONNECTED_DIR, ec_dir)
        if has_ec:
            collected_ec_dirs.append(ec_dir)
        
        # Step 4: Generate per-test coverage report
        print(f"\nGenerating per-test coverage report...")
        # Copy .ec files back to connected/ for report generation
        clean_connected_dir()
        copy_ec_files(ec_dir, CONNECTED_DIR)
        
        report_cmd = f'{GRADLEW} {REPORT_TASK} -x {TEST_TASK} --no-daemon --no-configuration-cache'
        success = run_cmd(report_cmd, timeout_sec=120)
        
        # Step 5: Copy report to staging
        copy_report_to_staging(short_name, class_dir)
        
        # Parse and print summary
        report_dir = os.path.join(class_dir, "report")
        result = parse_coverage_from_html(report_dir)
        if result:
            print(f"\n  Coverage Summary for {short_name}:")
            if 'instruction_pct' in result:
                print(f"    Instruction Coverage: {result['instruction_pct']}%")
            if 'lines_missed' in result:
                print(f"    Lines: {result['lines_total'] - result['lines_missed']} / {result['lines_total']}")
    
    # Phase 2: Generate merged report
    print(f"\n{'#'*70}")
    print(f"# Generating MERGED coverage report...")
    print(f"{'#'*70}")
    
    merged_dir = os.path.join(STAGING_DIR, "merged")
    merged_ec_dir = os.path.join(merged_dir, "ec")
    
    # Copy all .ec files to both merged_ec_dir and connected/
    clean_connected_dir()
    os.makedirs(merged_ec_dir, exist_ok=True)
    
    for ec_src in collected_ec_dirs:
        for f in glob.glob(os.path.join(ec_src, "*.ec")):
            shutil.copy2(f, merged_ec_dir)
            shutil.copy2(f, CONNECTED_DIR)
    
    # Generate merged report
    report_cmd = f'{GRADLEW} {REPORT_TASK} -x {TEST_TASK} --no-daemon --no-configuration-cache'
    success = run_cmd(report_cmd, timeout_sec=120)
    copy_report_to_staging("merged", merged_dir)
    
    # Parse merged results
    report_dir = os.path.join(merged_dir, "report")
    result = parse_coverage_from_html(report_dir)
    if result:
        print(f"\n  MERGED Coverage Summary:")
        if 'instruction_pct' in result:
            print(f"    Instruction Coverage: {result['instruction_pct']}%")
        if 'lines_missed' in result:
            print(f"    Lines: {result['lines_total'] - result['lines_missed']} / {result['lines_total']}")
    
    # Phase 3: Print summary
    print(f"\n{'#'*70}")
    print(f"# ALL DONE!")
    print(f"# Reports saved to: {STAGING_DIR}")
    print(f"{'#'*70}")
    print(f"\nIndividual reports:")
    for tc in TEST_CLASSES:
        short_name = tc.split(".")[-1]
        path = os.path.join(STAGING_DIR, short_name, "report", "index.html")
        print(f"  {short_name}: file:///{path.replace(os.sep, '/')}")
    merged_path = os.path.join(STAGING_DIR, "merged", "report", "index.html")
    print(f"  MERGED:  file:///{merged_path.replace(os.sep, '/')}")

if __name__ == "__main__":
    main()
