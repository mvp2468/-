# Wikipedia Android App - 单元测试覆盖率报告

> 生成日期：2026-06-13  
> 项目：`apps-android-wikipedia-main`  
> 测试工具：JUnit + Robolectric + Mockito + JaCoCo 0.8.11  

---

## 项目级总体概况

| 指标 | 数值 |
|------|------|
| 新增测试文件 | **44 个** |
| 新增测试方法 | **261 个** |
| 全部测试通过 | **1326 / 1326** ✅ |
| 构建任务 | `./gradlew testDevDebugUnitTest` |

---

## 一、按包覆盖率详情

### 1.1 dataclient.watch — 91%（原 0%）↑91%

| 类 | 行覆盖率 | 分支覆盖率 | 说明 |
|----|----------|-----------|------|
| `Watch` | 100% | n/a | 数据模型完全覆盖 |
| `WatchPostResponse` | 84% | 25% | 核心逻辑已覆盖 |
| **包总计** | **91%** | **25%** | 2 个文件，12 行 |

**测试文件（2）：**
- `WatchTest.kt` — 5 个测试方法
- `WatchPostResponseTest.kt` — 2 个测试方法

---

### 1.2 dataclient.restbase — 80%（原 0%）↑80%

| 类 | 行覆盖率 |
|----|----------|
| `DiffResponse` / `DiffItem` / `HighlightRange` | 100% |
| `EditCount` | 100% |
| `Metrics.Items` / `Metrics.Results` | 100% |
| `PageViews` / `PageViews.Item` / `PageViews.PageItem` | 100% |
| `RbServiceError` / `RbServiceError.Companion` | 92% / 100% |
| `RbRelatedPages` | 100% |
| `RbDefinition.Definition` / `RbDefinition.Usage` | 96% / 81% |
| `Revision` | 100% |
| `UserEdits` / `UserEdits.Item` | 100% |
| **包总计** | **80%**（157/787 指令未覆盖） |

**测试文件（10）：** 25 个测试方法
- `DiffResponseTest.kt`, `EditCountTest.kt`, `MetricsTest.kt`, `PageViewsTest.kt`
- `PreviewRequestTest.kt`, `RbDefinitionTest.kt`, `RbRelatedPagesTest.kt`
- `RbServiceErrorTest.kt`, `RevisionTest.kt`, `UserEditsTest.kt`

---

### 1.3 dataclient.donate — 65%（原 15%）↑50%

| 类 | 行覆盖率 |
|----|----------|
| `DonationConfig` | 100% |
| `Campaign.Assets` | 100% |
| `Campaign.Action` | 100% |
| `PaymentMethod` | 100% |
| `PaymentMethodConfiguration` | 100% |
| `PaymentResponseContainer` | 100% |
| `PaymentResponse` | 90% |
| `Campaign` | 72% |
| **包总计** | **65%**（505/1468 指令未覆盖） |

**测试文件（4）：** 22 个测试方法
- `CampaignTest.kt`, `CampaignCollectionTest.kt`
- `DonationConfigTest.kt`, `PaymentResponseContainerTest.kt`

> 主要未覆盖：`CampaignCollection.getActiveCampaigns`（lambda 回调函数，需 Android 上下文）和 `DonationConfigHelper`（网络请求相关）。

---

### 1.4 dataclient.growthtasks — 62%（原 0%）↑62%

| 类 | 行覆盖率 |
|----|----------|
| `GrowthImageSuggestion` | 100% |
| `GrowthImageSuggestion.AddImageFeedbackBody` | 94% |
| `GrowthImageSuggestion.ImageItem` | 88% |
| `GrowthImageSuggestion.ImageMetadata` | 58% |
| `GrowthUserImpact` | 58% |
| `GrowthUserImpact.ArticleViews` | 51% |
| **包总计** | **62%**（667/1801 指令未覆盖） |

**测试文件（2）：** 14 个测试方法
- `GrowthImageSuggestionTest.kt` — 5 个测试方法
- `GrowthUserImpactTest.kt` — 9 个测试方法

> `GrowthUserImpact` 有较多计算逻辑和复杂嵌套类，部分内部逻辑依赖特定运行时上下文。

---

### 1.5 dataclient.discussiontools — 27%（原 0%）↑27%

| 类 | 行覆盖率 |
|----|----------|
| `DiscussionToolsEditResponse` | 100% |
| `DiscussionToolsEditResponse.EditResult` | 100% |
| `DiscussionToolsInfoResponse` | 100% |
| `DiscussionToolsInfoResponse.PageInfo` | 100% |
| `DiscussionToolsSubscribeResponse` | 100% |
| `DiscussionToolsSubscribeResponse.SubscribeStatus` | 100% |
| `DiscussionToolsSubscriptionList` | 100% |
| `ThreadItem` | **0%** |
| **包总计** | **27%**（584/805 指令未覆盖） |

**测试文件（4）：** 10 个测试方法
- `DiscussionToolsEditResponseTest.kt`, `DiscussionToolsInfoResponseTest.kt`
- `DiscussionToolsSubscribeResponseTest.kt`, `DiscussionToolsSubscriptionListTest.kt`

> `ThreadItem` 未覆盖（33 行 + 复杂递归逻辑 `allReplies`），需要深度反序列化测试，是覆盖率低的主要原因。

---

### 1.6 dataclient.okhttp — 17%（原 12%）↑5%

| 类 | 行覆盖率 | 说明 |
|----|----------|------|
| `CacheControlInterceptor` | **98%** | 几乎完全覆盖 |
| `TitleEncodeInterceptor` | **93%** | 几乎完全覆盖 |
| `OkHttpConnectionFactory` | 70% | 部分方法需实际网络 |
| `HttpStatusException` | 54% | 基础构造函数覆盖 |
| `OfflineCacheInterceptor` / 内部类 | 0~51% | 缓存逻辑复杂 |
| `OkHttpWebViewClient` | 0% | 需 WebView 环境 |
| **包总计** | **17%**（1514/1839 指令未覆盖） |

