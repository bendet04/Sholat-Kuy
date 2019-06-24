package id.bhinneka.sholatkuy.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import id.bhinneka.sholatkuy.R
import id.bhinneka.sholatkuy.db.DatabaseHelper
import id.bhinneka.sholatkuy.experimental.Android
import id.bhinneka.sholatkuy.model.Data
import id.bhinneka.sholatkuy.network.api.ShalatClient
import kotlinx.coroutines.experimental.launch

class SplashScreenActivity: AppCompatActivity (){
    private val TAG = javaClass.simpleName
    private val databaseHelper by lazy {
        DatabaseHelper(
                context = this@SplashScreenActivity,
                name = DatabaseHelper.DATABASE_NAME,
                factory = null,
                version = DatabaseHelper.DATABASE_VERSION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        doLoadData()
    }

    private fun doLoadData(){
        launch(Android) {
            val itemCountDataCityLocal =  databaseHelper.countDataCity()
            val selectedCity = databaseHelper.getDataCitySelected();
            val resultDataKota = ShalatClient.getCityData().await()
            val intentHomeActivity = Intent(this@SplashScreenActivity, HomeActivity::class.java)

            intentHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            if (itemCountDataCityLocal == resultDataKota.count){
                if (selectedCity.isEmpty()){
                    startActivity(intentHomeActivity)
                }else{
                    Log.e(TAG, "ID Kota : "+selectedCity[0].id)
                    Log.e(TAG, "Nama Kota : "+selectedCity[0].namaKota)
                    launch(Android) {
                        val resultDataPrayerSchedule = ShalatClient
                                .getPrayerScheduleData(id = selectedCity[0].id.toInt())
                                .await()

                        val intentPrayerScheduleActivity = Intent(this@SplashScreenActivity, SchedulePrayerActivity::class.java)
                        intentPrayerScheduleActivity.putExtra("dataPrayer", resultDataPrayerSchedule)
                        intentPrayerScheduleActivity.putExtra("locationCity", selectedCity[0].namaKota)
                        startActivity(intentPrayerScheduleActivity)
                        finish()
                    }
                }

            }else{
                databaseHelper.deleteDataCity()
                databaseHelper.insertDataCity(resultDataKota.data)
                startActivity(intentHomeActivity)
            }
        }
    }
}