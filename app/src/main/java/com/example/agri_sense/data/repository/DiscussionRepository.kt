package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.DiscussionDao
import com.example.agri_sense.data.models.Discussion
import com.example.agri_sense.utils.SocketManager
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscussionRepository @Inject constructor(
    private val discussionDao: DiscussionDao,
    private val socketManager: SocketManager
) {

    val allDiscussions: Flow<List<Discussion>> = discussionDao.getAll()
    val answeredDiscussions: Flow<List<Discussion>> = discussionDao.getAnswered()

    init {
        socketManager.connect()
        
        // --- REAL-TIME NEURAL LISTENER ---
        // Listen for expert replies in real-time
        socketManager.on("NEW_EXPERT_REPLY") { args ->
            val data = args[0] as? JSONObject
            data?.let { json ->
                val questionId = json.optString("questionId")
                val reply = json.optString("reply")
                
                // Update local database instantly
                // In a real app, you would use a coroutine scope to update the DAO
                println("📢 Real-Time Expert Reply Received for $questionId: $reply")
            }
        }

        // Listen for new global advisories
        socketManager.on("NEW_ADVISORY_PUBLISHED") { args ->
            println("📢 National Advisory Received: Broadcasting to UI")
        }
    }

    fun search(query: String): Flow<List<Discussion>> = discussionDao.search(query)

    suspend fun postQuestion(question: String, authorName: String, authorDistrict: String, authorCrop: String = "", imageUrl: String = "") {
        val newId = UUID.randomUUID().toString()
        val discussion = Discussion(
            id = newId,
            authorName = authorName,
            authorDistrict = authorDistrict,
            authorCrop = authorCrop,
            question = question,
            imageUrl = imageUrl,
            expertAnswer = "",
            likes = 0,
            replies = 0,
            tags = "",
            isAnswered = false,
            isUserPost = true,
            postedAt = System.currentTimeMillis()
        )
        
        // 1. Save Locally
        discussionDao.insert(discussion)

        // 2. Broadcast to Ecosystem (Expert and Admin Portals see this instantly)
        val payload = mapOf(
            "id" to newId,
            "content" to question,
            "authorName" to authorName,
            "imageUrl" to imageUrl,
            "farmerId" to "local-user-id" 
        )
        socketManager.emit("NEW_FARMER_QUESTION", payload)
    }

    suspend fun likeDiscussion(id: String) = discussionDao.incrementLikes(id)
    suspend fun addComment(id: String, comment: String) = discussionDao.addComment(id, comment)

    suspend fun seedIfEmpty() {
        if (discussionDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        discussionDao.insertAll(listOf(
            Discussion(
                id = "d1",
                authorName = "James Phiri",
                authorDistrict = "Kasungu",
                authorCrop = "Maize",
                question = "My maize leaves are turning yellow from the bottom up. Soil test shows low nitrogen. What fertilizer should I apply at V6?",
                expertAnswer = "Apply CAN at 50 kg/ha as a top-dress. Ensure soil moisture before applying to prevent burning.",
                likes = 42,
                replies = 7,
                tags = "maize,fertilizer,nitrogen",
                isAnswered = true,
                isUserPost = false,
                postedAt = now - 86400000L
            )
        ))
    }
}
