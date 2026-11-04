package com.puciec.ghiblikmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.puciec.ghiblikmp.ui.App

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as GhibliApplication).container
        setContent {
            // NÃO REMOVA — sem testTagsAsResourceId=true, Modifier.testTag(...) não
            // vira resource-id na árvore de acessibilidade e o Maestro (seletor `id:`)
            // não encontra nenhum elemento.
            App(
                container = container,
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { testTagsAsResourceId = true },
            )
        }
    }
}
