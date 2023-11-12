package com.github.motoshige021.sunflowercopyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.motoshige021.sunflowercopyapp.adapter.MY_GARDEN_PAGE_INDEX
import com.github.motoshige021.sunflowercopyapp.adapter.PLANT_LIST_PAGE_INDEX
import com.github.motoshige021.sunflowercopyapp.adapter.SunflowerPagerAdapter
import com.github.motoshige021.sunflowercopyapp.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeViewPagerFragment :Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout =binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // ViewPager2 を使用してタブ付きスワイプビューを作成する
        // https://developer.android.com/guide/navigation/navigation-swipe-view-2?hl=ja
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabText(position)
        }.attach()

        return binding.root
    }

    private fun getTabIcon(position: Int) : Int {
        return when(position) {
            MY_GARDEN_PAGE_INDEX -> R.drawable.garden_tab_selector
            PLANT_LIST_PAGE_INDEX -> R.drawable.plant_list_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabText(position: Int): String? {
        return when(position) {
            MY_GARDEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            else -> null
        }
    }
}