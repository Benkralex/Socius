package de.benkralex.socius.ui.components.contactsList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.ui.pages.ContactListPageViewModel
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpToLetterDialog(
    viewModel: ContactListPageViewModel,
    onDismiss: () -> Unit = {viewModel.showJumpToLetterDialog = false},
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(CornerSize(30.dp))
            )
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        onDismissRequest = onDismiss,
    ) {
        val keys = viewModel.grouped.minus("?")
        val rowLength = sqrt(keys.size.toDouble()).toInt()
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            for (i in 0 until keys.size step rowLength) {
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    for (j in i until i + rowLength) {
                        if (j < keys.size) {
                            val letter = viewModel.grouped.keys.elementAt(j)
                            Box (
                                modifier = Modifier
                                    .padding(horizontal = 7.dp, vertical = 10.dp)
                                    .size(52.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(CornerSize(8.dp))
                                    ),
                            ) {
                                if (letter == "starred") {
                                    IconButton(
                                        modifier = Modifier
                                            .align(Alignment.Center),
                                        onClick = {
                                            viewModel.scrollToLetter(letter)
                                            onDismiss()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = stringResource(R.string.starred),
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        )
                                    }
                                } else {
                                    Text(
                                        text = letter.toString(),
                                        modifier = Modifier
                                            .padding(end = 3.dp)
                                            .clickable {
                                                viewModel.scrollToLetter(letter)
                                                onDismiss()
                                            }
                                            .align(Alignment.Center),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.displayMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}