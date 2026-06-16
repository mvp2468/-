import os, hashlib

ROOT = r'E:\apps-android-wikipedia-main\apps-android-wikipedia-main'

# ALL 39 test classes (FQCN)
ALL_FQCN = [
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
]

# These are the 11 classes that passed in the previous prod run
# (confirmed by parsing test-results.log with STATUS_CODE: 0)
PASSED_FQCN = set([
    "org.wikipedia.tests.SearchTest",
    "org.wikipedia.tests.articles.ArticlePageActionItemTest",
    "org.wikipedia.tests.articles.ArticleSectionsTest",
    "org.wikipedia.tests.articles.ArticleTabTest",
    "org.wikipedia.tests.articles.OverflowMenuTest",
    "org.wikipedia.tests.DeepLinkingTest",
    "org.wikipedia.tests.settings.DownloadReadingListTest",
    "org.wikipedia.tests.OfflinePageLoadTest",
    "org.wikipedia.tests.OnboardingTest",
    "org.wikipedia.database.UpgradeFromPreRoomTest",
    "org.wikipedia.database.AppDatabaseTests",
])

remaining = sorted([c for c in ALL_FQCN if c not in PASSED_FQCN])
passed = sorted(PASSED_FQCN)

print(f"Passed (skip):     {len(passed)}")
print(f"To re-run:         {len(remaining)}")
print(f"Total:             {len(ALL_FQCN)}")

# ── Source hash (Gradle-compatible: int(milliseconds)) ──
src_dirs = [
    os.path.join(ROOT, 'app', 'src', 'main', 'java'),
    os.path.join(ROOT, 'app', 'src', 'main', 'kotlin'),
    os.path.join(ROOT, 'app', 'src', 'androidTest', 'java'),
    os.path.join(ROOT, 'app', 'src', 'androidTest', 'kotlin'),
    os.path.join(ROOT, 'app', 'src', 'dev', 'java'),
    os.path.join(ROOT, 'app', 'src', 'dev', 'kotlin'),
]

tokens = []
for d in src_dirs:
    if os.path.isdir(d):
        for root_d, _, files in os.walk(d):
            for fn in files:
                if fn.endswith(('.kt', '.java')):
                    fp = os.path.join(root_d, fn)
                    # MUST match Gradle: f.absolutePath + ":" + f.lastModified()
                    tokens.append(f'{fp}:{int(os.path.getmtime(fp) * 1000)}')

source_hash = hashlib.md5('\n'.join(sorted(tokens)).encode()).hexdigest()

# ── Write files ──
cache_dir = os.path.join(ROOT, '.gradle', 'test-cache')
os.makedirs(cache_dir, exist_ok=True)

with open(os.path.join(cache_dir, 'passing-android-tests.txt'), 'w') as f:
    f.write(f'# {len(passed)} passing test classes (from prod run)\n\n')
    f.write('\n'.join(passed) + '\n')

with open(os.path.join(cache_dir, 'android-test-filter.txt'), 'w') as f:
    f.write(','.join(remaining))

with open(os.path.join(cache_dir, 'source-hash-android.txt'), 'w') as f:
    f.write(source_hash)

print(f"\n[OK] passing-android-tests.txt : {len(passed)} classes")
print(f"[OK] android-test-filter.txt    : {len(remaining)} classes")
print(f"[OK] source-hash-android.txt    : {source_hash[:16]}...")
print(f"\nNext: ./gradlew quickAndroidProdTest")
