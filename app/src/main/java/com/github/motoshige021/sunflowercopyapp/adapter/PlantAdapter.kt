package com.github.motoshige021.sunflowercopyapp.adapter

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.motoshige021.sunflowercopyapp.HomeViewPagerFragmentDirections
import com.github.motoshige021.sunflowercopyapp.compose.plantlist.PlantListItemView
import com.github.motoshige021.sunflowercopyapp.data.Plant
import com.google.android.material.composethemeadapter.MdcTheme

class PlantAdapter : ListAdapter<Plant, RecyclerView.ViewHolder>(PlantDiffCallback()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(ComposeView(parent.context))
    }

    class PlantViewHolder(
        composeView: ComposeView
    ):RecyclerView.ViewHolder(composeView) {
        fun bind(plant: Plant) {
            (itemView as ComposeView).setContent {
                MdcTheme {
                    PlantListItemView(plant = plant) {
                        // 引数 (plant, onclickListener) 最後の引数onClickListenerをラムダ式で
                        // 設定する時、引数省略してラムダ式の実態を記述することができる
                        // (plant,onclickListener) -> (plantId, () {....})
                        // -> (plantId) { ... }
                        navigateToPlant(plant)
                    }
                }
            }
        }

        private fun navigateToPlant(plant: Plant) {
            val direction =
                HomeViewPagerFragmentDirections.actionViewPageFragmentToPlantDetailFragment(
                    plant.plantId
                )
            itemView.findNavController().navigate(direction)
        }
    }
}

private class PlantDiffCallback: DiffUtil.ItemCallback<Plant>() {
    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.plantId == newItem.plantId
    }

    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return  oldItem == newItem
    }
}