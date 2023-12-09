package com.github.motoshige021.sunflowercopyapp.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepository @Inject constructor(
    private val plantDao: PlantDao
){
    fun getPlants() = plantDao.getPlants()

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)


    companion object {
        @Volatile private var instance: PlantRepository? = null

        fun getInstance(plantDao: PlantDao) =
            // エルビス演算子 instanceがnullならsynchronized()を実行、nullでないならinstance
            instance ?: synchronized(this) {
                // エルビス演算子 instanceがnullならPlantRepository()生成、nullでないならinstance
                instance ?: PlantRepository(plantDao).also { instance = it }
            }
    }
}