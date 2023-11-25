package com.github.motoshige021.sunflowercopyapp.utilties

import com.github.motoshige021.sunflowercopyapp.data.GardenPlanting
import com.github.motoshige021.sunflowercopyapp.data.Plant
import java.util.Calendar

val testPlants = arrayListOf(
    Plant("1", "Apple",
        "An apple is a sweet, edible fruit produced by an apple tree (Malus pumila). Apple trees are cultivated worldwide, and are the most widely grown species in the genus Malus. The tree originated in Central Asia, where its wild ancestor, Malus sieversii, is still found today. Apples have been grown for thousands of years in Asia and Europe, and were brought to North America by European colonists. Apples have religious and mythological significance in many cultures, including Norse, Greek and European Christian traditions.<br><br>Apple trees are large if grown from seed. Generally apple cultivars are propagated by grafting onto rootstocks, which control the size of the resulting tree. There are more than 7,500 known cultivars of apples, resulting in a range of desired characteristics. Different cultivars are bred for various tastes and uses, including cooking, eating raw and cider production. Trees and fruit are prone to a number of fungal, bacterial and pest problems, which can be controlled by a number of organic and non-organic means. In 2010, the fruit's genome was sequenced as part of research on disease control and selective breeding in apple production.<br><br>Worldwide production of apples in 2014 was 84.6 million tonnes, with China accounting for 48% of the total.<br><br>(From <a href=\"https://en.wikipedia.org/wiki/Apple\">Wikipedia</a>)",
        3, 30,
          "https://upload.wikimedia.org/wikipedia/commons/5/55/Apple_orchard_in_Tasmania.jpg"),
    Plant("2", "B", "Description B", 1),
    Plant("3", "C", "Description C", 2)
)

val testPlant = testPlants[0]

val testCalender: Calendar = Calendar.getInstance().apply {
    this.set(Calendar.YEAR, 2023)
    this.set(Calendar.MONTH, Calendar.NOVEMBER)
    this.set(Calendar.DAY_OF_MONTH, 26)
}

val testGardenPlanting = GardenPlanting(testPlant.plantId, testCalender, testCalender)

var testGardenPlantingId: Long = -999

/*
>>>> androidTest の TestUtilsの実装、画像URLをassetのjsonファイルから追加
>>> androidTestのGardenPlantingDaoTestを参考にテストデータinsertする
>>> insertするタイミング di.DatabaseModuleのDao作成時
 */