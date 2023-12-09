package com.github.motoshige021.sunflowercopyapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.github.motoshige021.sunflowercopyapp.adapter.PlantAdapter
import com.github.motoshige021.sunflowercopyapp.databinding.FragmentPlantListBinding
import com.github.motoshige021.sunflowercopyapp.viewmodel.PlantListViewModel
import com.github.motoshige021.sunflowercopyapp.R
import com.github.motoshige021.sunflowercopyapp.utilties.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantListFragment : Fragment() {
    private val viewModel : PlantListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlantListBinding.inflate(inflater, container, false)
        context ?: return binding.root // ?: エルビス演算子 getContext()がNULLならrootを返して処理を終える

        val adapter = PlantAdapter()
        binding.plantList.adapter = adapter

        subscribeUI(adapter)

        //setHasOptionsMenu(true) 非推奨
        //setMenuVisibility(true) 効果なし
        // https://atsum.in/android/menu-provider/
/*
        >>>>> MainActivtyではなく HomeViewPagerFragmentのxmlにAppBarが定義されている <<<
        HomeViewPagerFragmentで、ActivityのsetSupportActionBar(toolbar)の引数に
        HomeViewPagerFragmentのtoolbarを設定してコールしており、Activityが
        HomeViewPagerFragmentのtoolbarのメニューを認識している
*/

        parentFragment?.activity?.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_plant_list, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.filter_zone -> {
                            updateData()
                            true
                        }
                        else -> true
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    /* 非推奨
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_plant_list, menu)
    }
    */
    /* 非推奨
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            } else -> super.onContextItemSelected(item)
        }
    }
    */

    private fun subscribeUI(adapter: PlantAdapter) {
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }
    }

    private fun updateData() {
        Log.d(TAG, "updateData() called")
        with (viewModel) { -> viewModel
            if (viewModel.isFiltered()) {
                viewModel.clearGrowZoneNumber()
            } else {
                viewModel.setGrowZoneNumber(9)
            }
        }
    }
}