package com.github.motoshige021.sunflowercopyapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.github.motoshige021.sunflowercopyapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions

@HiltAndroidTest
class MainActivityTest {
    private val hitlRule = HiltAndroidRule(this)
    //private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule = RuleChain
        .outerRule(hitlRule) // outRule Before Afterの処理
        .around(activityTestRule)

    @Test
    fun clickAddPlanetOpensPlanetList() {
        // Add Planetボタンを押下 -- add_plantは Plantが追加されていない時に表示されるボタン
        // テスト前に、キャッシュを削除する
        Espresso.onView(ViewMatchers.withId(R.id.add_plant)).perform(ViewActions.click())
        // Plant List Fragmentが表示されるのを確認するテスト
        Espresso.onView(ViewMatchers.withId(R.id.plant_list))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}