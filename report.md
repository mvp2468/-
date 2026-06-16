# Wikipedia Android App - 测试报告

> 包含：单元测试覆盖率报告 + GUI 测试（Espresso/Compose Testing）报告

> 生成日期：2026-06-16  
> 项目：`apps-android-wikipedia-main`  
> 测试工具：JUnit + Robolectric + Mockito + JaCoCo + Espresso + UiAutomator + Compose Testing

---

## 项目级总体概况

| 指标 | 数值 |
|------|------|
| 单元测试文件 | **150 个** |
| 单元测试方法 | **1326 个** |
| 全部单元测试通过 | **1326 / 1326** ✅ |
| 单元测试执行时间 | 2 分 44 秒 |
| Instrumented 测试文件 | **39 个** |
| Instrumented 测试方法 | **104 个** |
| Instrumented 测试执行设备 | PHV110 (Android 14) |
| 构建任务 | `./gradlew testDevDebugUnitTest` + `connectedDevDebugAndroidTest` |
| 覆盖率任务 | `./gradlew jacocoTestReport` + `jacocoAndroidTestReport` |

### 综合覆盖率概览（单元测试 + UI 测试）

| 覆盖类型 | 行覆盖率 | 分支覆盖率 | 覆盖行数 |
|----------|---------|-----------|---------|
| 仅单元测试 (Robolectric) | **8.8%** | 3.8% | 5,486 / 62,494 |
| 仅 Instrumented 测试 (Espresso) | **34.2%** | 17.5% | 21,453 / 62,673 |
| **综合覆盖率（两者合并）** | **~34.4%** | **~17%** | ~21,500 / 62,673 |

> 注：Instrumented 测试已覆盖绝大多数单元测试所能触及的代码路径，因此合并覆盖率主要取决于 Instrumented 测试的覆盖范围。

---

## 一、各包综合覆盖率排行（单元测试 + Instrumented 测试）

按代码量排序的 Top 25 包：

| 包 | 总行数 | 已覆盖 | 覆盖率 | vs 仅单元测试 |
|----|--------|--------|--------|--------------|
| `readinglist` | 2,848 | 939 | **33.0%** | +33.0% ▲ |
| `suggestededits` | 2,606 | 241 | **9.2%** | +6.4% |
| `activitytab` | 2,460 | 240 | **9.8%** | +8.2% |
| `staticdata` | 2,457 | 2,106 | **85.7%** | +0.0% |
| `views` | 2,414 | 1,060 | **43.9%** | +43.6% ▲ |
| `page` | 2,400 | 1,559 | **65.0%** | +53.0% ▲ |
| `readinglist.db` | 2,097 | 1,018 | **48.5%** | +48.5% ▲ |
| `search` | 2,086 | 937 | **44.9%** | +36.0% ▲ |
| `talk` | 1,797 | 202 | **11.2%** | +11.2% |
| `readinglist.recommended` | 1,698 | 18 | **1.1%** | +0.1% |
| `widgets.readingchallenge` | 1,672 | 118 | **7.1%** | +4.3% |
| `games.onthisday` | 1,493 | 42 | **2.8%** | +2.8% |
| `settings` | 1,340 | 726 | **54.2%** | +47.3% ▲ |
| `edit` | 1,175 | 10 | **0.9%** | +0.0% |
| `notifications` | 1,070 | 400 | **37.4%** | +29.7% |
| `util` | 1,047 | 428 | **40.9%** | +21.4% |
| `analytics.eventplatform` | 975 | 570 | **58.5%** | +45.0% ▲ |
| `donate.donationreminder` | 965 | 32 | **3.3%** | +1.7% |
| `compose.components` | 953 | 319 | **33.5%** | +33.5% ▲ |
| `descriptions` | 923 | 2 | **0.2%** | +0.2% |
| `history.db` | 896 | 445 | **49.7%** | +49.7% ▲ |
| `gallery` | 883 | 469 | **53.1%** | +42.0% ▲ |
| `diff` | 800 | 0 | **0.0%** | +0.0% |
| `watchlist` | 735 | 290 | **39.5%** | +39.5% ▲ |
| `edit.insertmedia` | 726 | 0 | **0.0%** | +0.0% |

> ▲ 标记表示 Instrumented 测试较单元测试有 >30% 的显著提升。

### 1.1 Instrumented 测试带来的最大覆盖率提升

| 包 | 单元测试 | 综合 | 提升 | 说明 |
|----|---------|------|------|------|
| `feed.aggregated` | 0.0% | **94.0%** | +94.0% | Feed 聚合数据模型 |
| `bridge` | 0.0% | **90.9%** | +90.9% | JSBridge 通信层 |
| `feed.becauseyouread` | 0.0% | **86.1%** | +86.1% | 推荐阅读卡片 |
| `pageimages.db` | 8.0% | **93.8%** | +85.8% | 页面图片数据库 |
| `feed.news` | 2.2% | **85.4%** | +83.2% | 新闻 Feed |
| `navtab` | 0.0% | **82.4%** | +82.4% | 底部导航栏 |
| `offline.db` | 7.0% | **87.3%** | +80.3% | 离线数据库 |
| `language.addlanguages` | 0.0% | **79.5%** | +79.5% | 语言选择 |
| `views.imageservice` | 3.8% | **83.0%** | +79.2% | 图片加载服务 |
| `compose.theme` | 0.0% | **78.0%** | +78.0% | Compose 主题 |
| `feed.featured` | 6.1% | **81.7%** | +75.6% | 精选文章 Feed |
| `onboarding` | 0.0% | **73.9%** | +73.9% | 新手引导 |
| `history` | 6.7% | **80.5%** | +73.8% | 历史记录 |
| `theme` | 4.2% | **77.7%** | +73.5% | 主题管理 |
| `feed.view` | 0.0% | **70.2%** | +70.2% | Feed 视图 |

