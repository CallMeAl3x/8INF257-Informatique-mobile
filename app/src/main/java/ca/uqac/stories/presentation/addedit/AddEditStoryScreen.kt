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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ca.uqac.stories.navigation.Screen
import ca.uqac.stories.presentation.HighPriority
import ca.uqac.stories.presentation.LowPriority
import ca.uqac.stories.presentation.StandardPriority
import java.time.LocalDate
import java.time.LocalTime
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
    val priorityCategories = listOf("Peu important", "Important", "Très important")
    var expanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    val priorityText = when (story.priority) {
        is HighPriority -> "Très important"
        is StandardPriority -> "Important"
        is LowPriority -> "Peu important"
    }

    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

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
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Date:",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDatePicker = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select Date",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Heure:",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier.padding(start = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showTimePicker = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select Time",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (story.hour != null && story.minute != null) {
                            val localTime = LocalTime.of(story.hour, story.minute)
                            localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        } else {
                            "Select Time"
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

            if (showTimePicker) {
                Dialog(onDismissRequest = { showTimePicker = false }) {
                    val timePickerState = rememberTimePickerState(
                        initialHour = story.hour ?: 0,
                        initialMinute = story.minute ?: 0
                    )

                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimePicker(state = timePickerState)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { showTimePicker = false }) {
                                Text("Cancel")
                            }
                            TextButton(onClick = {
                                viewModel.onEvent(
                                    AddEditStoryEvent.EnteredHour(
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )
                                )
                                showTimePicker = false
                            }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                TextField(
                    value = priorityText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Importance") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Ouvrir le menu"
                        )
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(16.dp)
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    priorityCategories.forEach { category -> 
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                val priorityValue = when (category) {
                                    "Très important" -> HighPriority
                                    "Important" -> StandardPriority
                                    "Peu important" -> LowPriority
                                    else -> {
                                        throw IllegalArgumentException("Invalid priority category: $category")
                                    }
                                }
                                viewModel.onEvent(AddEditStoryEvent.EnteredPriority(priorityValue))
                                categoryExpanded = false
                            }
                        )
                    }
                }
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
