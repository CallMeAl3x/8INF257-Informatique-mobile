package ca.uqac.stories.presentation.addedit

sealed interface AddEditStoryEvent {
    data class EnteredTitle(val title: String): AddEditStoryEvent
    data class EnteredDescription(val description: String): AddEditStoryEvent
    data class EnteredDate(val date: String): AddEditStoryEvent
    data class EnteredCategory(val category: String): AddEditStoryEvent
    data object StoryDone: AddEditStoryEvent
    data object SaveStory: AddEditStoryEvent
}
