package ca.uqac.stories.presentation.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uqac.stories.presentation.StoryVM
import ca.uqac.stories.utils.deleteStoryFromList
import ca.uqac.stories.utils.getStories
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListStoriesViewModel() : ViewModel() {
    private val _stories: MutableState<List<StoryVM>> = mutableStateOf(emptyList())
    var stories: State<List<StoryVM>> = _stories

    init {
//        _stories.value = loadStories()
        loadStories()
    }

    private fun loadStories() {
        getStories().onEach { stories ->
             _stories.value = stories
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: StoryEvent) {
        when(event) {
            is StoryEvent.Delete -> {
                deleteStory(event.story)
            }
        }
    }

    private fun deleteStory(story: StoryVM) {
        _stories.value = _stories.value.filter { it != story }
        deleteStoryFromList(story)
    }
}