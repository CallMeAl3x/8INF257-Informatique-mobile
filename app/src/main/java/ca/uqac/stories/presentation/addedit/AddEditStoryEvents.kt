package ca.uqac.stories.presentation.addedit

sealed interface AddEditStoryEvent {
    data class EnteredTitle(val title: String): AddEditStoryEvent
    data class EnteredDescription(val description: String): AddEditStoryEvent
    data object StoryDone: AddEditStoryEvent
    data object SaveStory: AddEditStoryEvent
}