package org.wikipedia.testsuites

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.wikipedia.database.AppDatabaseTests
import org.wikipedia.tests.categories.CategoryActivityTest
import org.wikipedia.tests.random.RandomArticleTest
import org.wikipedia.tests.readinglist.RecommendedReadingListTest
import org.wikipedia.tests.talk.TalkTopicsActivityTest
import org.wikipedia.tests.watchlist.WatchlistActivityTest
import org.wikipedia.tests.wiktionary.WiktionaryDialogComposeTest

@RunWith(Suite::class)
@SuiteClasses(
    AppDatabaseTests::class,
    CategoryActivityTest::class,
    RandomArticleTest::class,
    RecommendedReadingListTest::class,
    TalkTopicsActivityTest::class,
    WatchlistActivityTest::class,
    WiktionaryDialogComposeTest::class
)
class IntegrationTestSuite
