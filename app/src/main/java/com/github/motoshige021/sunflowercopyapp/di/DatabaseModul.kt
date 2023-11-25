package com.github.motoshige021.sunflowercopyapp.di

import android.content.Context
import com.github.motoshige021.sunflowercopyapp.data.*
import com.github.motoshige021.sunflowercopyapp.utilties.testGardenPlanting
import com.github.motoshige021.sunflowercopyapp.utilties.testGardenPlantingId
import com.github.motoshige021.sunflowercopyapp.utilties.testPlants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModul {
    var isTest: Boolean = false

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    fun providesPlantDao(appDataBase: AppDatabase): PlantDao {
        return appDataBase.plantDao()
    }


    @Provides
    fun provideGardenPlantingDao(appDataBase: AppDatabase) : GardenPlantingDao {
        if (isTest) { testDataInsert(appDataBase) }
        return appDataBase.gardenPlantingDao()
    }


    private fun testDataInsert(appDataBase: AppDatabase) = runBlocking{
        appDataBase.plantDao().insertAll(testPlants)
        testGardenPlantingId = appDataBase.gardenPlantingDao().insertGardenPlanting(testGardenPlanting)
    }
}