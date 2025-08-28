package de.benkralex.socius.pages

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.benkralex.socius.R

@Composable
fun AllowPermissionsPage(
    modifier: Modifier = Modifier,
    onAllPermissionsAllowed: @Composable () -> Unit = {},
) {
    Scaffold (
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val context = LocalContext.current
                var hasPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            permission.READ_CONTACTS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }
                var shouldShowRationale by remember {
                    mutableStateOf(
                        (context as? Activity)?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                permission.READ_CONTACTS
                            )
                        } ?: false
                    )
                }

                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    hasPermission = isGranted
                    if (!isGranted) {
                        shouldShowRationale = (context as? Activity)?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                permission.READ_CONTACTS
                            )
                        } ?: false
                    }
                }

                LaunchedEffect(Unit) {
                    if (!hasPermission) {
                        launcher.launch(permission.READ_CONTACTS)
                    }
                }

                if (hasPermission) {
                    onAllPermissionsAllowed()
                } else {
                    Text(
                        text = stringResource(R.string.permission_error_contacts_read),
                        textAlign = TextAlign.Center,
                    )
                    if (shouldShowRationale) {
                        Button(
                            onClick = { launcher.launch(permission.READ_CONTACTS) },
                        ) {
                            Text(
                                text = stringResource(R.string.permission_give),
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                intent.data = uri
                                context.startActivity(intent)
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.permission_open_settings),
                            )
                        }
                        Button(
                            onClick = {
                                hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    permission.READ_CONTACTS
                                ) == PackageManager.PERMISSION_GRANTED
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.permission_test),
                            )
                        }
                    }
                }
            }
        }
    }
}