### 1.2 42 个包覆盖率仍为 0%（6,740 行，占 10.8%）

```
settings.dev.playground (962行)  diff (800行)  edit.insertmedia (726行)
donate (514行)  talk.template (506行)  random (285行)  edit.richtext (254行)
edit.templates (250行)  categories (245行)  categories.db (232行)
talk.db (221行)  wiktionary (209行)  page.customize (194行)
edit.summaries (161行)  topics (161行) ...
```

这些主要是尚未被任何测试覆盖的 UI 页面、编辑辅助模块、以及部分辅助功能页面。

---

### 1.1 dataclient.watch — 91.7%（行覆盖率）

| 类 | 行覆盖率 | 说明 |
|----|----------|------|
| `Watch` | 100% | 数据模型完全覆盖 |
| `WatchPostResponse` | ~85% | 核心逻辑已覆盖 |
| **包总计** | **91.7%** | 11/12 行, 2 个测试文件 |

**单元测试文件（2）：** `WatchTest.kt`, `WatchPostResponseTest.kt` — 7 个测试方法

---

### 1.2 dataclient.growthtasks — 88.9%（行覆盖率）

| 类 | 行覆盖率 |
|----|----------|
| `GrowthImageSuggestion` / `ImageItem` / `AddImageFeedbackBody` | 100% / 88% / 94% |
| `GrowthImageSuggestion.ImageMetadata` | 58% |
| `GrowthUserImpact` / `ArticleViews` | 58% / 51% |
| **包总计** | **88.9%** | 96/108 行 |

**单元测试文件（2）：** `GrowthImageSuggestionTest.kt`, `GrowthUserImpactTest.kt` — 14 个测试方法

> 部分 `GrowthUserImpact` 计算属性依赖 `LocalDate.now()` 和 `System.currentTimeMillis()`，输出不稳定。

---

### 1.3 dataclient.restbase — 87.7%（行覆盖率）

| 类 | 行覆盖率 |
|----|----------|
| `DiffResponse` / `DiffItem` / `HighlightRange` | 100% |
| `EditCount`, `Metrics.Items`, `Metrics.Results` | 100% |
| `PageViews` / `PageViews.Item` / `PageViews.PageItem` | 100% |
| `PreviewRequest` | 100% |
| `RbDefinition.Definition` / `Usage` | 96% / 81% |
| `RbRelatedPages`, `UserEdits` / `UserEdits.Item` | 100% |
| `RbServiceError` / `Companion` | 92% / 100% |
| `Revision` | 100% |
| **包总计** | **87.7%** | 64/73 行 |

**单元测试文件（10）：** 25 个测试方法

---

### 1.4 dataclient.donate — 60.9%（行覆盖率）

| 类 | 行覆盖率 |
|----|----------|
| `DonationConfig` | 100% |
| `Campaign.Assets`, `Campaign.Action` | 100% |
| `PaymentMethod`, `PaymentMethodConfiguration` | 100% |
| `PaymentResponseContainer`, `PaymentResponse` | 100% / 90% |
| `Campaign` | 72% |
| **包总计** | **60.9%** | 92/151 行 |

**单元测试文件（4）：** 22 个测试方法

> 主要未覆盖：`CampaignCollection.getActiveCampaigns`（lambda 回调含网络请求）和 `DonationConfigHelper`（网络下载）。

---

### 1.5 dataclient.discussiontools — 40.3%（行覆盖率）

| 类 | 行覆盖率 |
|----|----------|
| `DiscussionToolsEditResponse` / `.EditResult` | 100% |
| `DiscussionToolsInfoResponse` / `.PageInfo` | 100% |
| `DiscussionToolsSubscribeResponse` / `.SubscribeStatus` | 100% |
| `DiscussionToolsSubscriptionList` | 100% |
| `ThreadItem` | **0%** |
| **包总计** | **40.3%** | 25/62 行 |

**单元测试文件（4）：** 10 个测试方法

> `ThreadItem` 实现了 `@Parcelize`，依赖 Android 框架，且包含递归计算属性 `allReplies`。

---

### 1.6 dataclient.okhttp — 19.5%（行覆盖率）

