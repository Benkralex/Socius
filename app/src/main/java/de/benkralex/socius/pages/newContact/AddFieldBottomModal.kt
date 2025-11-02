package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFieldBottomModal(
    modifier: Modifier = Modifier,
    viewModel: NewContactViewModel,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            viewModel.showAddFieldBottomModal = false
        }
    ) {
        AnimatedVisibility (!viewModel.showPrefixTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showPrefixTextField = true
                    },
                text = stringResource(R.string.name_prefix),
            )
        }
        AnimatedVisibility (!viewModel.showMiddleNameTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showMiddleNameTextField = true
                    },
                text = stringResource(R.string.name_middle_name),
            )
        }
        AnimatedVisibility (!viewModel.showSuffixTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showSuffixTextField = true
                    },
                text = stringResource(R.string.name_suffix),
            )
        }
        AnimatedVisibility (!viewModel.showNicknameTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showNicknameTextField = true
                    },
                text = stringResource(R.string.name_nickname),
            )
        }
        AnimatedVisibility (!viewModel.showJobTitleTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showJobTitleTextField = true
                    },
                text = stringResource(R.string.job_title),
            )
        }
        AnimatedVisibility (!viewModel.showDepartmentTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showDepartmentTextField = true
                    },
                text = stringResource(R.string.department),
            )
        }
        AnimatedVisibility (!viewModel.showOrganizationTextField) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.showAddFieldBottomModal = false
                        viewModel.showOrganizationTextField = true
                    },
                text = stringResource(R.string.organization),
            )
        }
    }
}