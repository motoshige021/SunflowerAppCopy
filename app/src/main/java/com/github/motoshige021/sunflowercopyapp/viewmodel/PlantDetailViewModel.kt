package com.github.motoshige021.sunflowercopyapp.viewmodel

import androidx.lifecycle.*
import com.github.motoshige021.sunflowercopyapp.data.GardenPlantingRepository
import com.github.motoshige021.sunflowercopyapp.data.PlantRepository
import com.github.motoshige021.sunflowercopyapp.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository
) : ViewModel() {
    // PlantDetailFragmentへのnavigationの引数の名前が "plantId"、引数の型がString型
    // 引数の名前 "plantId"は、PLANT_ID_SAVED_STATE_KEY="plantId"と一致。
    // SavedStateHandleは、Fragment遷移の引数を自動設定している
    val plantId: String = saveStateHandle.get<String>(PLANT_ID_SAVED_STATE_KEY)!!

    val isPlanted = gardenPlantingRepository.isPlanted(plantId).asLiveData()
    val plant = plantRepository.getPlant(plantId).asLiveData()

    private val _showSnackbar = MutableLiveData(false)
    val showSnackbar : LiveData<Boolean>
        get() = _showSnackbar

    fun addPlantToGarden() {
        viewModelScope.launch {
            // createGardenPlanting()が、suspned修飾より CoroutineScope(= ViewModelScope)で実行
            gardenPlantingRepository.createGardenPlanting(plantId)
            _showSnackbar.value = true
        }
    }

    fun dismissSnacker() {
        _showSnackbar.value = false
    }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }
}