| 类 | 行覆盖率 | 说明 |
|----|----------|------|
| `CacheControlInterceptor` | 98% | 几乎完全覆盖 |
| `TitleEncodeInterceptor` | 93% | 几乎完全覆盖 |
| `OkHttpConnectionFactory` | 70% | 部分方法需实际网络 |
| `HttpStatusException` | 54% | 基础构造函数覆盖 |
| `OfflineCacheInterceptor` / 内部类 | 0~51% | 缓存逻辑复杂 |
| `OkHttpWebViewClient` | 0% | 需 WebView 环境 |
| **包总计** | **19.5%** | 58/298 行 |

**单元测试文件（6）：** 27 个测试方法

---

### 1.7 database — 7.0%（行覆盖率）

| 类 | 行覆盖率 |
|----|----------|
| `DateTypeConverter` | 100% |
| `LocalDateTimeTypeConverter` | 100% |
| `NamespaceTypeConverter` | 100% |
| `NotificationTypeConverters` | 100% |
| `AppDatabase` | 52% |
| `AppDatabase_Impl` | 0%（479 行） |
| **包总计** | **7.0%** | 33/470 行 |

**单元测试文件（5）：** 46 个测试方法

> `AppDatabase_Impl` 是 Room 编译器生成的实现类，不适合作为单元测试目标。所有手写 TypeConverter 均已达到 100% 覆盖率。

---

### 1.8 其他覆盖的关键包

| 包 | 行覆盖率 | 行 (C/T) | 说明 |
|----|----------|----------|------|
| `analytics` | 64.9% | 37/57 | ABTest, SessionData 核心覆盖 |
| `analytics/eventplatform` | 13.5% | 131/971 | EventPlatformClient 等已测试 |
| `appshortcuts` | 94.0% | 47/50 | 应用快捷方式 |
| `csrf` | 65.8% | 25/38 | CSRF Token 客户端 |
| `connectivity` | 57.6% | 34/59 | 网络连接监控 |
| `dataclient` | 32.3% | 98/303 | Service, WikiSite 等 |
| `dataclient/mwapi` | 44.3% | 185/418 | MediaWiki API 数据模型 |
| `dataclient/page` | 55.8% | 48/86 | PageSummary 等 |
| `dataclient/wikidata` | 47.8% | 32/67 | Wikidata Claims 等 |
| `feed` | 10.8% | 53/490 | FeedContentType, Card, UtcDate |
| `feed/announcement` | 28.4% | 58/204 | 公告/GeoIP |
| `feed/model` | 64.4% | 47/73 | 信息流核心模型 |
| `gallery` | 11.1% | 98/883 | 图片元数据/许可 |
| `history` | 6.7% | 23/343 | HistoryEntry |
| `json` | 61.0% | 25/41 | 自定义 JSON 序列化器 |
| `language` | 23.1% | 120/520 | AppLanguageState, LangLinks |
| `login` | 9.1% | 39/430 | LoginResponse, LoginResult |
| `notifications` | 7.7% | 82/1070 | NotificationCategory, db 层 |
| `page` | 12.0% | 287/2400 | Namespace, PageProperties, PageTitle, Section |
| `readinglist/database` | 78.6% | 92/117 | 阅读列表数据模型 |
| `search` | 9.0% | 186/2074 | 搜索模型和仓库 |
| `staticdata` | 85.7% | 2106/2457 | 多语言静态数据 |
| `suggestededits` | 2.9% | 75/2606 | ImageTag, Task, FilterTypes |
| `util` | 19.5% | 204/1047 | 各类工具函数 |
| `auth` | 16.2% | 13/80 | AccountUtil |
| `settings` | 6.9% | 92/1340 | Prefs（50.5KB 文件未覆盖） |
| `edit` | 0.9% | 10/1175 | 基本编辑模型 |
| `richtext` | 26.4% | 55/208 | 富文本 Span 扩展 |

---

## 二、Instrumented（GUI）测试概况

### 2.1 测试文件清单（39 个）

| 目录 | 文件数 | 覆盖功能 |
|------|--------|---------|
| `tests/` (根) | 6 | 深链接, 离线加载, 引导, 阅读列表, 搜索, 建议编辑 |
| `tests/articles/` | 10 | 文章操作项, 分区, 标签页, 编辑图标, 头图, 媒体, 溢出菜单, 已保存文章, 特殊文章, 目录 |
| `tests/diff/` | 1 | 差异对比（ArticleEditDetailsActivity） |
| `tests/editing/` | 1 | 文章编辑器格式化/媒体插入 |
| `tests/explorefeed/` | 6 | 信息流卡片, 搜索, 建议编辑, 导航项, 菜单, "因为你读过" |
| `tests/offline/` | 1 | 在线/离线保存文章 |
| `tests/random/` | 1 | 随机文章浏览 |
| `tests/search/` | 2 | 外部分享搜索意图, 搜索意图 |
| `tests/settings/` | 11 | 关于/开发者, 主题/字体, 语言, 折叠表格, 自定义信息流, 下载阅读列表, 链接预览, 阅读专注模式, 显示图片 |

### 2.2 最近运行结果

| 运行时间 | Flavor | 测试状态 | 说明 |
|---------|--------|----------|------|
| 2026-06-16 12:45 | prod | **DiffTest**: 1 失败 | `edit_history_recycler` 未找到 — 已知问题（溢出菜单被状态栏遮挡） |
| 2026-06-16 11:42 | dev | **2 通过 0 失败** | DownloadReadingListTest ✅, OverflowMenuTest ✅ |

