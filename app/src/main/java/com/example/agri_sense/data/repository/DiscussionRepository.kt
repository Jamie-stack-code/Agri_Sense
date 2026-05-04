package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.DiscussionDao
import com.example.agri_sense.data.models.Discussion
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscussionRepository @Inject constructor(private val discussionDao: DiscussionDao) {

    val allDiscussions: Flow<List<Discussion>> = discussionDao.getAll()
    val answeredDiscussions: Flow<List<Discussion>> = discussionDao.getAnswered()

    fun search(query: String): Flow<List<Discussion>> = discussionDao.search(query)

    suspend fun postQuestion(question: String, authorName: String, authorDistrict: String, authorCrop: String = "") {
        discussionDao.insert(
            Discussion(
                id = UUID.randomUUID().toString(),
                authorName = authorName,
                authorDistrict = authorDistrict,
                authorCrop = authorCrop,
                question = question,
                isUserPost = true,
                postedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun likeDiscussion(id: String) = discussionDao.incrementLikes(id)

    suspend fun seedIfEmpty() {
        if (discussionDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        discussionDao.insertAll(listOf(
            Discussion("d1", "James Phiri", "Kasungu", "Maize",
                "My maize leaves are turning yellow from the bottom up. Soil test shows low nitrogen. What fertilizer should I apply at V6?",
                "Apply CAN at 50 kg/ha as a top-dress. Ensure soil moisture before applying to prevent burning. If you see purple coloring, also suspect phosphorus — apply DAP at next planting.",
                42, 7, "maize,fertilizer,nitrogen", true, false, now - 86400000L),
            Discussion("d2", "Grace Banda", "Lilongwe", "Groundnuts",
                "How can I protect my groundnuts from Rosette disease? Last season I lost 60% of my yield.",
                "Groundnut Rosette is spread by aphids. Plant early, use resistant varieties (CG 7, Chalimbana), and spray Dimethoate 40EC at 0.5L/ha every 2 weeks from emergence.",
                67, 12, "groundnuts,disease,aphids,rosette", true, false, now - 259200000L),
            Discussion("d3", "John Mkandawire", "Zomba", "Tomatoes",
                "The price of tomatoes keeps dropping when I sell at Zomba Market. Is there a better time or place to sell?",
                "Prices peak July–September. Consider drying to powder (MK 4,000–6,000/kg vs MK 300 fresh). Contact NASFAM Zomba for market linkages and hotel buyers.",
                89, 18, "tomatoes,market,pricing,post-harvest", true, false, now - 432000000L),
            Discussion("d4", "Mary Nkhoma", "Mzimba", "Soybeans",
                "My soybeans are not nodulating properly. I did not apply Rhizobium inoculant. Does this affect yield significantly?",
                "Yes — you lose up to 60% of biological nitrogen without nodulation. Next planting apply Optimize inoculant at 0.75L per 50kg seed. Available from Farmers World at MK 4,500/bottle.",
                55, 9, "soybeans,nodulation,rhizobium,nitrogen", true, false, now - 691200000L),
            Discussion("d5", "Peter Nthara", "Salima", "Rice",
                "Is drip irrigation feasible for a 0.5 Ha rice plot near Lake Malawi? What is the setup cost?",
                "For 0.5Ha: setup MK 800,000–1,200,000. FAO Malawi offers subsidized kits. For Lake Malawi proximity, gravity-fed furrow irrigation is more economical at MK 50,000–150,000.",
                34, 6, "rice,irrigation,drip,lake malawi", true, false, now - 1036800000L),
            Discussion("d6", "Rose Chilunga", "Blantyre", "Maize",
                "What is the best intercropping combination with maize in the Southern Region?",
                "", 12, 0, "maize,intercropping,soil health", false, false, now - 3600000L)
        ))
    }
}
