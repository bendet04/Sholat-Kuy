package id.bhinneka.sholatkuy.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import id.bhinneka.sholatkuy.R
import id.bhinneka.sholatkuy.db.DatabaseHelper
import id.bhinneka.sholatkuy.experimental.Android
import id.bhinneka.sholatkuy.model.Data
import id.bhinneka.sholatkuy.network.api.ShalatClient
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = javaClass.simpleName
    private val REQUEST_CODE_ACTIVITY = 210
    private var dataCity: Data? = null
    private val databaseHelper by lazy {
        DatabaseHelper(
                context = this@HomeActivity,
                name = DatabaseHelper.DATABASE_NAME,
                factory = null,
                version = DatabaseHelper.DATABASE_VERSION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initListener()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_activity_home)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun initListener() {
        text_view_value_location_city_activity_home.setOnClickListener(this)
        button_submit_activity_home.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.id.let {
            when (it) {
                R.id.text_view_value_location_city_activity_home -> {
                    startActivityForResult(Intent(this@HomeActivity,
                            CityActivity::class.java), REQUEST_CODE_ACTIVITY)
                }
                R.id.button_submit_activity_home -> {
                    if (text_view_value_location_city_activity_home.text
                                    .equals(getString(R.string.choose_location))) {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                getString(R.string.please_choose_location),
                                Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        doLoadDataPrayerSchedule()
                    }
                }
            }
        }
    }

    private fun doLoadDataPrayerSchedule() {
        val progressDialog = ProgressDialog(this)
        progressDialog.let {
            it.setCancelable(false)
            it.setMessage(getString(R.string.please_wait))
            it.show()
        }
        launch(Android) {
            val resultDataPrayerSchedule = ShalatClient
                    .getPrayerScheduleData(id = dataCity!!.id.toInt())
                    .await()
            progressDialog.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
            val intentPrayerScheduleActivity = Intent(this@HomeActivity, SchedulePrayerActivity::class.java)
            intentPrayerScheduleActivity.putExtra("dataPrayer", resultDataPrayerSchedule)
            intentPrayerScheduleActivity.putExtra("locationCity", dataCity!!.namaKota)
            startActivity(intentPrayerScheduleActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_ACTIVITY -> {
                        val bundle = data?.extras
                        val dataCity = bundle?.get("data") as Data
                        this@HomeActivity.dataCity = dataCity.copy()
                        text_view_value_location_city_activity_home.text = dataCity.namaKota
                        databaseHelper.insertCitySelected(dataCity)
                    }
                    else -> {
                        /** do nothing **/
                    }
                }
            }
            else -> {
                /** do nothing **/
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}