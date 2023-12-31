package com.github.motoshige021.sunflowercopyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.motoshige021.sunflowercopyapp.utilities.testCalender
import com.github.motoshige021.sunflowercopyapp.utilities.testGardenPlanting
import com.github.motoshige021.sunflowercopyapp.utilities.testPlant
import com.github.motoshige021.sunflowercopyapp.utilities.testPlants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.*

class GardenPlantingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var gardenPlantingDao: GardenPlantingDao
    private var testGardenPlantingId:Long = 0

    @get:Rule
    var instantTaskExecuteRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // インメモリデータベースより、ストレージのデータの影響なしにテストが出来る
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenPlantingDao = database.gardenPlantingDao()
        // テストデータ投入
        database.plantDao().insertAll(testPlants)
        testGardenPlantingId = gardenPlantingDao.insertGardenPlanting(testGardenPlanting)
    }

    @After
    fun closeDb() { database.close() }

    @Test
    fun testGardenPlanting() = runBlocking {
        // testPlants[0]のデータを投入済み、testPlants[1]のデータを投入すると
        // データベースが２件になることを確認する
        val gardenPlanting2 = GardenPlanting(
            testPlants[1].plantId, testCalender, testCalender
        ).also { it.gardenPlantId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGarden_plantings().first().size, Matchers.equalTo(2))
    }

    @Test
    fun testDeleteGardenPlanting() = runBlocking {
        // 既に存在するデータを挿入して件数が増えないこと、そのデータを削除して件数が減ること
        val gardenPlanting2 = GardenPlanting(
            testPlants[1].plantId, testCalender, testCalender
        ).also { it.gardenPlantId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGarden_plantings().first().size, Matchers.equalTo(2))
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGarden_plantings().first().size, Matchers.equalTo(1))
    }

    @Test
    fun testGetGardenPlaintToPlant() = runBlocking {
        // 既に存在するデータを指定して、isPlainted()がtrueを返す確認をするテスト
       assertTrue(gardenPlantingDao.isPlanted(testPlant.plantId).first())
    }

    @Test
    fun testGetGardenPlaintToPlantNotFound() = runBlocking {
        // 存在しないデータを指定して、isPlantedがfalseを返すのを確認するテスト
        assertFalse(gardenPlantingDao.isPlanted(testPlants[2].plantId).first())
    }

    @Test
    fun testGardenPlantAndGardenPlanting() = runBlocking {
        // 一覧を取得して件数が１件を確認する
        var plangtAndGardenPlantings = gardenPlantingDao.getPlantedGardens().first()
        assertThat(plangtAndGardenPlantings.size, Matchers.equalTo(1))
        // 取得した内容を確認するテスト
        assertThat(plangtAndGardenPlantings[0].plant, Matchers.equalTo(testPlant))
        assertThat(plangtAndGardenPlantings[0].gardenPlantings.size, Matchers.equalTo(1))
        assertThat(plangtAndGardenPlantings[0].gardenPlantings[0],
            Matchers.equalTo(testGardenPlanting))
    }
}