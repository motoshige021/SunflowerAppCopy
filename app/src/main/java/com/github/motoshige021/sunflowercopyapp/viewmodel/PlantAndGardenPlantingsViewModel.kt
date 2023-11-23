package com.github.motoshige021.sunflowercopyapp.viewmodel

import android.text.format.DateFormat
import com.github.motoshige021.sunflowercopyapp.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.*

class PlantAndGardenPlantingsViewModel(plantings: PlantAndGardenPlantings) {
    private val plant = checkNotNull(plantings.plant)
    private val gardenPlantings = plantings.gardenPlantings[0]

    val wateDateString = dateFormat.format(gardenPlantings.lastWateringDate.time)
    val waterInterval
    get() = plant.wateringInterval

    val imageUri
    get() = plant.imageUrl

    val plantName
    get() = plant.name

    val plantDateString: String = dateFormat.format(gardenPlantings.plantData.time)

    val plantId
    get() = plant.plantId

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy MMM d", Locale.JAPANESE)
    }
}