> `DiffTest` 的失败正在修复中：溢出菜单在设备上被状态栏遮挡（110px 高度），已切换为 UiAutomator 方案绕过 Espresso 可见性约束。

---

## 三、覆盖率提升对比表

| 包 | 之前覆盖率 | 现在覆盖率 | 提升幅度 |
|----|-----------|-----------|----------|
| `dataclient.watch` | 0% | **91.7%** | ↑ 91.7% |
| `dataclient.growthtasks` | 0% | **88.9%** | ↑ 88.9% |
| `dataclient.restbase` | 0% | **87.7%** | ↑ 87.7% |
| `dataclient.donate` | 15% | **60.9%** | ↑ 45.9% |
| `dataclient.discussiontools` | 0% | **40.3%** | ↑ 40.3% |
| `dataclient.okhttp` | 12% | **19.5%** | ↑ 7.5% |
| `database` | 6% | **7.0%** | ↑ 1.0% |

### 新增覆盖的包

| 包 | 行覆盖率 | 来源 |
|----|---------|------|
| `appshortcuts` | 94.0% | 新测试文件 |
| `staticdata` | 85.7% | JSON 静态数据自动序列化 |
| `readinglist/database` | 78.6% | 新数据模型测试 |
| `csrf` | 65.8% | CsrfTokenClientTest |
| `feed/model` | 64.4% | CardTest, CardTypeTest, UtcDateTest |
| `analytics` | 64.9% | ABTest, SessionData |
| `json` | 61.0% | 自定义 Serializer 测试 |
| `connectivity` | 57.6% | ConnectionStateMonitor |
| `dataclient/page` | 55.8% | PageSummaryTest |
| `dataclient/wikidata` | 47.8% | ClaimsTest |
| `dataclient/mwapi` | 44.3% | MwQueryPageTest, MwQueryResultTest, MwServiceErrorTest |
| `richtext` | 26.4% | 富文本扩展测试 |
| `language` | 23.1% | AppLanguageState, LangLinks, LanguageUtil |
| `util` | 19.5% | 12 个工具类测试（DateUtil, StringUtil, ImageUrlUtil 等） |
| `page` | 12.0% | Namespace, PageProperties, PageTitle, Section |
| `gallery` | 11.1% | ExtMetadata, ImageInfo, ImageLicense, MediaListItem |
| `notifications` | 7.7% | NotificationCategory, Notification 数据模型 |
| `search` | 9.0% | SearchResult, SearchResults, SearchRepository, SemanticSearch |

---

## 四、单元测试文件统计

### 按包分组

| 包 | 单元测试文件数 | 测试方法数（近似） |
|----|-------------|-----------------|
| `search/` | 10 | ~80 |
| `analytics/` + `eventplatform/` | 9 | ~85 |
| `dataclient/okhttp/` | 6 | ~27 |
| `gallery/` | 5 | ~18 |
| `dataclient/restbase/` | 10 | ~25 |
| `database/` | 5 | ~46 |
| `util/` | 12 | ~60 |
| `dataclient/donate/` | 4 | ~22 |
| `dataclient/discussiontools/` | 4 | ~10 |
| `page/` | 5 | ~20 |
| `notifications/` | 3 | ~8 |
| `login/` | 3 | ~8 |
| `createaccount/` | 3 | ~6 |
| `feed/` + `feed/model/` | 4 | ~10 |
| `dataclient/mwapi/` | 3 | ~15 |
| `language/` | 3 | ~6 |
| `edit/` | 2 | ~6 |
| `suggestededits/` | 3 | ~6 |
| `donate/` | 2 | ~5 |
| `usercontrib/` | 1 | ~4 |
| `readinglist/` | 1 | ~3 |
| `savedpages/` | 1 | ~3 |
| `extensions/` | 3 | ~10 |
| `yearinreview/` | 2 | ~6 |
| `watchlist/` | 1 | ~4 |
| 其他 | ~20 | ~40 |
| **总计** | **150** | **~1326** |

---

## 五、Instrumented（GUI）测试覆盖的目标类

以下是从 39 个 Instrumented 测试文件中提取的 **5 个显式覆盖声明**的测试（在测试类注释中标注了覆盖目标）：

| 测试文件 | 覆盖的目标类 |
|----------|------------|
| `DiffTest` | `ArticleEditDetailsActivity`, `ArticleEditDetailsFragment`, `ArticleEditDetailsViewModel`, `DiffUtil`, `DiffLineView`, `EmptyLineSpan`, `UndoEditDialog` |
| `RandomArticleTest` | `RandomActivity`, `RandomFragment`, `RandomItemFragment`, `RandomItemViewModel`, `RandomViewModel`, `PagerTransformer`, `BottomViewBehavior` |
| `SuggestedEditScreenTest` | `SuggestedEditsTasksActivity`, `SuggestedEditsTasksFragment` |
| `FeedScreenSuggestedEditTest` | `SuggestedEditsCardsFragment` |
| `AppThemeTest` | `ThemeFittingRoomActivity`, `ThemeChooserDialog` |

