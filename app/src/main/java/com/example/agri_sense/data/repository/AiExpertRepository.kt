package com.example.agri_sense.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiExpertRepository @Inject constructor() {

    // This key is now securely pulled from local.properties via BuildConfig
    private val apiKey = com.example.agri_sense.BuildConfig.GEMINI_API_KEY 

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun getAdvice(question: String, isEnglish: Boolean): String {
        if (apiKey.isEmpty() || apiKey == "YOUR_GEMINI_API_KEY" || apiKey == "NO_KEY") {
            // Mocked premium AI response since no key is provided
            kotlinx.coroutines.delay(1500)
            return if (isEnglish) {
                "Based on latest agricultural intelligence: To treat Fall Armyworm on your maize, apply neem oil mixed with water directly into the plant funnel. If the infestation is severe, use a targeted insecticide like Emamectin benzoate early in the morning."
            } else {
                "Malinga ndi chidziwitso cha ulimi: Pofuna kuthetsa mbozwa mu chimanga, tsirani mankhwala a neem oil m'mbuwu. Ngati vuto lili lalikulu, gwiritsani ntchito mankhwala monga Emamectin benzoate m'mawa kwambiri."
            }
        }

        return try {
            val prompt = if (isEnglish) {
                "You are an expert agronomist in Malawi. Answer this farmer's question professionally and concisely: $question"
            } else {
                "You are an expert agronomist in Malawi. Translate your expertise and answer this farmer's question in Chichewa: $question"
            }
            val response = generativeModel.generateContent(prompt)
            response.text ?: "I am currently analyzing your farm data. Please try asking again later."
        } catch (e: Exception) {
            e.printStackTrace()
            if (isEnglish) "Connection to the Agri-Sense AI network failed." else "Kulumikizana ndi Agri-Sense AI kwalephera."
        }
    }
}
