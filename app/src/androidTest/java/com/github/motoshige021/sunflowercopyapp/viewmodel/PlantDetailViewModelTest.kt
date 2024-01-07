package com.github.motoshige021.sunflowercopyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.github.motoshige021.sunflowercopyapp.MainCoroutineRule
import com.github.motoshige021.sunflowercopyapp.data.AppDatabase
import com.github.motoshige021.sunflowercopyapp.data.GardenPlantingRepository
import com.github.motoshige021.sunflowercopyapp.data.PlantRepository
import com.github.motoshige021.sunflowercopyapp.runBlockingTest
import com.github.motoshige021.sunflowercopyapp.utilities.getValue
import com.github.motoshige021.sunflowercopyapp.utilities.testPlant
import com.github.motoshige021.sunflowercopyapp.utilities.testPlants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class PlantDetailViewModelTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = RuleChain.outerRule(hiltRule)
                .around(instantTaskExecutorRule)
                .around(coroutineRule)

    @Inject
    lateinit var plantRepository: PlantRepository

    @Inject
    lateinit var gardenPlantRepository: GardenPlantingRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val saveStateHandle: SavedStateHandle = SavedStateHandle().apply {
            this.set("PlantId", testPlant.plantId)
        }
        //plantRepository = PlantRepository(appDatabase.plantDao())
        //gardenPlantRepository = GardenPlantingRepository(appDatabase.gardenPlantingDao())
        viewModel = PlantDetailViewModel(saveStateHandle, plantRepository, gardenPlantRepository)
    }

    @After
    fun tearDown() { appDatabase.close() }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @kotlin.jvm.Throws(InterruptedException::class)
    fun testDefaultValue() = coroutineRule.runBlockingTest {
        Assert.assertFalse(getValue(viewModel.isPlanted))
    }
}