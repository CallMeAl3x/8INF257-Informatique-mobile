package ca.uqac.stories.presentation.list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uqac.stories.data.source.StoriesDao
import ca.uqac.stories.presentation.StoryVM
import ca.uqac.stories.utils.deleteStoryFromList
import ca.uqac.stories.utils.logDatabaseState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ListStoriesViewModel(private val dao: StoriesDao) : ViewModel() {
    private val _stories: MutableState<List<StoryVM>> = mutableStateOf(emptyList())
    val stories: State<List<StoryVM>> = _stories
    private var job: Job? = null

    init {
        loadStories()
    }

    private fun loadStories() {
        job?.cancel()
        job = dao.getStories().onEach { storyEntities ->
            val stories = storyEntities.map { StoryVM.fromEntity(it) }
            _stories.value = stories
            Log.d("ListStoriesViewModel", "Loaded ${stories.size} stories from DB")

            viewModelScope.launch {
                logDatabaseState(dao)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: StoryEvent) {
        when (event) {
            is StoryEvent.Delete -> {
                deleteStory(event.story)
            }
        }
    }

    private fun deleteStory(story: StoryVM) {
        viewModelScope.launch {
            deleteStoryFromList(dao, story)
        }
    }
}
