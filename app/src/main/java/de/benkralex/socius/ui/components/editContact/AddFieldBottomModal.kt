package de.benkralex.socius.ui.components.editContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFieldBottomModal(
    modifier: Modifier = Modifier,
    viewModel: NewContactViewModel,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun hideSheet(action: () -> Unit = {}) {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                viewModel.showAddFieldBottomModal = false
                action()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = {
            viewModel.showAddFieldBottomModal = false
        }
    ) {
        AnimatedVisibility (!viewModel.structuredNameState.showPrefixTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.structuredNameState.showPrefixTextField = true }
                    },
                text = stringResource(R.string.name_prefix),
            )
        }
        AnimatedVisibility (!viewModel.structuredNameState.showMiddleNameTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.structuredNameState.showMiddleNameTextField = true }
                    },
                text = stringResource(R.string.name_middle_name),
            )
        }
        AnimatedVisibility (!viewModel.structuredNameState.showSuffixTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.structuredNameState.showSuffixTextField = true }
                    },
                text = stringResource(R.string.name_suffix),
            )
        }
        AnimatedVisibility (!viewModel.structuredNameState.showNicknameTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.structuredNameState.showNicknameTextField = true }
                    },
                text = stringResource(R.string.name_nickname),
            )
        }
        AnimatedVisibility (!viewModel.workInformationState.showJobTitleTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.workInformationState.showJobTitleTextField = true }
                    },
                text = stringResource(R.string.job_title),
            )
        }
        AnimatedVisibility (!viewModel.workInformationState.showDepartmentTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.workInformationState.showDepartmentTextField = true }
                    },
                text = stringResource(R.string.department),
            )
        }
        AnimatedVisibility (!viewModel.workInformationState.showOrganizationTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        hideSheet { viewModel.workInformationState.showOrganizationTextField = true }
                    },
                text = stringResource(R.string.organization),
            )
        }
    }
}