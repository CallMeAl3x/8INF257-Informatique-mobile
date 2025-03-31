package ca.uqac.stories.presentation.addedit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uqac.stories.data.source.StoriesDao
import ca.uqac.stories.presentation.StoryVM
import ca.uqac.stories.presentation.HighPriority
import ca.uqac.stories.presentation.LowPriority
import ca.uqac.stories.presentation.StandardPriority
import ca.uqac.stories.utils.addOrUpdateStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddEditStoryViewModel(val dao: StoriesDao, storyId: Int = -1) : ViewModel() {
    private val _story = mutableStateOf(StoryVM())
    var story: State<StoryVM> = _story
    private val _isNewStory = mutableStateOf(storyId == -1)
    val isNewStory: State<Boolean> = _isNewStory

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (storyId == -1) {
                _story.value = StoryVM(id = generateUniqueRandomId())
            } else {
                val storyEntity = dao.getStory(storyId)
                if (storyEntity != null) {
                    _story.value = StoryVM.fromEntity(storyEntity)
                    Log.d("AddEditStoryViewModel", "Story loaded: ${_story.value}")
                } else {
                    Log.d("AddEditStoryViewModel", "Story not found for ID: $storyId")
                    _story.value = StoryVM(id = storyId)
                }
            }
        }
    }

    private suspend fun generateUniqueRandomId(): Int {
        var id: Int
        do {
            id = Random.nextInt(1, Int.MAX_VALUE)
        } while (dao.getStory(id) != null)
        return id
    }

    fun onEvent(event: AddEditStoryEvent) {
        when (event) {
            is AddEditStoryEvent.EnteredTitle -> {
                _story.value = _story.value.copy(title = event.title)
            }

            is AddEditStoryEvent.EnteredDescription -> {
                _story.value = _story.value.copy(description = event.description)
            }

            is AddEditStoryEvent.EnteredDate -> {
                _story.value = _story.value.copy(date = event.date)
            }

            is AddEditStoryEvent.EnteredCategory -> {
                _story.value = _story.value.copy(category = event.category)
            }

            is AddEditStoryEvent.EnteredPriority -> {
                _story.value = _story.value.copy(priority = event.priority)
            }

            AddEditStoryEvent.StoryDone -> {
                _story.value = _story.value.copy(done = !_story.value.done)
            }

            AddEditStoryEvent.SaveStory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addOrUpdateStory(dao, _story.value)
                }
            }
        }
    }
}