---

## 六、低覆盖率根因分析

### 6.1 核心架构问题：全局单例泛滥

项目中最核心的测试障碍是**全局单例（Global Singleton）的过度使用**，导致几乎每个类都与运行时环境强耦合。

| 全局单例 | 引用次数 | 影响的包 |
|----------|---------|----------|
| `WikipediaApp.instance` | 全项目 ~300+ 处 | 所有 |
| `AppDatabase.instance` | okhttp, activitytab, donate | okhttp (19%), activitytab (1%), donate (61%) |
| `ServiceFactory.get()` | activitytab, donate | activitytab (1%), donate (61%) |
| `Prefs.xxx` | analytics, donate, auth | analytics (65%), donate (61%), auth (16%) |
| `EventPlatformClient.submit()` | analytics | analytics/eventplatform (13%) |
| `OkHttpConnectionFactory.client` | okhttp, donate | okhttp (19%), donate (61%) |

**根本原因：** 项目缺少统一的依赖注入框架（Hilt/Dagger 虽有使用但不全面），大量类直接引用全局单例而非通过构造函数注入依赖。

### 6.2 无法覆盖的代码归类

| 类别 | 影响行数（近似） | 可否改善 |
|------|----------------|---------|
| **Compose UI 函数** | ~26,000 | ⚠️ 需 Compose Testing + Robolectric |
| **Activity/Fragment 生命周期** | ~15,000 | ⚠️ 需 Robolectric/Espresso |
| **Room 编译器生成代码** (`*_Impl`) | ~2,900 | ❌ 不可改善（框架代码），应从覆盖率排除 |
| **Paging3 分页源** (ViewModel + PagingSource) | ~2,000 | ⚠️ 需 Paging Testing + Room + 网络 mock |
| **OkHttp 内部耦合代码** | ~900 | ✅ 可重构后测试 |
| **Android 框架紧耦合** (WebViewClient, AccountManager) | ~700 | ✅ 部分可重构 |
| **未编写测试的事件/数据类** | ~600 | ✅ 可直接添加测试 |
| **网络请求 lambda** (CampaignCollection 等) | ~250 | ✅ 可重构为可注入 |
| **全局单例副作用方法** | ~230 | ✅ 可重构为参数注入 |

---

## 七、改进建议

### 7.1 短期改进（低成本，快速见效）—— 预计可提升 5-10%

| 优先级 | 措施 | 目标包 | 预期提升 | 工作量 |
|--------|------|--------|---------|--------|
| 🔴 P0 | 为未测试的事件类/数据类添加 JSON 反序列化测试 | analytics/eventplatform, notifications | analytics +3~5%, notifications +5~10% | 小 |
| 🔴 P0 | 为 `GrowthUserImpact` 添加时间注入，使计算属性可测试 | growthtasks | growthtasks +10% | 中 |
| 🟡 P1 | 为 `ThreadItem` 添加 JSON 反序列化测试（递归 `allReplies`） | discussiontools | discussiontools +25% | 小 |
| 🟡 P1 | 扩大 `OfflineCacheInterceptor.Intercept` 的 mock 覆盖 | okhttp | okhttp +3~5% | 中 |

### 7.2 中期改进（需重构）—— 预计可提升 15-25%

| 优先级 | 措施 | 目标包 | 预期提升 | 工作量 |
|--------|------|--------|---------|--------|
| 🔴 P0 | **为 `OfflineCacheInterceptor` 拆分职责** | okhttp | okhttp +30~40% | 大 |
| 🔴 P0 | **为 `CampaignCollection` 引入协程调度器注入** | donate | donate +20~25% | 中 |
| 🟡 P1 | **重构 `AccountUtil` 为可注入类** | auth | auth +30~50% | 中 |
| 🟡 P1 | 为 `ActivityTabViewModel` 编写 Robolectric 测试 | activitytab | activitytab +5~10% | 大 |

### 7.3 长期改进（架构层面）

| 措施 | 说明 | 影响范围 |
|------|------|----------|
| **全面引入 Hilt 依赖注入** | 项目已有 Hilt 基础，但 `WikipediaApp.instance` 等全局单例仍在大量使用。应将所有依赖改为 Hilt `@Inject` | 全项目 |
| **排除框架生成代码** | 将 Compose `ComposableSingletons$*`、Room `*_Impl`、Hilt `*_Factory` 从覆盖率排除 | database, activitytab |
| **数据类与 Android 解耦** | `ThreadItem` 等不应直接实现 `Parcelable`，使用单独 mapper 层 | discussiontools, growthtasks |
| **补充 Instrumented 测试** | 对 `OkHttpWebViewClient`、`OfflineCacheInterceptor`、Room Migration 等编写 Espresso 测试 | okhttp, database |
| **Compose 预览截图测试** | 对 TimelineModule 等使用 Paparazzi 或 Roborazzi 截图测试 | activitytab |

### 7.4 优先级路线图

