package ca.uqac.stories.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

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

    val tabTitles = listOf("Aujourd'hui", "Mes routines")

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val searchQuery = remember { mutableStateOf("") }
    val filteredStories = storiesViewModel.stories.value.filter { story ->
        story.title.contains(searchQuery.value, ignoreCase = true)
    }

    val storiesByDate = filteredStories.groupBy { it.date }
    val expandedDates = remember { mutableStateMapOf<String, Boolean>().apply {
        storiesByDate.keys.forEach { date -> this[date] = true }
    }}

    // Update expandedDates whenever the searchQuery changes
    LaunchedEffect(searchQuery.value) {
        storiesByDate.keys.forEach { date ->
            expandedDates[date] = filteredStories.any { story -> story.date == date }
        }
    }

    // State to keep track of the selected tab
    val selectedTab = remember { mutableIntStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab.value,
                modifier = Modifier
                    .background(Color.White),
                indicator = {}, // on ne l'utilise pas ici
                divider = {} // enlève la ligne noire du bas
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab.value == index

                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab.value = index },
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color(0xFFB3D9FF) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }

            // Search input field
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Chercher une routine") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Icone de recherche"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            when (selectedTab.value) {
                0 -> TodayStoriesContent(navController, storiesViewModel, snackbarHostState, scope, searchQuery)
                1 -> AllStoriesContent(navController, storiesViewModel, snackbarHostState, scope, searchQuery, expandedDates, ::formatDateToFrench)
            }
        }
    }
}

@Composable
fun TodayStoriesContent(
    navController: NavController,
    storiesViewModel: ListStoriesViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    searchQuery: State<String>
) {
    val todayStories = storiesViewModel.stories.value.filter { story ->
        story.title.contains(searchQuery.value, ignoreCase = true) && story.date == getTodayDate()
    }

    LazyColumn(
        modifier = Modifier
            .background(Color.White) // Fond blanc pour la colonne entière
            .padding(horizontal = 16.dp), // Padding horizontal pour la colonne
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(todayStories) { story ->
            StoryCard(
                title = story.title,
                priority = story.priority,
                onEditClick = {
                    navController.navigate(Screen.AddEditStoryScreen.route + "?storyId=${story.id}")
                },
                onDeleteClick = {
                    storiesViewModel.onEvent(StoryEvent.Delete(story))
                    scope.launch {
                        snackbarHostState.showSnackbar("La routine a été supprimée avec succès")
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AllStoriesContent(
    navController: NavController,
    storiesViewModel: ListStoriesViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    searchQuery: State<String>,
    expandedDates: SnapshotStateMap<String, Boolean>,
    formatDateToFrench: (String) -> String
) {
    val filteredStories = storiesViewModel.stories.value.filter { story ->
        story.title.contains(searchQuery.value, ignoreCase = true)
    }

    val storiesByDate = filteredStories.groupBy { it.date }

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
                                    "Réduire routines"
                                else
                                    "Ouvrir routines",
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
                                        snackbarHostState.showSnackbar("La routine a été supprimée avec succès")
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

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return "$year-$month-$day"
}
