
package ca.uqac.stories.utils
import ca.uqac.stories.presentation.HighPriority
import ca.uqac.stories.presentation.StandardPriority
import ca.uqac.stories.presentation.StoryVM
import kotlinx.coroutines.flow.flow

private val storiesList: MutableList<StoryVM> = mutableListOf(
    StoryVM(id = 1,
        "Inscription",
        description = "En tant qu'utilisateur, \nje veux créer un compte.",
        done = false,
        priority = StandardPriority,
        time = 10,
        category = "Vitale"
    ),

    StoryVM(id = 2,
        "Consulter le solde",
        description = "En tant que client, " +
                "\nje veux voir mon solde.",
        done = true,
        priority = HighPriority,
        time = 2,
        category = "Vitale"
    ),
    StoryVM(id = 3,
        "Notifications",
        description = "En tant qu'abonné, \nje veux recevoir des notifications",
        done = true,
        priority = StandardPriority,
        time = 3,
        category = "Equipement"
    ),
    StoryVM(id = 4,
        "Recherche d’articles",
        description = "En tant qu'utilisateur, \nje veux voir des articles",
        done = true,
        priority = HighPriority,
        time = 7,
        category = "Equipment"
    )
)

fun getStories() = flow {
    emit(storiesList)
}

fun findStory(storyId: Int) : StoryVM? {
    return storiesList.find { it.id == storyId }
}

fun addOrUpdateStory(story: StoryVM) {
    val existingStory = storiesList.find { it.id == story.id }

    existingStory?.let {
        storiesList.remove(it)
    }

    storiesList.add(story)
}

fun deleteStoryFromList(story: StoryVM) {
    storiesList.remove(story)
}