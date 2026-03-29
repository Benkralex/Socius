package de.benkralex.socius.ui.components.displayContact

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.model.Contact

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfilePictureWidget(
    modifier: Modifier = Modifier,
    contact: Contact,
    size: Dp,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val bitmap: Bitmap? = contact.profilePicture.bitmap

    val rotation by animateFloatAsState(
        targetValue = if (isSelected) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
    )

    Box (
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable (
                enabled = onClick != null
            ) {
                if (onClick != null) onClick()
            },
    ) {
        if (rotation <= 90f) {
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
        } else {
            Icon (
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .graphicsLayer { rotationY = 180f }
                    .clip(MaterialShapes.Cookie9Sided.toShape())
                    .background(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    .padding(5.dp),
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}