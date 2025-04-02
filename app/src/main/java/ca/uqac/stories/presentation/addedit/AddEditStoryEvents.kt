package ca.uqac.stories.presentation.addedit

import ca.uqac.stories.presentation.PriorityType
import java.time.LocalTime
import org.osmdroid.util.GeoPoint

sealed interface AddEditStoryEvent {
    data class EnteredTitle(val title: String): AddEditStoryEvent
    data class EnteredDescription(val description: String): AddEditStoryEvent
    data class EnteredDate(val date: String): AddEditStoryEvent
    data class EnteredCategory(val category: String): AddEditStoryEvent
    data class EnteredPriority(val priority: PriorityType) : AddEditStoryEvent
    data class EnteredHour(val hour: Int, val minute: Int) : AddEditStoryEvent
    data class EnteredLocation(val location: GeoPoint) : AddEditStoryEvent
    data object StoryDone: AddEditStoryEvent
    data object SaveStory: AddEditStoryEvent
}