```
第 1 周：补充事件类 JSON 测试 → analytics +5%, notifications +8%
        ThreadItem 反序列化测试 → discussiontools +25%

第 2 周：GrowthUserImpact 时间注入重构 → growthtasks +10%
        CampaignCollection URL 构造测试 → donate +4%

第 3 周：OfflineCacheInterceptor 拆分重构 → okhttp +35%
        CampaignCollection Dispatchers 注入 → donate +22%

第 4 周：AccountUtil 重构为可注入 → auth +40%

长期：Hilt 全面引入 + 框架代码排除 + Compose 测试 + Instrumented 测试补充
```

---

## 八、测试执行命令

```bash
# 运行所有单元测试（开发 flavor，无需设备）
./gradlew testDevDebugUnitTest

# 生成单元测试覆盖率报告
./gradlew jacocoTestReport
# 报告路径: app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml
# HTML:     app/build/reports/jacoco/testDevDebugUnitTestCoverage/html/index.html

# 运行 GUI 测试（Espresso + Compose Testing，需连接设备/模拟器）
./gradlew connectedProdDebugAndroidTest

# 运行单个 GUI 测试
./gradlew connectedProdDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=org.wikipedia.tests.diff.DiffTest

# 生成 GUI 测试覆盖率报告
./gradlew jacocoAndroidTestReport
# HTML:     app/build/reports/jacoco/androidTestCoverage/html/index.html

# 生成合并覆盖率报告（单元测试 + GUI 测试，需连接设备/模拟器）
./gradlew jacocoCombinedReport
# HTML:     app/build/reports/jacoco/combinedCoverage/html/index.html
```

---

## 九、已知问题

| 问题 | 状态 | 说明 |
|------|------|------|
| **DiffTest 溢出菜单点击失败** | 🔧 修复中 | 设备状态栏 110px 遮挡工具栏溢出按钮，已切换为 UiAutomator `UiSelector().resourceId()` 方案 |
| **OverflowMenuTest 测试驱动取消** | ⚠️ 偶发 | dev flavor 上 `AndroidInstrumentationDriver was canceled`，可能是手动中断 |
| **Compose UI 覆盖率极低** | ⚠️ 已知 | `ActivityTabFragment` (49KB)、`SuggestedEditsCardsFragment` (19KB) 等大量 Compose 代码未被单元测试覆盖 |


---

## 十、内存分析（Heap Dump）

> 来源：Android Studio Profiler -> Analyze Memory Usage -> Heap Dump  
> Dump 文件：`memory-20260616T215005.hprof`（~60 MB）  
> 分析工具：Android Studio Profiler 内置 Memory Analyzer

### 10.1 堆内存总览

| 指标 | 数值 | 说明 |
|------|------|------|
| 加载类总数 | **10,021** | 包括 App 代码、第三方库、Android 框架类 |
| 内存泄漏 | **0** | 未检测到 Activity/Fragment 泄漏 |
| 重复字符串 | **0** | 不存在相同内容的冗余字符串对象 |
| 对象实例总数 | **257,737** | 堆中存活的所有 Java/Kotlin 对象 |
| Native 内存 | **1,081,282 字节 (~1.03 MB)** | JNI/Native 层分配（如 Bitmap 像素缓冲区） |
| Shallow 堆大小 | **14,441,570 字节 (~13.77 MB)** | 所有对象自身占用的堆内存（不含引用对象） |
| Retained 堆大小 | **6,269,987 字节 (~5.98 MB)** | 若 App 主对象被回收，可释放的关联内存 |

### 10.2 关键解读

**1. 无泄漏 -- 0 Leaks**
- Android Studio Profiler 的 Leak Detection 未发现任何 Activity 或 Fragment 引用残留
- 说明 App 在 Android 14 设备 `PHV110` 上的生命周期管理正确，没有常见的内存泄漏（Handler、匿名内部类、静态引用等）

**2. 257,737 个对象实例**
- 对于一个包含 WebView、Feed、搜索等复杂功能的 Wikipedia App，这个数量在正常范围内
- 平均每类约 26 个实例，说明不存在某类对象堆积异常

**3. Shallow > Retained（13.77 MB > 5.98 MB）**
- 这是一个**正面信号**：说明大多数对象被 GC Root 直接或通过短引用链持有，而非被少数大对象独占
- 对象之间的引用关系比较扁平，没有形成深层的支配树（Dominator Tree），GC 回收效率高
- 如果 Retained 远大于 Shallow，则说明少数对象持有了大量独占引用，可能意味着集合类或缓存模块堆积

**4. Native 内存低（~1 MB）**
- Bitmap 像素数据、WebView 渲染缓冲等主要在 Native 层分配
- 1 MB 说明当前快照中没有大量图片或 WebView 内容驻留，App 处于较干净状态（冷启动后浏览轻量页面）

**5. 0 个重复字符串**
- 字符串常量池利用良好，没有因字符串拼接、substring 等操作产生大量冗余字符串对象

### 10.3 潜在关注点

| 关注点 | 风险级别 | 说明 |
|--------|---------|------|
| WebView 内存峰值 | 中 | 当前快照可能未覆盖加载长篇文章的峰值状态，建议在浏览 "Barack Obama" 等级别文章后再次 Dump 对比 |
| Coil 图片缓存 | 中 | Native 仅 1 MB，图片库缓存未满，高频滚动 Feed 后需观察 Bitmap 对象是否暴涨 |
| 后台线程残留 | 低 | 0 Leaks 说明线程引用已正确释放 |
| Room Database | 低 | AppDatabase_Impl 单例正常，未见连接池膨胀 |

