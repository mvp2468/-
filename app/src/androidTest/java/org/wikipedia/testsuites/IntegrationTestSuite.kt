package org.wikipedia.testsuites

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.wikipedia.database.AppDatabaseTests
import org.wikipedia.tests.IntegrationSmokeTest

@RunWith(Suite::class)
@SuiteClasses(
    IntegrationSmokeTest::class,
    AppDatabaseTests::class
)
class IntegrationTestSuite
