package ca.uqac.stories.presentation.addedit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.uqac.stories.presentation.StoryVM
import ca.uqac.stories.utils.addOrUpdateStory
import ca.uqac.stories.utils.findStory


class  AddEditStoryViewModel(storyId: Int = -1) : ViewModel() {
    private val _story = mutableStateOf(StoryVM())
    var story : State<StoryVM> = _story

    init {
        _story.value = findStory(storyId) ?: StoryVM()
    }

    fun onEvent(event: AddEditStoryEvent) {
        when (event) {
            is AddEditStoryEvent.EnteredTitle -> {
                _story.value = _story.value.copy(title = event.title)
            }

            is AddEditStoryEvent.EnteredDescription -> {
                _story.value = _story.value.copy(description = event.description)
            }

            AddEditStoryEvent.StoryDone ->
                _story.value = _story.value.copy(done = !_story.value.done)

            AddEditStoryEvent.SaveStory -> {
                addOrUpdateStory(story.value)
            }
        }
    }
}