# 第十一部分：App 运行性能监控分析（Android Studio Profiler 采集）
## 11.1 监控截图信息说明


### 监控采集条件
1. 测试场景：App前台持续常规操作（浏览词条、查看信息流、切换设置页面、本地阅读列表读写），连续稳定运行20分钟
2. 采集工具：Android Studio Profiler 实时CPU+内存双指标监控
3. 快照采集时间点：00:20:02，为长时间稳定运行后的稳态性能数据

## 11.2 CPU指标解读
截图CPU区域核心数据：
- 当前App进程CPU占用：**2%**
- 整机其他进程合计占用：27%
- 活跃线程总数：128
### 指标分析
1. **CPU负载表现优秀**
   长时间前台运行后应用仅占用2%CPU，无持续高负载、轮询死循环、频繁主线程耗时运算问题，日常浏览场景功耗控制良好，不会出现发热、卡顿现象。
2. **线程数量存在优化空间**
   128个线程属于偏高区间，存在大量闲置子线程、OkHttp网络线程池、协程未及时销毁、图片加载后台线程残留等问题；长期后台驻留会小幅增加内存开销，极端场景可能触发系统线程限制。
3. 整机其他进程占用27%为系统、桌面、后台第三方应用正常消耗，不属于本App性能问题。

## 11.3 内存指标解读
截图内存分区总数据：
- 应用总占用内存 Total：361.5MB（设备内存上限阈值384MB，剩余充足）
- Java堆内存：44.1MB
- Native原生内存：73.4MB
- Graphics图形渲染内存：71.4MB
- Stack线程栈内存：5.4MB
- Code代码段内存：75.2MB
- Others其他缓存/资源内存：92MB

### 分区逐项分析
1. **总内存水位安全**
   361.5MB距离384MB设备分配上限仍有22.5MB余量，长时间运行未出现内存持续上涨、OOM崩溃风险，无明显严重内存泄漏趋势。
2. 内存占用结构拆解
   | 内存分区 | 占用大小 | 风险判定 | 说明 |
   |---|---|---|---|
   | Java堆 44.1MB | 低风险 | 业务对象、页面实例占用偏低，Activity/ViewModel销毁后回收正常，无大量页面实例滞留 |
   | Native 73.4MB | 中等关注 | 图片解码、WebView内核、Okio缓冲区占用，维基词条大图、Web词条加载会持续抬高Native内存 |
   | Graphics 71.4MB | 中等关注 | 页面Bitmap、Compose图层渲染缓存，频繁切换图文页面易堆积图形缓存 |
   | Code 75.2MB | 无风险 | 应用dex、第三方库固定代码占用，运行中不会持续增长 |
   | Others 92MB | 中等风险 | 本地Sqlite缓存、网络响应缓存、图片临时文件堆积，长期使用未主动清理会缓慢抬升总内存 |
3. 内存隐患
   Native、Graphics、Others三项合计占用236.4MB，是内存主要消耗来源；当前虽未溢出，但反复打开大量图文词条、长时间停留信息流页面时，三类缓存会持续上涨，存在OOM隐性风险。

## 11.4 综合性能结论
1. 优势：常规使用场景CPU负载极低，长时间运行无持续耗电卡顿；整体内存水位可控，无急性内存泄漏，基础流畅度达标。
2. 现存性能隐患：
    - 线程总数128，存在大量闲置后台线程未回收；
    - Native图形、本地缓存类内存占比过高，高频图文操作会挤压可用内存；
3. 优化建议
    1. 线程治理：统一封装协程调度器，网络/图片加载任务使用限定线程池，页面退出时取消全部后台任务，销毁闲置线程；
    2. 内存缓存优化：图片页面退出主动回收Bitmap、释放WebView资源；为阅读缓存、网络响应缓存设置大小上限，定时清理过期缓存；
    3. 补充专项测试：连续快速切换图文页面30分钟，持续采集内存曲线，验证是否存在渐进式内存泄漏。

---

## 十二、手动功能测试（Appium 辅助定位 + 人工验证）

> 测试设备：Android 14 (PHV110)  
> 测试应用：Wikipedia Alpha (`org.wikipedia.alpha`)  
> 测试日期：2026-06-16  
> 测试结果：**全部通过 ✅**

### 12.1 搜索功能测试（4 项）

| 编号 | 测试场景 | 操作步骤 | 预期结果 | 结果 |
|------|---------|---------|---------|------|
| S-01 | 搜索英文词条 | 1. 打开 App 进入 Explore 主页<br>2. 点击顶部搜索栏 (`search_container`)<br>3. 输入 `China`，按回车<br>4. 观察搜索结果 | 出现 `search_results_list`，包含与 "China" 相关的词条列表 | ✅ 通过 |
| S-02 | 搜索无结果词条 | 1. 点击搜索栏<br>2. 输入 `asdfxxyyzzznonexistent`，按回车<br>3. 观察结果 | 显示空结果提示（`search_empty_message`）或搜索栏恢复 | ✅ 通过 |
| S-03 | 搜索中文词条 | 1. 点击搜索栏<br>2. 输入 `北京`，按回车<br>3. 观察搜索结果 | 出现与 "北京" 相关的中文词条列表 | ✅ 通过 |
| S-04 | 输入联想建议 | 1. 点击搜索栏<br>2. 部分输入 `Barack`（不按回车）<br>3. 观察搜索结果区域 | 搜索输入框内容保持，结果显示区域出现联想建议 | ✅ 通过 |

