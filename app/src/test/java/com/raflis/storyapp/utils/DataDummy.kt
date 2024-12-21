package com.raflis.storyapp.utils

import com.raflis.storyapp.data.remote.entity.Story
import java.util.UUID
import kotlin.random.Random

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val storyList: MutableList<Story> = arrayListOf()

        for (i in 0..100) {
            val story = Story(
                id = UUID.randomUUID().toString(),
                createdAt = "2024-11-16T12:34:56Z",
                description = "Story $i",
                name = "Story by $i",
                lon = Random.nextDouble(100.0, 140.0),
                lat = Random.nextDouble(-10.0, 10.0),
                photoUrl = "https://picsum.photos/200/300?random=$i"
            )

            storyList.add(story)
        }
        return storyList
    }
}