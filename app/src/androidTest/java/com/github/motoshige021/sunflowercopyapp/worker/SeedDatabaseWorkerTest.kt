package com.github.motoshige021.sunflowercopyapp.worker

import android.content.Context
import android.util.Log
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.assertThat
import androidx.work.ListenableWorker.Result
import org.hamcrest.CoreMatchers.`is`
import com.github.motoshige021.sunflowercopyapp.utilties.PLANT_DATA_FILE_NAME

@RunWith(JUnit4::class)
class SeedDatabaseWorkerTest {
    private lateinit var workManager: WorkManager
    private lateinit var context: Context
    private lateinit var configuration: Configuration

    @Before
    fun setup() {
        configuration = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun testRefreshMainDataWork() {
        // WorkerのInputDataにデータベースの初期化データのファイルを渡して、
        // データベースへのデータ投入のWorkerスレッドが成功するか確認するテスト
        var worker = TestListenableWorkerBuilder<SeedDatabaseWorker>(
            context = context,
            inputData = workDataOf(SeedDatabaseWorker.KEY_FILE_NAME to PLANT_DATA_FILE_NAME)
        ).build()

        val result = worker.startWork().get()
        assertThat(result, `is`(Result.success()))
    }
}