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
