package com.github.motoshige021.sunflowercopyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.motoshige021.sunflowercopyapp.R
import com.github.motoshige021.sunflowercopyapp.HomeViewPagerFragmentDirections
import com.github.motoshige021.sunflowercopyapp.data.PlantAndGardenPlantings
import com.github.motoshige021.sunflowercopyapp.databinding.ListItemGardenPlantingBinding
import com.github.motoshige021.sunflowercopyapp.viewmodel.PlantAndGardenPlantingsViewModel

class GardenPlantingAdapter : ListAdapter<PlantAndGardenPlantings,
        GardenPlantingAdapter.ViewHolder>( GardenPlantDiffCallback() ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_garden_planting,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemGardenPlantingBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                binding.viewModel?.plantId?.let {plantId ->
                    navigateToPlant(plantId, view)
                }
            }
        }

        private fun navigateToPlant(plantId: String, view: View) {
            val direction = HomeViewPagerFragmentDirections
                .actionViewPageFragmentToPlantDetailFragment(plantId)
            view.findNavController().navigate(direction)
        }

        fun bind(planting: PlantAndGardenPlantings) {
            with(binding) {
                binding.viewModel =  PlantAndGardenPlantingsViewModel(planting)
                // https://blog.ryskit.com/entry/2019/01/06/194414
                binding.executePendingBindings()
            }
        }
    }
}

private class GardenPlantDiffCallback : DiffUtil.ItemCallback<PlantAndGardenPlantings>() {
    override fun areContentsTheSame(
        oldItem: PlantAndGardenPlantings,
        newItem: PlantAndGardenPlantings
    ): Boolean {
        return oldItem.plant.plantId == newItem.plant.plantId
    }

    override fun areItemsTheSame(
        oldItem: PlantAndGardenPlantings,
        newItem: PlantAndGardenPlantings
    ): Boolean {
        return oldItem.plant == newItem.plant
    }
}