package ca.uqac.stories.presentation.addedit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uqac.stories.navigation.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStoryScreen(
    navController: NavController,
    viewModel: AddEditStoryViewModel
) {
    val story = viewModel.story.value
    val isNewStory = viewModel.isNewStory.value

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val categories = listOf("Aventure", "Science-fiction", "Horreur", "Comédie", "Drame")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditStoryEvent.SaveStory)
                    navController.navigate(Screen.StoriesListScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (isNewStory) Icons.Filled.AddCircle else Icons.Default.Edit,
                    contentDescription = "Save story",
                    tint = Color.White
                )
            }
        }
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color(0xFFEAF3FF))
                .padding(contentPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = if (isNewStory) "Ajouter" else "Modifier",
                    modifier = Modifier.weight(1f).padding(end = 42.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                )
            }

            OutlinedTextField(
                value = story.title,
                label = { Text("Titre") },
                onValueChange = { viewModel.onEvent(AddEditStoryEvent.EnteredTitle(it)) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(8.dp))
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = story.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Ouvrir le menu"
                        )
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(16.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                viewModel.onEvent(AddEditStoryEvent.EnteredCategory(category))
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = story.description,
                label = { Text("Description") },
                onValueChange = { viewModel.onEvent(AddEditStoryEvent.EnteredDescription(it)) },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Date:", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black), modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDatePicker = true }
                ) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Select Date", tint = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (story.date.isNotEmpty()) {
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val localDate = LocalDate.parse(story.date, formatter)
                            localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                        } else {
                            "Select Date"
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                    )
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis?.let {
                                val calendar = Calendar.getInstance().apply { timeInMillis = it; add(Calendar.DAY_OF_MONTH, 1) }
                                android.icu.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
                            }
                            viewModel.onEvent(AddEditStoryEvent.EnteredDate(selectedDate ?: ""))
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
                ) { DatePicker(state = datePickerState) }
            }

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fait", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black), modifier = Modifier.weight(1f))
                Checkbox(checked = story.done, onCheckedChange = { viewModel.onEvent(AddEditStoryEvent.StoryDone) })
            }

            BackHandler { navController.navigateUp() }
        }
    }
}
