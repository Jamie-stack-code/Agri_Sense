package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agri_sense.data.models.Discussion
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscussionDao {
    @Query("SELECT * FROM discussions ORDER BY postedAt DESC")
    fun getAll(): Flow<List<Discussion>>

    @Query("SELECT * FROM discussions WHERE isAnswered = 1 ORDER BY postedAt DESC")
    fun getAnswered(): Flow<List<Discussion>>

    @Query("SELECT * FROM discussions WHERE isUserPost = 1 ORDER BY postedAt DESC")
    fun getUserPosts(): Flow<List<Discussion>>

    @Query("SELECT * FROM discussions WHERE question LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY postedAt DESC")
    fun search(query: String): Flow<List<Discussion>>

    @Query("SELECT COUNT(*) FROM discussions")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(discussion: Discussion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(discussions: List<Discussion>)

    @Query("UPDATE discussions SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLikes(id: String)
}
