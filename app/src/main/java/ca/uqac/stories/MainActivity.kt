package ca.uqac.stories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import ca.uqac.stories.data.source.StoriesDao
import ca.uqac.stories.data.source.StoriesDatabase
import ca.uqac.stories.presentation.list.ListStoriesScreen
import ca.uqac.stories.presentation.list.ListStoriesViewModel
import ca.uqac.stories.presentation.addedit.AddEditStoryScreen
import ca.uqac.stories.presentation.addedit.AddEditStoryViewModel
import ca.uqac.stories.ui.theme.StoriesTheme
import ca.uqac.stories.navigation.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            StoriesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val db = StoriesDatabase.getInstance(applicationContext) // Utilisez le singleton
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.StoriesListScreen.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = Screen.StoriesListScreen.route) {
                                val storiesViewModel = viewModel<ListStoriesViewModel> {
                                    ListStoriesViewModel(db.dao)
                                }
                                ListStoriesScreen(navController, storiesViewModel)
                            }
                            composable(
                                route = Screen.AddEditStoryScreen.route + "?storyId={storyId}",
                                arguments = listOf(
                                    navArgument(name = "storyId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) { navBackStackEntry ->
                                val storyId = navBackStackEntry.arguments?.getInt("storyId") ?: -1
                                val story = viewModel<AddEditStoryViewModel>() {
                                    AddEditStoryViewModel(db.dao, storyId)
                                }
                                AddEditStoryScreen(navController, story)
                            }
                        }
                    }
                }
            }
        }
    }
}