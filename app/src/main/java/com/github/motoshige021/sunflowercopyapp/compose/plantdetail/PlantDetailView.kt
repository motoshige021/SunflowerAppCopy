package com.github.motoshige021.sunflowercopyapp.compose.plantdetail

import android.text.method.LinkMovementMethod
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.motoshige021.sunflowercopyapp.compose.utils.TextSnackbarContainer
import com.github.motoshige021.sunflowercopyapp.viewmodel.PlantDetailViewModel
import com.github.motoshige021.sunflowercopyapp.R
import com.github.motoshige021.sunflowercopyapp.compose.Dimens
import com.github.motoshige021.sunflowercopyapp.compose.visible
import com.github.motoshige021.sunflowercopyapp.data.Plant
import com.github.motoshige021.sunflowercopyapp.databinding.ItemPlantDescriptionBinding
import com.github.motoshige021.sunflowercopyapp.view.MaskedCardView
import okhttp3.internal.immutableListOf

// クラスのコンストラクタの引数にval修飾すると、クラス変数定義を兼ねる
data class PlantDetailCallbacks (
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: () -> Unit
)

@Composable
fun PlantDetailScreen(
    plantDetailViewModel: PlantDetailViewModel,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val plant = plantDetailViewModel.plant.observeAsState().value
    val isPlanted = plantDetailViewModel.isPlanted.observeAsState().value
    val showSnackbar = plantDetailViewModel.showSnackbar.observeAsState().value
    if (plant != null && isPlanted != null && showSnackbar != null) {
        // Dark modeを考慮したテーマはSurfaceで囲む必要がる(文字が黒くなり見えなくなる）
        Surface {
            TextSnackbarContainer(
                snackbarText = stringResource(R.string.added_plant_to_garden),
                showSnackbar = showSnackbar,
                onDismissSnackbar = { plantDetailViewModel.dismissSnacker() }
            ) {
                PlantDetails(
                    plant,
                    isPlanted,
                    PlantDetailCallbacks(
                        onBackClick = onBackClick,
                        onFabClick = {
                            plantDetailViewModel.addPlantToGarden()
                        },
                        onShareClick = onShareClick
                    )
                )
            }
        }
    }
}

@Composable
fun PlantDetails(
    plant: Plant,
    isPlanted: Boolean,
    callbacks: PlantDetailCallbacks,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var plantScroller by remember {
        mutableStateOf(PlantDetailScroller(scrollState, Float.MIN_VALUE))
    }
    val transitState = remember(plantScroller) { plantScroller.toolbarTransitionState }
    val toolbarState = plantScroller.getToolbarState(LocalDensity.current)
    val transition = updateTransition(transitState, label="")
    val toolbarAlpha = transition.animateFloat(
        // springは、バネのように端に到達すると反発して、端に位置するアニメーション
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 0f else 1f
    }
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
    }
    var toolbarHeightPx = with(LocalDensity.current) {
        Dimens.PlantDetailAppBarHeight.roundToPx().toFloat()
    }
    val toolbarOffsetHeighPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffSet = toolbarOffsetHeighPx.value + delta
                toolbarOffsetHeighPx.value = newOffSet.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }
    // Box 任意の位置に配置する/ 要素を別の要素上の任意の位置に配置する·
    Box(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .nestedScroll(nestedScrollConnection)
    ) {
        PlantDetailContent(
            scrollState = scrollState,
            toolbarState = toolbarState,
            plant = plant,
            isPlanted = isPlanted,
            imageHeight = with(LocalDensity.current) {
                Dimens.PlantDetailAppBarHeight + toolbarOffsetHeighPx.value.toDp()
            },
            onNamePosition = { newNamePosition ->
                if (plantScroller.namePosition == Float.MIN_VALUE) {
                    plantScroller = plantScroller.copy(namePosition = newNamePosition)
                }
            },
            onFabClick = callbacks.onFabClick,
            contentAlpha = { contentAlpha.value }
        )

        PlantToolbar(
            toolbarState = toolbarState,
            plantName = plant.name,
            callbacks = callbacks,
            toolbarAlpha = { toolbarAlpha.value },
            contentAlpha = { contentAlpha.value }
        )
    }
}

@Composable
private fun PlantDetailContent(
    scrollState: ScrollState,
    toolbarState: ToolbarState,
    plant: Plant,
    isPlanted: Boolean,
    imageHeight: Dp,
    onNamePosition: (Float) -> Unit,
    onFabClick: () -> Unit,
    contentAlpha: () -> Float
) {
    // ス直方向に並べる
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        ConstraintLayout {
            // createRefs():ConstraintLayoutの子要素(今から作成する)となるComposeを
            // 参照するオブジェクトを作成
            val (image, fab, info) = this.createRefs()

            PlantImage(
                imageUrl = plant.imageUrl,
                imageHeight = imageHeight,
                modifier = Modifier // createRefs()のオブジェクトをModifierのconstraintAs()に指定
                    .constrainAs(image) {
                        // constraintAs()のラムダ式で、ConstraintLayoutの制約を設定
                        top.linkTo(parent.top)
                    }
                    .alpha(contentAlpha())
            )

            if (!isPlanted) {
                val fabEndMargin = Dimens.PaddingSmall

                PlantFab(
                    onFabClick = onFabClick,
                    modifier = Modifier
                        .constrainAs(fab) {
                            centerAround(image.bottom)
                            // bottom.linkTo(parent.bottom) debug motoshige
                            absoluteRight.linkTo(
                                parent.absoluteRight,
                                margin = fabEndMargin
                            )
                        }
                        .alpha(contentAlpha())
                        .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 24.dp,
                                                bottomStart = 24.dp, bottomEnd = 0.dp))
                )
            }

            PlantInformation(
                name = plant.name,
                wateringInterval = plant.wateringInterval,
                description = plant.description,
                onNamePosition = { onNamePosition(it) },
                toolbarState = toolbarState,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(image.bottom)
                }
            )
        }
    }
}

