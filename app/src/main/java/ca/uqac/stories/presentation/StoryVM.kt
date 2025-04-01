package ca.uqac.stories.presentation

import androidx.compose.ui.graphics.Color
import ca.uqac.stories.domain.model.Story
import ca.uqac.stories.ui.theme.Purple40
import ca.uqac.stories.ui.theme.Purple80
import ca.uqac.stories.ui.theme.PurpleGrey40
import ca.uqac.stories.ui.theme.PurpleGrey80
import kotlin.random.Random

data class StoryVM(
    val id: Int = Random.nextInt(),
    val date: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val done: Boolean = false,
    val priority: PriorityType = StandardPriority,
    val hour: Int = 0,
    val minute: Int = 0
) {
    companion object {
        fun fromEntity(entity: Story): StoryVM {
            return StoryVM(
                id = entity.id!!,
                title = entity.title,
                description = entity.description,
                done = entity.done,
                date = entity.date,
                category = entity.category,
                hour = entity.hour,
                minute = entity.minute,
                priority = when (entity.priority) {
                    2 -> HighPriority
                    1 -> StandardPriority
                    else -> LowPriority
                },
            )
        }
    }

    fun toEntity(): Story {
        return Story(
            id = this.id,
            title = this.title,
            description = this.description,
            done = this.done,
            hour = this.hour,
            minute = this.minute,
            priority = when (this.priority) {
                is HighPriority -> 2
                is StandardPriority -> 1
                is LowPriority -> 0
            },
            date = this.date,
            category = this.category
        )
    }
}

sealed class PriorityType(
    val backgroundColor: Color,
    val foregroundColor: Color
)

data object HighPriority : PriorityType(
    Purple40, PurpleGrey80
)

data object StandardPriority : PriorityType(
    Purple80, PurpleGrey40
)

data object LowPriority : PriorityType(
    Color.Gray, Color.White
)

