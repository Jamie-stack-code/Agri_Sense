package com.example.agri_sense.data.repository

import com.example.agri_sense.utils.SocketManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiExpertRepository @Inject constructor(
    private val socketManager: SocketManager
) {

    private val apiKey = com.example.agri_sense.BuildConfig.GEMINI_API_KEY 

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun getAdvice(question: String, isEnglish: Boolean, userName: String = "Farmer"): String {
        var aiResponse = ""
        var status = "PENDING"

        if (apiKey.isEmpty() || apiKey == "YOUR_GEMINI_API_KEY" || apiKey == "NO_KEY") {
            aiResponse = if (isEnglish) {
                "Based on latest agricultural intelligence: To treat Fall Armyworm on your maize, apply neem oil mixed with water directly into the plant funnel."
            } else {
                "Malinga ndi chidziwitso cha ulimi: Pofuna kuthetsa mbozwa mu chimanga, tsirani mankhwala a neem oil m'mbuwu."
            }
            status = "REPLIED" // Mock AI successfully replied
        } else {
            try {
                val prompt = if (isEnglish) {
                    "You are an expert agronomist in Malawi. Answer this farmer's question professionally and concisely: $question"
                } else {
                    "You are an expert agronomist in Malawi. Translate your expertise and answer this farmer's question in Chichewa: $question"
                }
                val response = generativeModel.generateContent(prompt)
                aiResponse = response.text ?: ""
                status = if (aiResponse.isNotBlank()) "REPLIED" else "PENDING"
            } catch (e: Exception) {
                e.printStackTrace()
                aiResponse = ""
                status = "PENDING"
            }
        }

        // --- HYBRID BROADCAST TO COMMAND PORTALS ---
        val payload = mapOf(
            "id" to UUID.randomUUID().toString(),
            "content" to question,
            "authorName" to userName,
            "aiResponse" to aiResponse,
            "status" to status,
            "timestamp" to System.currentTimeMillis()
        )
        socketManager.emit("NEW_FARMER_QUESTION", payload)

        return if (aiResponse.isBlank()) {
            if (isEnglish) "I am currently analyzing your farm data. An expert will respond shortly."
            else "Ndikupitiliza kuyeza chidziwitso chanu. Katswiri adzakuyankhani posachedwa."
        } else {
            aiResponse
        }
    }

    suspend fun getSoilRecommendation(soilType: String, pH: Float, nitrogen: String, phosphorus: String, potassium: String, isEnglish: Boolean): String {
        try {
            val prompt = if (isEnglish) {
                "As an expert agronomist in Malawi, provide a specific crop recommendation for a farmer with $soilType soil. " +
                "Properties: pH $pH, Nitrogen $nitrogen, Phosphorus $phosphorus, Potassium $potassium. " +
                "Suggest 2-3 suitable crops and a brief fertilizer advice. Keep it concise."
            } else {
                "As an expert agronomist in Malawi, provide a specific crop recommendation in Chichewa for a farmer with $soilType soil. " +
                "Properties: pH $pH, Nitrogen $nitrogen, Phosphorus $phosphorus, Potassium $potassium. " +
                "Suggest 2-3 suitable crops and a brief fertilizer advice in Chichewa. Keep it concise."
            }
            val response = generativeModel.generateContent(prompt)
            return response.text ?: "Could not generate recommendation."
        } catch (e: Exception) {
            e.printStackTrace()
            return "Recommendation pending expert review."
        }
    }
}
