package com.github.motoshige021.sunflowercopyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.fragment.app.Fragment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.github.motoshige021.sunflowercopyapp.adapter.PLANT_LIST_PAGE_INDEX
import com.github.motoshige021.sunflowercopyapp.databinding.FragmentGardenBinding

class GardenFragment : Fragment() {
    private lateinit var binding: FragmentGardenBinding

    /* >>> Do Implement   <<< */
    /* private val viewModel : GardenPlantingListViewModel */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater, container, false)

        /* >>> Do Implement <<< */
        /* GardenPlantingAdapter clas and SubscribeUi() */

        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }
        subScribeUi(/* adapter ,*/ binding)

        return binding.root
    }

    private fun subScribeUi(/* adapter: GardenPlantingAdapter,*/
                            binding: FragmentGardenBinding) {
        binding.hasPlanting = false // dummy
        //binding.gardenList.isGone = !binding.hasPlanting
        //binding.emptyLayout.isGone = binding.hasPlanting
        // isGone問題は、BindingAdapter.ktの@BindingAdapter("isGone")を参照
    }

    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = PLANT_LIST_PAGE_INDEX
    }
}