package ca.uqac.stories.presentation.addedit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@Composable
fun AddEditStoryScreen(
    navController: NavController,
    viewModel: AddEditStoryViewModel
) {
    val story = viewModel.story.value
    val isNewStory = viewModel.isNewStory.value

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
                    imageVector = if(isNewStory) Icons.Filled.AddCircle else Icons.Default.Edit,
                    contentDescription = "Save story",
                    tint = Color.White
                )
            }
        }
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color(0xFFEAF3FF)) // Light blue background
                .padding(contentPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                // Back Button
                IconButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                // Title Text
                Text(
                    text = if (isNewStory) "Ajouter" else "Modifier",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 42.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                )
            }

            // Title Input Field
            OutlinedTextField(
                value = story.title,
                label = { Text("Titre") },
                onValueChange = {
                    viewModel.onEvent(AddEditStoryEvent.EnteredTitle(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp)), // Rounded corners for the text field
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Input Field
            OutlinedTextField(
                value = story.description,
                label = { Text("Description") },
                onValueChange = {
                    viewModel.onEvent(AddEditStoryEvent.EnteredDescription(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp)), // Rounded corners for the text field
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Done Checkbox Row
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fait",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = story.done,
                    onCheckedChange = {
                        viewModel.onEvent(AddEditStoryEvent.StoryDone)
                    }
                )
            }

            // Handle system back button behavior
            BackHandler {
                navController.navigateUp()
            }
        }
    }
}

