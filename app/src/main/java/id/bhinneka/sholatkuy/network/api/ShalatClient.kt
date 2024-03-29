package id.bhinneka.sholatkuy.network.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.bhinneka.sholatkuy.BuildConfig
import id.bhinneka.sholatkuy.model.DataJadwalSholat
import id.bhinneka.sholatkuy.model.DataKota
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*


object ShalatClient {
    private val TAG = javaClass.simpleName
    val okhttpClient = OkHttpClient()

    /**
     * @description Get City Data from API
     * @return {Defferd<DataKota> return value DataKota from API
     */
    fun getCityData(): Deferred<DataKota>{
        return async (CommonPool){
            val request = Request.Builder()
                    .url(BuildConfig.BASE_URL + BuildConfig.API_KEY + "/jadwal-sholat/get-kota")
                    .build()
            val response = okhttpClient.newCall(request).execute()
            val dataKota = object : TypeToken<DataKota>() {}.type
            Gson().fromJson<DataKota> (response.body()!!.string(), dataKota)

        }
    }
    /**
     * @description Get Prayer Schedule Data from API
     * @param {Int} id Unique id for each city
     * @return {Deferred<DataJadwalSholat>} Return value DataJadwalSholat from API
     */
    fun getPrayerScheduleData(id: Int): Deferred<DataJadwalSholat>{
        return async (CommonPool){
            var url = BuildConfig.BASE_URL + BuildConfig.API_KEY + "/jadwal-sholat"
            val strDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            url += "?idk=$id"
            url += url.let {
                val strMonth = strDate.substring(5, 5 + 2)
                val strYear = strDate.substring(0, 0 + 4)
                "&bln=$strMonth&thn=$strYear"
            }

            val request = Request.Builder()
                    .url(url)
                    .build()
            Log.e(TAG, "url "+url);
            val response = okhttpClient.newCall(request).execute()
            val dataPrayerSchedule = object : TypeToken<DataJadwalSholat> () {}.type
            Gson().fromJson<DataJadwalSholat>(response.body()!!.string(), dataPrayerSchedule)
        }
    }
}