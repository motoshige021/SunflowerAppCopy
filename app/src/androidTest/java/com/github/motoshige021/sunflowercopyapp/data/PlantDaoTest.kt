package com.github.motoshige021.sunflowercopyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlantDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var plantDao: PlantDao
    private val plantA = Plant("1", "A", "", 1, 1, "")
    private val plantB = Plant("2", "B", "", 1, 1, "")
    private val plantC = Plant("3", "C", "", 2, 2, "")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        plantDao = database.plantDao()

        plantDao.insertAll(listOf(plantA, plantB, plantC))
    }

    @After
    fun closeDb() { database.close() }

    @Test
    fun testGetPlants() = runBlocking {
        // Plant一覧を取得して3件であることを確認し、3件が正しいか確認するテスト
        var plants = plantDao.getPlants().first()
        assertThat(plants.size, equalTo(3))

        assertThat(plants[0], equalTo(plantA))
        assertThat(plants[1], equalTo(plantB))
        assertThat(plants[2], equalTo(plantC))
    }

    @Test
    fun testGetPlantsWithGrowZoneNumber() = runBlocking {
        // GrowZoneNumberを指定してPlant一覧を取得し、件数と内容が正しいか確認するテスト
        val plantList = plantDao.getPlantsWithGrowZoneNumber(1).first()
        assertThat(plantList.size, equalTo(2))
        assertThat(plantDao.getPlantsWithGrowZoneNumber(2).first().size,
                    equalTo(1))
        assertThat(plantDao.getPlantsWithGrowZoneNumber(3).first().size,
            equalTo(0))
        assertThat(plantList[0], equalTo(plantA))
        assertThat(plantList[1], equalTo(plantB))
    }

    @Test
    fun testGetPlant() = runBlocking {
        assertThat(plantDao.getPlant(plantB.plantId).first(), equalTo(plantB))
    }
}