@Composable
private fun PlantImage(
    imageUrl: String,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
    placeHolderColor: Color = MaterialTheme.colors.onSurface.copy(0.2f)
){
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = imageUrl)
            .crossfade(true)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    )

    if (painter.state is AsyncImagePainter.State.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(placeHolderColor)
        )
    }
}

@Composable
private fun PlantFab(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val addPlantConentDescripiton = stringResource(R.string.add_plant)
    FloatingActionButton(
        onClick = onFabClick,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.semantics {
            this.contentDescription = addPlantConentDescripiton
        }
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun PlantToolbar(
    toolbarState: ToolbarState,
    plantName: String,
    callbacks: PlantDetailCallbacks,
    toolbarAlpha: ()-> Float,
    contentAlpha : () -> Float
) {
    if (toolbarState.isShown) {
        PlantDetailToolbar(
            plantName = plantName,
            onBackClick = callbacks.onBackClick,
            onShareClick = callbacks.onShareClick,
            modifier = Modifier.alpha(toolbarAlpha())
        )
    } else {
        PlantHeaderAction(
            onBackClick = callbacks.onBackClick,
            onShareClick = callbacks.onShareClick,
            modifier = Modifier.alpha(contentAlpha())
        )
    }
}

@Composable
private fun PlantDetailToolbar(
    plantName: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dark modeを考慮したテーマはSurfaceで囲む必要がる(文字が黒くなり見えなくなる）
    Surface {
        TopAppBar(
            modifier = modifier.statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            IconButton(
                onBackClick,
                Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.ally_back)
                )
            }
            Text(
                text = plantName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            val shareContentDescription = stringResource(R.string.menu_item_share_plant)
            IconButton(
                onClick = onShareClick,
                Modifier
                    .align(Alignment.CenterVertically)
                    // Semantics in parent due to https://issuetracker.google.com/184825850
                    .semantics { contentDescription = shareContentDescription }
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun PlantHeaderAction(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 縦に並べる Row
    Row(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = Dimens.ToolbarIconPadding),
        // Arrangement.SpaceBetween - 子要素が等間隔 horizontalより垂直方向の子要素が等間隔
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var iconModifier = Modifier
            .sizeIn(
                maxWidth = Dimens.ToolbarIconSize,
                maxHeight = Dimens.ToolbarIconSize
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = Dimens.ToolbarIconPadding)
                // Modifier#then() 引数に与えられたModifierを後ろに連結したModifierを返す
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.ally_back)
            )
        }
        val shareContentDescription = stringResource(R.string.share_text_plant)
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .padding(Dimens.ToolbarIconPadding)
                .then(iconModifier)
                .semantics {
                    contentDescription = shareContentDescription
                }
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class) // pluralStringResource()のビルドエラー対応
@Composable
private fun PlantInformation(
    name: String,
    wateringInterval: Int,
    description: String,
    onNamePosition: (Float) -> Unit,
    toolbarState: ToolbarState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(Dimens.PaddingSmall)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.CenterHorizontally)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
                .visible { toolbarState == ToolbarState.HIDDEN }
        )
        Text(
            text = stringResource(R.string.watering_need_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = Dimens.PaddingSmall)
                .align(Alignment.CenterHorizontally)
        )
        // CompositionLocalProviderの引数で指定した依存を下層のComposeに渡す(DIの代わり？）
        CompositionLocalProvider(LocalContentAlpha provides  ContentAlpha.medium) {
            Text(
                text = pluralStringResource(
                    R.plurals.watering_need_prefix,
                    wateringInterval,
                    wateringInterval
                ),
                modifier = Modifier
                    .padding(
                        start = Dimens.PaddingSmall,
                        end = Dimens.PaddingSmall,
                        bottom = Dimens.PaddingNormal
                    )
                    .align(Alignment.CenterHorizontally)
            )
        }
        PlantDescription(description)
    }
}

@Composable
private fun PlantDescription(descrption: String) {
    AndroidViewBinding(ItemPlantDescriptionBinding::inflate) {
        this.plantDescription.text = HtmlCompat.fromHtml(
            descrption,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        this.plantDescription.movementMethod = LinkMovementMethod.getInstance()
        this.plantDescription.linksClickable = true
    }
}

