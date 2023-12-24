package com.github.motoshige021.sunflowercopyapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.motoshige021.sunflowercopyapp.compose.plantdetail.PlantDetailScreen
import com.github.motoshige021.sunflowercopyapp.viewmodel.PlantDetailViewModel
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantDetailFragment : Fragment() {
    private val plantDetailViewModel: PlantDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        // setViewCompositionStrategy()-破棄するタイミングを指定
        // DisposeOnViewTreeLifecycleDestroyedはFragmentのライフサイクルに合わせて破棄する
        this.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        this.setContent {
            MdcTheme {
                PlantDetailScreen(
                    plantDetailViewModel,
                    onBackClick = {
                        findNavController().navigateUp()
                    },
                    onShareClick = {
                        createShareIntent()
                    }
                )
            }
        }
    }

    fun createShareIntent() {
        val shareText = plantDetailViewModel.plant.value.let { plant ->
            if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(shareText)
            .setType("text/lain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        startActivity(shareIntent)
    }
}