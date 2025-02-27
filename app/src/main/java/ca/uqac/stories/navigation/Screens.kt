package ca.uqac.stories.navigation

sealed class Screen(val route: String) {
    data object StoriesListScreen : Screen(route = "stories_list_screen")
    data object AddEditStoryScreen : Screen(route = "add_edit_stories_screen")
}