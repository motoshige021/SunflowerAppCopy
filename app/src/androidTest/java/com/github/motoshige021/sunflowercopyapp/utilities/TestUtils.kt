package com.github.motoshige021.sunflowercopyapp.utilities

import android.content.Intent
import com.github.motoshige021.sunflowercopyapp.data.GardenPlanting
import com.github.motoshige021.sunflowercopyapp.data.Plant
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import java.util.Calendar
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import org.hamcrest.Matcher

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

fun chooser(matchers: Matcher<Intent>): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(`is`(Intent.EXTRA_INTENT), matchers)
)