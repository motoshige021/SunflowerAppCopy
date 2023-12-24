package com.github.motoshige021.sunflowercopyapp.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints

fun Modifier.visible(isVisible: () -> Boolean) = this.then(VisibleModifier(isVisible))

private data class VisibleModifier(
    private val isVisible: () -> Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeble = measurable.measure(constraints)
        return layout(placeble.width, placeble.height) {
            if (isVisible()) {
                placeble.place(0, 0)
            }
        }
    }
}