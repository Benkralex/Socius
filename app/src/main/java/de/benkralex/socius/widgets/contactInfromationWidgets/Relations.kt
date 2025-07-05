package de.benkralex.socius.widgets.contactInfromationWidgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.benkralex.socius.backend.Relation

@Composable
fun RelationsWidget(
    relations: List<Relation>
) {
    if (relations.isEmpty()) {
        return
    }
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            relations.forEach { relation ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Relation",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = relation.name,
                        )
                        Text(
                            text = translateType(relation.type, relation.label),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (relations.indexOf(relation) != (relations.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}