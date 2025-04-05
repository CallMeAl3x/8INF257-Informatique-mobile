package ca.uqac.stories.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uqac.stories.presentation.components.StoryCard
import ca.uqac.stories.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ListStoriesScreen(navController: NavController, storiesViewModel: ListStoriesViewModel) {
    fun formatDateToFrench(dateString: String): String {
        return try {
            val parts = dateString.split("-")
            if (parts.size == 3) {
                val year = parts[0]
                val month = parts[1].toInt()
                val day = parts[2].toInt()

                val monthName = when (month) {
                    1 -> "Janvier"
                    2 -> "Février"
                    3 -> "Mars"
                    4 -> "Avril"
                    5 -> "Mai"
                    6 -> "Juin"
                    7 -> "Juillet"
                    8 -> "Août"
                    9 -> "Septembre"
                    10 -> "Octobre"
                    11 -> "Novembre"
                    12 -> "Décembre"
                    else -> "Mois inconnu"
                }

                "$day $monthName $year"
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val storiesByDate = storiesViewModel.stories.value.groupBy { it.date }
    val expandedDates = remember { mutableStateMapOf<String, Boolean>().apply {
        storiesByDate.keys.forEach { date -> this[date] = true }
    }}

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mes routines",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp)
                )
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddEditStoryScreen.route) },
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.hsl(215f, 0.37f, 0.66f, 1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add a story",
                        tint = Color.Black
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .background(Color.White) // Fond blanc pour la colonne entière
                    .padding(horizontal = 16.dp), // Padding horizontal pour la colonne
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                storiesByDate.forEach { (date, stories) ->
                    item(key = "header_$date") {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFFB3D9FF), RoundedCornerShape(12.dp)) // Fond bleu pour chaque section de date
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = formatDateToFrench(date),
                                    modifier = Modifier.weight(1f),
                                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                )
                                IconButton(
                                    onClick = { expandedDates[date] = !(expandedDates[date] ?: true) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = if (expandedDates[date] == true)
                                            Icons.Filled.KeyboardArrowUp
                                        else
                                            Icons.Filled.ArrowDropDown,
                                        contentDescription = if (expandedDates[date] == true)
                                            "Collapse stories"
                                        else
                                            "Expand stories",
                                        tint = Color.Black
                                    )
                                }
                            }

                            if (expandedDates[date] == true) {
                                stories.forEach { story ->
                                    StoryCard(
                                        title = story.title,
                                        priority = story.priority,
                                        onEditClick = {
                                            navController.navigate(Screen.AddEditStoryScreen.route + "?storyId=${story.id}")
                                        },
                                        onDeleteClick = {
                                            storiesViewModel.onEvent(StoryEvent.Delete(story))
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Deleted story successfully")
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    if (date != storiesByDate.keys.last()) {
                        item(key = "spacer_after_$date") {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
