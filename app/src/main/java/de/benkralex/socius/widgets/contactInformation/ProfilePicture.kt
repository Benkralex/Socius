package de.benkralex.socius.widgets.contactInformation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.Contact

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    contact: Contact,
    size: Dp,
    isSelected: Boolean = false,
) {
    var bitmap: Bitmap? = null
    if (size > 100.dp && contact.photoBitmap != null) {
        bitmap = contact.photoBitmap
    } else if (contact.thumbnailBitmap != null) {
        bitmap = contact.thumbnailBitmap
    }

    Box (
        modifier = modifier,
    ) {
        if (isSelected) {
            Icon (
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(MaterialShapes.Cookie6Sided.toShape())
                    .background(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    .padding(5.dp),
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        } else {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(MaterialShapes.Cookie6Sided.toShape())
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(MaterialShapes.Cookie6Sided.toShape())
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                        .padding(5.dp),
                    tint = MaterialTheme.colorScheme.background,
                )
            }
        }
        if (contact.isReadOnly()) {
            Icon (
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                modifier = Modifier
                    .size(size / 3)
                    .dropShadow(
                        shape = MaterialShapes.Cookie9Sided.toShape(),
                        shadow = Shadow(
                            radius = size / 20,
                            spread = size / 20,
                            color = MaterialTheme.colorScheme.surfaceDim,
                        )
                    )
                    .clip(MaterialShapes.Cookie9Sided.toShape())
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(size / 20)
                    .align(Alignment.BottomEnd),
                tint = MaterialTheme.colorScheme.background,
            )
        }
    }
}