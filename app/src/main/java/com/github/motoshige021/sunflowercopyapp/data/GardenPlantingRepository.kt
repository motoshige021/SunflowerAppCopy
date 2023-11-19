package com.github.motoshige021.sunflowercopyapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GardenPlantingRepository @Inject constructor(
    private val gardenPlantingDao : GardenPlantingDao
) {
    fun getPlantedGardens() : Flow<List<PlantAndGardenPlantings>> {
        return gardenPlantingDao.getPlantedGardens()
    }
}