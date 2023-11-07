package com.github.motoshige021.sunflowercopyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.motoshige021.sunflowercopyapp.adapter.SunflowerPagerAdapter
import com.github.motoshige021.sunflowercopyapp.databinding.FragmentViewPagerBinding

class HomeViewPagerFragment :Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        // val tabLayout =binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // tabと　ActionBarの設定を記述する

        return binding.root
    }
}