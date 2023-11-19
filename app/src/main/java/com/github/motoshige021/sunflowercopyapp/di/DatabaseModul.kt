package com.github.motoshige021.sunflowercopyapp.di

import android.content.Context
import com.github.motoshige021.sunflowercopyapp.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModul {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providesPlantDao(appDataBase: AppDatabase): PlantDao {
        return appDataBase.plantDao()
    }

    @Provides
    fun provideGardenPlantingDao(appDataBase: AppDatabase) : GardenPlantingDao {
        return appDataBase.gardenPlantingDao()
    }
}