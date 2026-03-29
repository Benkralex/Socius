package de.benkralex.socius.ui.components.editContact

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.ui.components.displayContact.ProfilePictureWidget
import de.benkralex.socius.ui.pages.EditContactPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePicture(
    modifier: Modifier = Modifier,
    viewModel: EditContactPageViewModel,
) {
    val context = LocalContext.current
    var showMediaSelectionModal by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
        } catch (_: SecurityException) {
            // Best effort: falls nicht persistierbar, nutzen wir das Bild trotzdem fuer diese Session.
        }

        val bitmap = try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (_: Exception) {
            null
        }

        viewModel.profilePictureState.picture = bitmap
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        ProfilePictureWidget(
            contact = Contact(
                id = null,
                origin = ContactOrigin.TEMPORARY,
                profilePicture = ProfilePicture(viewModel.profilePictureState.picture),
            ),
            size = 150.dp,
            onClick = {
                showMediaSelectionModal = true
            }
        )

        if (showMediaSelectionModal) {
            ModalBottomSheet(
                onDismissRequest = {
                    showMediaSelectionModal = false
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMediaSelectionModal = false
                            imagePickerLauncher.launch(arrayOf("image/*"))
                        }
                        .padding(16.dp),
                    text = stringResource(R.string.select_image),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMediaSelectionModal = false
                            viewModel.profilePictureState.clearPicture()
                        }
                        .padding(16.dp),
                    text = stringResource(R.string.remove_profile_picture),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMediaSelectionModal = false
                        }
                        .padding(16.dp),
                    text = stringResource(R.string.cancel),
                )
            }
        }
    }
}