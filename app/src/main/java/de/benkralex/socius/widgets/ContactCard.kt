package de.benkralex.socius.widgets

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    name: String,
    profilePicture: Bitmap? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0, 0, 0, 1),
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            if (profilePicture != null) {
                Image(
                    bitmap = profilePicture.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialShapes.Cookie6Sided.toShape())
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialShapes.Cookie6Sided.toShape())
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                        .padding(5.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name)
            }
        }
    }
}