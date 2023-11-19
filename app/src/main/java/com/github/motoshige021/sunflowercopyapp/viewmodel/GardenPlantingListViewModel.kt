package com.github.motoshige021.sunflowercopyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.motoshige021.sunflowercopyapp.data.GardenPlantingRepository
import com.github.motoshige021.sunflowercopyapp.data.PlantAndGardenPlantings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GardenPlantingListViewModel @Inject  internal constructor(
    gardenPlantingRepository: GardenPlantingRepository
): ViewModel() {
    val plantAndGardenPlantings : LiveData<List<PlantAndGardenPlantings>>
       = gardenPlantingRepository.getPlantedGardens().asLiveData()
}