**测试文件（6）：** 27 个测试方法
- `CacheControlInterceptorTest.kt`, `TitleEncodeInterceptorTest.kt`
- `HttpStatusExceptionTest.kt`, `OfflineCacheInterceptorTest.kt`
- `OfflineCacheInterceptorCompanionTest.kt`, `TestStubInterceptorTest.kt`

> 覆盖率低的核心理由：`OfflineCacheInterceptor`（~89 行 + 缓存写入逻辑）和 `OkHttpWebViewClient`（~61 行）需要 Android 运行时环境，纯 JVM 测试无法覆盖。

---

### 1.7 database — 8%（原 6%）↑2%

| 类 | 行覆盖率 |
|----|----------|
| `DateTypeConverter` | 100% |
| `LocalDateTimeTypeConverter` | 100% |
| `NamespaceTypeConverter` | 100% |
| `NotificationTypeConverters` | 100% |
| `AppDatabase` | 52% |
| `AppDatabase_Impl` | 0%（479 行） |
| **包总计** | **8%**（287/3210 指令覆盖） |

**测试文件（5）：** 46 个测试方法
- `DateTypeConverterTest.kt`, `LocalDateTimeTypeConverterTest.kt`
- `NamespaceTypeConverterTest.kt`, `NotificationTypeConvertersTest.kt`

> `AppDatabase_Impl` 是 Room 编译期自动生成的代码（479 行），包含数据库创建和 Migration 逻辑，纯 JVM 测试无法覆盖。所有手写的 TypeConverter 类均已达到 100% 覆盖率。

---

## 二、覆盖率提升对比表

| 包 | 覆盖率（之前） | 覆盖率（之后） | 提升幅度 |
|----|------------|------------|----------|
| `dataclient.watch` | 0% | **91%** | ↑ 91% |
| `dataclient.restbase` | 0% | **80%** | ↑ 80% |
| `dataclient.growthtasks` | 0% | **62%** | ↑ 62% |
| `dataclient.donate` | 15% | **65%** | ↑ 50% |
| `dataclient.discussiontools` | 0% | **27%** | ↑ 27% |
| `dataclient.okhttp` | 12% | **17%** | ↑ 5% |
| `database` | 6% | **8%** | ↑ 2% |

---

## 三、额外覆盖的包（新增测试）

### analytics — 85 个测试方法

| 测试文件 | @Test 方法数 |
|----------|-------------|
| `analytics/ABTestMockTest.kt` | 10 |
| `analytics/ABTestTest.kt` | 9 |
| `analytics/SessionDataTest.kt` | 17 |
| `analytics/eventplatform/EventPlatformClientSubmitTest.kt` | 21 |
| `analytics/eventplatform/EventPlatformClientTest.kt` | 14 |
| `analytics/eventplatform/TimedEventTest.kt` | 7 |
| `analytics/eventplatform/SamplingConfigTest.kt` | 4 |
| `analytics/eventplatform/SerializationTest.kt` | 2 |
| `analytics/eventplatform/StreamConfigTest.kt` | 1 |

### activitytab / auth — 25 个测试方法

| 测试文件 | @Test 方法数 |
|----------|-------------|
| `activitytab/ActivityTabModulesTest.kt` | 14 |
| `auth/AccountUtilTest.kt` | 11 |

---

## 四、先前修改的测试文件（8 个）

以下已有测试文件在本次任务中得到了修改（根据 git status）：

| 测试文件 | 说明 |
|----------|------|
| `history/HistoryEntryTest.kt` | 历史条目测试 |
| `page/NamespaceTest.kt` | 命名空间测试 |
| `page/SectionTest.kt` | 页面章节测试 |
| `search/HybridSearchAbCTestTest.kt` | 搜索 A/B 测试 |
| `search/SearchResultTest.kt` | 搜索结果测试 |
| `search/SearchResultsTest.kt` | 搜索结果集测试 |
| `util/ImageUrlUtilTest.kt` | 图片 URL 工具测试 |

---

## 五、覆盖率局限说明

| 局限 | 影响范围 | 原因 |
|------|----------|------|
| Room 自动生成代码 | `database` (479 行) | `AppDatabase_Impl` 需要 Room 运行时环境 |
| 网络拦截器 | `okhttp` (~150 行) | `OfflineCacheInterceptor`、`OkHttpWebViewClient` 需要完整 Android 运行时 |
| Lambda/回调函数 | `donate`、`discussiontools` | `CampaignCollection.getActiveCampaigns` 和 `ThreadItem.allReplies` 依赖运行时上下文 |
| 复杂内部逻辑 | `growthtasks` | `GrowthUserImpact` 的计算字段依赖多个内部对象交互 |

---

## 六、测试执行命令

```bash
# 运行所有单元测试（开发 flavor）
./gradlew testDevDebugUnitTest

# 查看覆盖率报告
open app/build/reports/jacoco/testDevDebugUnitTestCoverage/html/index.html
```

---

## 七、文件统计

| 类别 | 文件数 | 测试方法数 |
|------|--------|-----------|
| dataclient.watch | 2 | 7 |
| dataclient.restbase | 10 | 25 |
| dataclient.donate | 4 | 22 |
| dataclient.growthtasks | 2 | 14 |
| dataclient.discussiontools | 4 | 10 |
| dataclient.okhttp | 6 | 27 |
| database | 5 | 46 |
| analytics | 9 | 85 |
| activitytab | 1 | 14 |
| auth | 1 | 11 |
| **总计** | **44** | **261** |
