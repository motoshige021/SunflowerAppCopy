package com.github.motoshige021.sunflowercopyapp.compose.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextSnackbarContainer(
    snackbarText: String,
    showSnackbar: Boolean,
    onDismissSnackbar: () -> Unit,
    modifier: Modifier = Modifier,
    // rememberは初回に { ... }の処理が実行され、二回目以降は実行されず初回の結果を返す
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contnet : @Composable () -> Unit
) {
    // BoxはRowやColumnと異なり任意の位置に配置するコンテナ
    Box(modifier) {
        contnet() // 引数の Composable 関数

        val onDismissState by rememberUpdatedState(onDismissSnackbar)
        // LaunchedEffect(): コンポジションに入場した時、または再コンポジションした時に引数のサスペンド関数を実行する
        LaunchedEffect(showSnackbar, snackbarText) {
            if (showSnackbar) {
                try {
                    // Snackbarは画面下部に表示されるメッセージ
                    snackbarHostState.showSnackbar(
                        message = snackbarText,
                        duration = SnackbarDuration.Short
                    )
                } finally {
                    onDismissState()
                }
            }
        }

        MaterialTheme(shapes = Shapes()) {
            // Snackbarは下部の中央に配置
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .systemBarsPadding()
                    .padding(all = 8.dp)
            ) {
                Snackbar(it)
            }
        }
    }
}