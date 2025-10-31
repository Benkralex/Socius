package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.theme.DarkColorScheme

val newContactFormEnterTransition: EnterTransition = fadeIn(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
) + expandVertically(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
)
val newContactFormExitTransition: ExitTransition = fadeOut(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
) + shrinkVertically(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewContactPage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: NewContactViewModel = viewModel<NewContactViewModel>(),
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Neuer Kontakt")
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable(
                                onClick = onBackClick
                            )
                    )
                },
            )
        },
        floatingActionButton = {
            MediumFloatingActionButton(
                onClick = {
                    if (viewModel.isSaving) return@MediumFloatingActionButton
                    viewModel.saveContact()
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Speichern"
                )
            }
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EditStructuredName(viewModel = viewModel)
            AnimatedVisibility(
                visible = viewModel.showWorkInformation,
                enter = newContactFormEnterTransition,
                exit = newContactFormExitTransition,
            ) {
                EditWorkInformation(viewModel = viewModel)
            }
            Button(
                onClick = {
                    viewModel.showAddFieldBottomModal = true
                }
            ) {
                Text("Feld hinzufügen")
            }
            AnimatedVisibility(
                visible = viewModel.showAddFieldBottomModal,
                enter = newContactFormEnterTransition,
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                    ),
                ),
            ) {
                AddFieldBottomModal(viewModel = viewModel)
            }
        }
    }
}

@Preview
@Composable
private fun NewContactPagePreview() {
    MaterialTheme (
        colorScheme = DarkColorScheme,
    ) {
        NewContactPage()
    }
}