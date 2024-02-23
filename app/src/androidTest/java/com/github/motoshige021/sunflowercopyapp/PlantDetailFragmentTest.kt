package com.github.motoshige021.sunflowercopyapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.test.*
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.motoshige021.sunflowercopyapp.utilities.chooser
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidRule

@RunWith(AndroidJUnit4::class)
class PlantDetailFragmentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun jumpToPlantDetalFragment() {
        composeTestRule.activityRule.scenario.onActivity {
            val bundle = Bundle().apply { putString("plantId", "malus-pumila") }
            Navigation.findNavController(it, R.id.nav_host).navigate(R.id.plant_detail_fragment, bundle)
        }
    }

    @Test
    fun screen_lunchear() {
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
    }

    @Test
    fun testShareTextIntent() {
        Intents.init()

        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Share").assertIsDisplayed().performClick()
        Intents.intended(
            chooser (
                allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasType("text/plain"),
                    hasExtra(Intent.EXTRA_TEXT,
                            "check out the Apple plant in Android Sunflower app"
                             )
                )
             )
        )
        Intents.release()
        // dismiss the share dialogue
        InstrumentationRegistry.getInstrumentation()
            .uiAutomation
            .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }
}