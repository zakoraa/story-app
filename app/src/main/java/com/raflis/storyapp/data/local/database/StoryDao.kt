package com.raflis.storyapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raflis.storyapp.data.remote.entity.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createStory(story : List<Story>)

    @Query("SELECT * FROM stories")
    fun getAllStories() : PagingSource<Int, Story>

    @Query("DELETE FROM stories")
    suspend fun deleteAllStory()
}