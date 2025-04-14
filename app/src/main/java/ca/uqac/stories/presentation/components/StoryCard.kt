package ca.uqac.stories.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import ca.uqac.stories.presentation.HighPriority
import ca.uqac.stories.presentation.LowPriority
import ca.uqac.stories.presentation.StandardPriority
import ca.uqac.stories.presentation.PriorityType

@Composable
fun StoryCard(
    title: String,
    priority: PriorityType,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    // Déterminer le libellé et la couleur de la priorité
    val priorityLabel = when (priority) {
        is HighPriority -> "Très important"
        is StandardPriority -> "Important"
        is LowPriority -> "Peu important"
    }

    val priorityColor = when (priority) {
        is HighPriority -> Color.Red
        is StandardPriority -> Color.Yellow
        is LowPriority -> Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onEditClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Important",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Green
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .background(color = Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = priorityLabel,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor
                        ),
                    )
                }

            }
        }

        // Right Section: Actions (Edit and Delete)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Supprimer",
                    tint = Color.Black
                )
            }
        }
    }
}
