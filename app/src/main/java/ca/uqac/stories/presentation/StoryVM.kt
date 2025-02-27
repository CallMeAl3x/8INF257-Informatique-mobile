package ca.uqac.stories.presentation

import androidx.compose.ui.graphics.Color
import ca.uqac.stories.ui.theme.Purple40
import ca.uqac.stories.ui.theme.Purple80
import ca.uqac.stories.ui.theme.PurpleGrey40
import ca.uqac.stories.ui.theme.PurpleGrey80
import kotlin.random.Random

data class StoryVM (
    val id: Int = Random.nextInt(),
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val time: Int = 0,
    val done: Boolean = false,
    val priority: PriorityType = StandardPriority
)

sealed class PriorityType(
    val backgroundColor: Color,
    val foregroundColor: Color
)

sealed class CategoryType(
    val devoir: String
)

data object HighPriority:PriorityType(
    Purple40, PurpleGrey80)

data object StandardPriority:PriorityType(
    Purple80, PurpleGrey40)
