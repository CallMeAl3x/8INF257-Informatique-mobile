package ca.uqac.stories.presentation.list

import ca.uqac.stories.presentation.StoryVM

sealed class StoryEvent {
    data class Delete(val story: StoryVM) : StoryEvent()
}

