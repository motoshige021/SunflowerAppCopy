package com.github.motoshige021.sunflowercopyapp.compose.plantdetail

import androidx.appcompat.widget.Toolbar
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import java.util.concurrent.Flow

private val HeaderTransitionOffset = 190.dp

enum class ToolbarState {
    HIDDEN,
    SHOWN;
}

val ToolbarState.isShown
    get() = this == ToolbarState.SHOWN

data class PlantDetailScroller(
    val scrollState: ScrollState,
    val namePosition: Float
) {
    val toolbarTransitionState = MutableTransitionState(ToolbarState.HIDDEN)

    fun getToolbarState(density: Density) : ToolbarState {
        return if (namePosition > 1f &&
                scrollState.value > (namePosition - getTransitionOffset(density))) {
            toolbarTransitionState.targetState = ToolbarState.SHOWN
            ToolbarState.SHOWN
        } else {
            toolbarTransitionState.targetState = ToolbarState.HIDDEN
            ToolbarState.HIDDEN
        }
    }

    private fun getTransitionOffset(density: Density): Float = with(density) {
        HeaderTransitionOffset.toPx()
    }
}

