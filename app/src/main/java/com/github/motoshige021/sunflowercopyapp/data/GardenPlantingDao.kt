package com.github.motoshige021.sunflowercopyapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenPlantingDao {
    @Query("SELECT * FROM garden_plantings")
    fun getGarden_plantings(): Flow<List<GardenPlanting>>

    @Query("SELECT EXISTS(SELECT 1 FROM garden_plantings WHERE plant_id = :platId LIMIT 1)")
    fun isPlanted(platId: String) : Flow<Boolean>

    // 複数テーブル間ではTransaciton
    @Transaction
    @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings)")
    fun getPlantedGardens(): Flow<List<PlantAndGardenPlantings>>

    // SelectのみのGardenFragmentのテストにInsertが必要なので先に作成する
    @Insert
    suspend fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    @Delete
    suspend fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}