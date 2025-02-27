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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uqac.stories.presentation.components.StoryCard
import ca.uqac.stories.navigation.Screen
import ca.uqac.stories.presentation.HighPriority
import ca.uqac.stories.presentation.StandardPriority
import kotlinx.coroutines.launch

@Composable
fun ListStoriesScreen(navController: NavController, storiesViewModel: ListStoriesViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(true) } // State to track expanded/collapsed state
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
                    .background(Color(0xFFB3D9FF), RoundedCornerShape(12.dp)),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                // Header item for a given day with expand/collapse toggle
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Lundi 27 Février",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = if (isExpanded) "Collapse stories" else "Expand stories",
                                tint = Color.Black
                            )
                        }
                    }
                }

                // Only display the story cards if the section is expanded
                if (isExpanded) {
                    items(
                        items = storiesViewModel.stories.value,
                        key = { it.id }
                    ) { story ->
                        StoryCard(
                            title = story.title,
                            time = "${story.time}h",
                            priority = if (story.priority == HighPriority) HighPriority else StandardPriority,
                            category = story.category ?: "",
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
    }
}
