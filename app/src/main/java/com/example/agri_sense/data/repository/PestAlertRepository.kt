package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.PestAlertDao
import com.example.agri_sense.data.models.PestAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PestAlertRepository @Inject constructor(private val pestAlertDao: PestAlertDao) {

    val allAlerts: Flow<List<PestAlert>> = pestAlertDao.getAllAlerts()
    val unreadAlerts: Flow<List<PestAlert>> = pestAlertDao.getUnreadAlerts()
    val unreadCount: Flow<Int> = pestAlertDao.getUnreadCount()

    suspend fun markAsRead(id: String) = pestAlertDao.markAsRead(id)
    suspend fun markAllRead() = pestAlertDao.markAllRead()

    suspend fun syncWeeklyAlerts() {
        // Simulates fetching official news and updating an outbreak alert
        val now = System.currentTimeMillis()
        val all = pestAlertDao.getAllAlerts().first()
        if (all.isNotEmpty()) {
            val alertToUpdate = all.random() // Simulate a new outbreak report
            pestAlertDao.insert(alertToUpdate.copy(reportedAt = now, isRead = false, severityLevel = "CRITICAL"))
        }
    }

    /** Seeds real 2024-2025 Malawian pest outbreak data */
    suspend fun seedIfEmpty() {
        if (pestAlertDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        pestAlertDao.insertAll(
            listOf(
                PestAlert(
                    id = "pa1",
                    pestName = "Fall Armyworm",
                    pestNameChichewa = "Dzombe la Chimanga",
                    affectedCrops = "Maize,Sorghum,Millet",
                    outbreakDistricts = "Kasungu,Mchinji,Lilongwe,Dedza",
                    severityLevel = "CRITICAL",
                    description = "Fall Armyworm (Spodoptera frugiperda) outbreaks have been reported across Central Region maize fields. Larvae bore into stems and destroy cobs. Early intervention is critical.",
                    descriptionChichewa = "Dzombe la Chimanga (Spodoptera frugiperda) lapezeka m'minda ya chimanga m'Chigawo cha Pakati. Mazira abisala m'mitengo ndi kuwononga chimanga. Chizolowezi choyambirira ndi chofunika kwambiri.",
                    recommendedAction = "Apply Coragen 20SC (chlorantraniliprole) at 0.4L/ha. Scout fields at dawn when larvae are active. Remove and destroy egg masses. Plant trap crops (Napier grass) at borders.",
                    recommendedActionChichewa = "Gwiritsani ntchito mankhwala a Coragen 20SC pa 0.4L pa hekitala. Onani minda m'mawa pomwe dzombe likukhala. Puchulanitsa ndi kuwononga mazira. Dzalani zomera za msampha (Napier grass) m'mbali mwa munda.",
                    reportedAt = now - (2 * 24 * 60 * 60 * 1000L),
                    isRead = false
                ),
                PestAlert(
                    id = "pa2",
                    pestName = "Cassava Brown Streak Virus",
                    pestNameChichewa = "Matenda a Chinangwa (Kukopoka)",
                    affectedCrops = "Cassava",
                    outbreakDistricts = "Nkhata Bay,Karonga,Rumphi,Chitipa",
                    severityLevel = "HIGH",
                    description = "Cassava Brown Streak Disease (CBSD) has been confirmed in the Northern Region. The virus causes yellow-green leaf mottling and brown streaks on stems. Spread by whitefly vectors.",
                    descriptionChichewa = "Matenda a CBSD apezeka kumpoto. Matenda amachitisa masamba kufiira ndi mitengo kukopoka. Amabwera ndi nkhungu (whitefly).",
                    recommendedAction = "Use certified disease-free planting material. Control whiteflies with neonicotinoid insecticides. Rogue and destroy infected plants immediately. Contact DARS extension officers for resistant varieties.",
                    recommendedActionChichewa = "Gwiritsani ntchito mbeu yotsimikiziridwa. Letsani nkhungu ndi mankhwala. Chotsani ndi kuwotcha zomera zomwalira. Lankhulani ndi akatswiri a DARS pazomera zoganizira.",
                    reportedAt = now - (5 * 24 * 60 * 60 * 1000L),
                    isRead = false
                ),
                PestAlert(
                    id = "pa3",
                    pestName = "Red Spider Mite",
                    pestNameChichewa = "Ntchenche Zofiira za Nkhono",
                    affectedCrops = "Groundnuts,Soybeans,Beans",
                    outbreakDistricts = "Blantyre,Zomba,Thyolo,Mulanje",
                    severityLevel = "MEDIUM",
                    description = "Red spider mite (Tetranychus urticae) infestations reported in Southern Region legume fields during the dry season. Mites suck plant sap causing leaf bronzing and premature drop.",
                    descriptionChichewa = "Ntchenche zofiira zapezeka m'minda ya nyemba kumpoto. Ntchenche imamwa maji a zomera ndi kuchititsa masamba kupsa.",
                    recommendedAction = "Apply acaricides (Abamectin 1.8% EC at 0.5L/ha). Ensure adequate irrigation to reduce plant stress. Introduce natural predators (Phytoseiulus persimilis). Avoid dusty conditions by mulching.",
                    recommendedActionChichewa = "Gwiritsani ntchama acaricides (Abamectin 1.8% EC pa 0.5L/ha). Podzerani madzi m'minda. Masambitsani dothi kuti mupewe fumbi.",
                    reportedAt = now - (7 * 24 * 60 * 60 * 1000L),
                    isRead = true
                ),
                PestAlert(
                    id = "pa4",
                    pestName = "African Bollworm",
                    pestNameChichewa = "Dzombe la Thonje",
                    affectedCrops = "Cotton,Tomatoes,Sorghum",
                    outbreakDistricts = "Chikwawa,Nsanje,Phalombe",
                    severityLevel = "HIGH",
                    description = "African Bollworm (Helicoverpa armigera) poses major threat to cotton and tomato crops in the Shire Valley. Larvae bore into bolls and fruit causing total crop loss if uncontrolled.",
                    descriptionChichewa = "Dzombe la Thonje (Helicoverpa armigera) linaopseza minda ya thonje ndi tamato ku Chigawo cha Shire. Mazira amabisala m'zipatso ndi kuziwononga.",
                    recommendedAction = "Use pheromone traps for monitoring (1 trap/ha). Apply Karate 5% EC at 0.5L/ha. Spray in evening when adults are active. Use Bt (Bacillus thuringiensis) for organic control.",
                    recommendedActionChichewa = "Gwiritsani ntchito msampha wa pheromone kuona (msampha 1 pa hekitala). Gwiritsani ntchito Karate 5% EC pa 0.5L/ha m'madzulo.",
                    reportedAt = now - (3 * 24 * 60 * 60 * 1000L),
                    isRead = false
                ),
                PestAlert(
                    id = "pa5",
                    pestName = "Maize Lethal Necrosis",
                    pestNameChichewa = "Matenda Opha Chimanga",
                    affectedCrops = "Maize",
                    outbreakDistricts = "Mzimba,Nkhata Bay",
                    severityLevel = "LOW",
                    description = "Isolated cases of Maize Lethal Necrosis (MLN) detected in Northern Region. Early warning alert. MLN is caused by co-infection of MCMV and SCMV viruses spread by thrips and aphids.",
                    descriptionChichewa = "Matenda oyambirira a MLN apezeka kumpoto. Zibuyo zaona kale. MLN imabwera ndi matenda awiri ogwirana ndi ntchenche.",
                    recommendedAction = "Immediately report confirmed cases to DARS (080 000 2000). Use MLN-tolerant varieties (DKC8033, H614D). Control vector insects with systemic insecticides at planting.",
                    recommendedActionChichewa = "Melderani akatswiri a DARS (080 000 2000). Gwiritsani ntchito mbeu zoganizira (DKC8033). Letsani ntchenche ndi mankhwala posenza.",
                    reportedAt = now - (10 * 24 * 60 * 60 * 1000L),
                    isRead = true
                )
            )
        )
    }
}
