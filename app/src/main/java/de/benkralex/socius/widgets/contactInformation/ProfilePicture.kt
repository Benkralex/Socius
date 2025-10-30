package de.benkralex.socius.widgets.contactInformation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
        if (bitmap != null) {
            Image (
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(MaterialShapes.Cookie6Sided.toShape())
            )
        } else {
            Icon (
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
        Icon (
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            modifier = Modifier
                .size(size / 3)
                .dropShadow(
                    shape = MaterialShapes.Cookie9Sided.toShape(),
                    shadow = Shadow(
                        radius = 5.dp,
                        spread = 5.dp,
                    )
                )
                .clip(MaterialShapes.Cookie9Sided.toShape())
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
                .padding(size / 30)
                .align(Alignment.BottomEnd),
            tint = MaterialTheme.colorScheme.background,
        )
    }
}