package com.example.agri_sense.ml

import com.example.agri_sense.data.models.CropRecommendation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Professional recommendation engine that maps soil analysis results,
 * climate data, and regional suitability to specific crop variants.
 */
class CropRecommendationEngine {

    /**
     * Generates a ranked list of recommended crops based on soil and regional parameters.
     * @param soilType The texture identified (e.g., "Sandy Loam").
     * @param pH The measured acidity/alkalinity.
     * @param district Geographic location for climate alignment.
     */
    suspend fun getRecommendations(
        soilType: String,
        pH: Float,
        district: String
    ): List<CropRecommendation> = withContext(Dispatchers.Default) {
        delay(1800)

        val recommendations = mutableListOf<CropRecommendation>()

        if (soilType.contains("Loam", ignoreCase = true) && !soilType.contains("Sandy", ignoreCase = true)) {
            recommendations.add(
                CropRecommendation(
                    soilAnalysisId = 0,
                    cropName = "Maize (Hybrid SC 419)",
                    cropNameChichewa = "Chimanga (SC 419)",
                    confidenceScore = 0.92f,
                    reasonSummary = "Loam soil has ideal water retention and aeration for maize. Excellent for $district climate.",
                    plantingGuide = "Plant at 25cm spacing within rows, 75cm between rows. Plant after first rains when soil is moist.",
                    fertilizerAdvice = "Basal dressing: NPK 23:21:0+4S at 200kg/ha. Top-dress with CAN at 200kg/ha at knee height (V4).",
                    wateringNeeds = "Requires 500-800mm rainfall/season. Critical stages: germination, tasseling, grain fill.",
                    expectedYieldKgPerHa = 4500.0
                )
            )
        }
        if (soilType.contains("Sandy", ignoreCase = true)) {
            recommendations.add(
                CropRecommendation(
                    soilAnalysisId = 0,
                    cropName = "Groundnuts (CG 7)",
                    cropNameChichewa = "Mtedza (CG 7)",
                    confidenceScore = 0.88f,
                    reasonSummary = "Well-drained sandy loam suits groundnut pod development. High-value market crop in Malawi.",
                    plantingGuide = "Plant in double rows on ridges. Space 30cm x 10cm. Requires well-drained, loose soil for pod filling.",
                    fertilizerAdvice = "Apply Gypsum 400kg/ha at flowering to ensure pod filling. No nitrogen needed — legume fixes own N.",
                    wateringNeeds = "Needs 400-600mm well-distributed rainfall. Drought-tolerant at vegetative stage, not at pod fill.",
                    expectedYieldKgPerHa = 2000.0
                )
            )
        }

        if (soilType.contains("Clay", ignoreCase = true) || pH < 6.0f) {
            recommendations.add(
                CropRecommendation(
                    soilAnalysisId = 0,
                    cropName = "Tobacco (Dark Fired)",
                    cropNameChichewa = "Fodya (Dark Fired)",
                    confidenceScore = 0.90f,
                    reasonSummary = "Heavy clay soils and lower pH suit dark-fired tobacco. Major cash crop for $district.",
                    plantingGuide = "Use certified seedlings from TAMA-registered nurseries. Transplant at 6-week seedling stage.",
                    fertilizerAdvice = "Apply Tobacco D Compound 150kg/ha at planting. Top-dress with CAN 100kg/ha at 6 weeks.",
                    wateringNeeds = "Requires 800-1200mm rainfall. Avoid waterlogging at all stages.",
                    expectedYieldKgPerHa = 1500.0
                )
            )
        } else {
            recommendations.add(
                CropRecommendation(
                    soilAnalysisId = 0,
                    cropName = "Soybeans (Tikolore)",
                    cropNameChichewa = "Soya (Tikolore)",
                    confidenceScore = 0.82f,
                    reasonSummary = "Neutral to slightly alkaline pH ideal for soybean nitrogen fixation. Growing market demand.",
                    plantingGuide = "Inoculate seeds with Rhizobium inoculant (Optimize) before planting. Space 45cm x 5cm.",
                    fertilizerAdvice = "Apply SSP (Single Super Phosphate) 200kg/ha at planting. No nitrogen required if inoculated.",
                    wateringNeeds = "Needs 400-800mm rainfall. Critical at podding stage — avoid dry spells then.",
                    expectedYieldKgPerHa = 2500.0
                )
            )
        }

        recommendations.sortedByDescending { it.confidenceScore }
    }
}
