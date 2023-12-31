package com.github.motoshige021.sunflowercopyapp.utilities

import com.github.motoshige021.sunflowercopyapp.data.GardenPlanting
import com.github.motoshige021.sunflowercopyapp.data.Plant
import java.util.Calendar

val testPlants = arrayListOf(
    Plant("1", "Apple", "A red fruit", 1),
    Plant("2", "B", "Description B", 1),
    Plant("3", "C", "Description C", 1)
)

val testPlant = testPlants[0] // Apple

val testCalender: Calendar = Calendar.getInstance().apply {
    this.set(Calendar.YEAR, 1998)
    this.set(Calendar.MONTH, Calendar.SEPTEMBER)
    this.set(Calendar.DAY_OF_MONTH, 4)
}

val testGardenPlanting = GardenPlanting(testPlant.plantId, testCalender, testCalender)