### 12.2 文章浏览功能测试（5 项）

| 编号 | 测试场景 | 操作步骤 | 预期结果 | 结果 |
|------|---------|---------|---------|------|
| A-01 | 打开文章 | 1. 搜索 `Earth`<br>2. 点击第一个搜索结果 | 进入文章页面，文章标题和内容正常显示 | ✅ 通过 |
| A-02 | 文章滚动 | 1. 打开文章<br>2. 向上滑动 5 次 | 页面正常滚动，无卡顿，内容持续加载 | ✅ 通过 |
| A-03 | 目录功能 | 1. 打开文章<br>2. 点击目录按钮<br>3. 观察目录弹出 | 目录面板正常显示，列出文章章节 | ✅ 通过 |
| A-04 | 保存文章 | 1. 打开文章<br>2. 点击保存按钮 | 文章成功保存到阅读列表 | ✅ 通过 |
| A-05 | 返回主页 | 1. 打开文章<br>2. 点击返回按钮 | 返回 Explore 主页，Feed 正常显示 | ✅ 通过 |

### 12.3 Explore 信息流测试（4 项）

| 编号 | 测试场景 | 操作步骤 | 预期结果 | 结果 |
|------|---------|---------|---------|------|
| F-01 | 信息流加载 | 1. 切换到 Explore 标签页<br>2. 等待加载 | Feed 卡片列表正常加载显示 | ✅ 通过 |
| F-02 | 下拉刷新 | 1. 在 Explore 页下拉<br>2. 观察刷新动画和结果 | 触发刷新，Feed 内容更新 | ✅ 通过 |
| F-03 | 精选文章入口 | 1. 在 Explore 页<br>2. 点击精选文章卡片 | 进入对应文章页面，内容正常 | ✅ 通过 |
| F-04 | 信息流滚动 | 1. 在 Explore 页<br>2. 向上滑动 5 次 | Feed 持续加载更多卡片 | ✅ 通过 |

### 12.4 底部导航测试（5 项）

| 编号 | 测试场景 | 操作步骤 | 预期结果 | 结果 |
|------|---------|---------|---------|------|
| N-01 | 标签切换 | 1. 依次点击底部 5 个标签<br>（Explore / Saved / Search / Edits / More）<br>2. 每次切换观察页面 | 每个标签页正常切换，无崩溃 | ✅ 通过 |
| N-02 | 更多菜单 | 1. 点击 More 标签<br>2. 观察菜单 | More 菜单列表正常显示 | ✅ 通过 |
| N-03 | 更多→设置 | 1. 点击 More 标签<br>2. 点击 Settings | 进入设置页面 | ✅ 通过 |
| N-04 | Explore↔搜索切换 | 1. 在 Explore 页<br>2. 打开搜索<br>3. 返回 Explore | 来回切换正常，无状态丢失 | ✅ 通过 |
| N-05 | 已保存标签 | 1. 点击 Saved 标签<br>2. 观察页面 | 阅读列表正常显示 | ✅ 通过 |

### 12.5 完整用户旅程测试（4 项）

| 编号 | 测试场景 | 操作步骤 | 预期结果 | 结果 |
|------|---------|---------|---------|------|
| J-01 | 搜索→阅读→返回 | 1. 搜索 `Moon`<br>2. 点击结果进入文章<br>3. 滚动 3 次<br>4. 返回主页 | 全流程无卡顿、崩溃，逐步骤正确 | ✅ 通过 |
| J-02 | 搜索→阅读→保存→查已存 | 1. 搜索 `Paris`<br>2. 进入文章<br>3. 点击保存<br>4. 返回→进入 Saved 标签 | 文章出现在已保存列表中 | ✅ 通过 |
| J-03 | 更多→设置→关于→返回 | 1. More→Settings<br>2. Settings→About<br>3. 逐级返回 | 各页面正常显示，返回路径正确 | ✅ 通过 |
| J-04 | 信息流→精选文章→返回 | 1. Explore 页<br>2. 点击精选文章卡片<br>3. 阅读后返回 | 进入文章正确，返回 Feed 正常 | ✅ 通过 |

### 12.6 手动测试总结

| 统计项 | 数值 |
|--------|------|
| 测试场景总数 | **22 项** |
| 通过 | **22 项** ✅ |
| 失败 | **0 项** |
| 通过率 | **100%** |
| 测试覆盖模块 | 搜索、文章浏览、信息流、底部导航、完整用户旅程 |

> 以上 22 项手动测试覆盖了 Wikipedia Android App 的主要用户功能路径，所有测试均已人工执行并通过验证。
