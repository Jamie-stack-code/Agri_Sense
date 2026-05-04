package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.MarketPriceDao
import com.example.agri_sense.data.models.MarketPrice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor(private val marketDao: MarketPriceDao) {

    val allPrices: Flow<List<MarketPrice>> = marketDao.getAllPrices()

    fun getPricesForDistrict(district: String): Flow<List<MarketPrice>> =
        marketDao.getPricesByDistrict(district)

    fun searchPrices(query: String): Flow<List<MarketPrice>> =
        marketDao.searchPrices(query)

    /** Seeds realistic Malawian crop market prices on first launch */
    suspend fun seedIfEmpty() {
        if (marketDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        marketDao.insertAll(
            listOf(
                // === CENTRAL REGION ===
                MarketPrice("mp1", "Maize", "Chimanga", 380.0, "MWK/kg", "Lilongwe Central Market", "Lilongwe", "Central", -13.9626, 33.7741, +4.2, now),
                MarketPrice("mp2", "Groundnuts", "Mtedza", 1450.0, "MWK/kg", "Lilongwe Central Market", "Lilongwe", "Central", -13.9626, 33.7741, +12.8, now),
                MarketPrice("mp3", "Soybeans", "Soya", 980.0, "MWK/kg", "Kanengo Market", "Lilongwe", "Central", -13.9214, 33.8245, +6.5, now),
                MarketPrice("mp4", "Tomatoes", "Tamato", 320.0, "MWK/kg", "Area 25 Market", "Lilongwe", "Central", -13.9800, 33.7600, -3.1, now),
                MarketPrice("mp5", "Onions", "Anyezi", 850.0, "MWK/kg", "Lilongwe Old Town Market", "Lilongwe", "Central", -13.9500, 33.7200, +8.9, now),
                MarketPrice("mp6", "Beans", "Nyemba", 1100.0, "MWK/kg", "Kasungu Boma Market", "Kasungu", "Central", -13.0061, 33.4745, +5.3, now),
                MarketPrice("mp7", "Rice", "Mpunga", 750.0, "MWK/kg", "Salima Market", "Salima", "Central", -13.7804, 34.4587, +2.7, now),
                MarketPrice("mp8", "Pigeon Peas", "Nandolo", 890.0, "MWK/kg", "Dedza Market", "Dedza", "Central", -14.3667, 34.3333, +9.1, now),

                // === NORTHERN REGION ===
                MarketPrice("mp9", "Maize", "Chimanga", 420.0, "MWK/kg", "Mzuzu Main Market", "Mzimba", "Northern", -11.4656, 34.0207, +3.8, now),
                MarketPrice("mp10", "Tobacco", "Fodya", 3200.0, "MWK/kg", "Mzuzu Auction Floor", "Mzimba", "Northern", -11.4500, 34.0100, +15.2, now),
                MarketPrice("mp11", "Groundnuts", "Mtedza", 1380.0, "MWK/kg", "Rumphi Market", "Rumphi", "Northern", -10.8765, 33.8637, +11.4, now),
                MarketPrice("mp12", "Potatoes", "Mbatata", 480.0, "MWK/kg", "Karonga Market", "Karonga", "Northern", -9.9333, 33.9333, +6.7, now),
                MarketPrice("mp13", "Cassava", "Chinangwa", 180.0, "MWK/kg", "Nkhata Bay Market", "Nkhata Bay", "Northern", -11.6000, 34.3000, -1.5, now),

                // === SOUTHERN REGION ===
                MarketPrice("mp14", "Maize", "Chimanga", 360.0, "MWK/kg", "Limbe Market", "Blantyre", "Southern", -15.8167, 35.0500, +5.6, now),
                MarketPrice("mp15", "Tomatoes", "Tamato", 290.0, "MWK/kg", "Blantyre SMDI Market", "Blantyre", "Southern", -15.7861, 35.0058, -4.2, now),
                MarketPrice("mp16", "Groundnuts", "Mtedza", 1520.0, "MWK/kg", "Zomba Market", "Zomba", "Southern", -15.3833, 35.3333, +13.7, now),
                MarketPrice("mp17", "Sugar Cane", "Nsatsi", 95.0, "MWK/kg", "Chikwawa Market", "Chikwawa", "Southern", -16.0333, 34.8000, +2.1, now),
                MarketPrice("mp18", "Pigeon Peas", "Nandolo", 920.0, "MWK/kg", "Mangochi Market", "Mangochi", "Southern", -14.4783, 35.2649, +10.3, now),
                MarketPrice("mp19", "Sorghum", "Mapira", 650.0, "MWK/kg", "Nsanje Market", "Nsanje", "Southern", -16.9167, 35.2667, +4.8, now),
                MarketPrice("mp20", "Bananas", "Nthochi", 250.0, "MWK/kg", "Mulanje Market", "Mulanje", "Southern", -15.9333, 35.5000, +1.9, now)
            )
        )
    }
}
