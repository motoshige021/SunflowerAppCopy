package com.github.motoshige021.sunflowercopyapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "garden_plantings")
data class GardenPlanting(
    @ColumnInfo(name = "plant_id") val plantId: String,
    @ColumnInfo(name = "plant_data") val plantData: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "last_watering_date")
    val lastWateringDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var gardenPlantId: Long = 0
}
