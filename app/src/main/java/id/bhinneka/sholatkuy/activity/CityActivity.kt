package id.bhinneka.sholatkuy.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import id.bhinneka.sholatkuy.R
import id.bhinneka.sholatkuy.adapter.AdapterDataCity
import id.bhinneka.sholatkuy.db.DatabaseHelper
import id.bhinneka.sholatkuy.model.Data
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.android.synthetic.main.activity_home.*

class CityActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val databaseHelper by lazy {
        DatabaseHelper(
                context = this@CityActivity,
                name = DatabaseHelper.DATABASE_NAME,
                factory = null,
                version = DatabaseHelper.DATABASE_VERSION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        initToolbar()
        doLoadData()
    }

    private fun doLoadData(){
        val listDataCity = databaseHelper.getDataCity()
        recycler_view_data_city_activity_city.let {
            val adapterDataCity = AdapterDataCity(
                    listDataCity = listDataCity,
                    listenerAdapterDataCity = object : AdapterDataCity.ListenerAdapterDataCity {
                        override fun clickItem(data: Data) {
                            val intent = Intent()
                            intent.putExtra("data", data)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
            )
            val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            it.addItemDecoration(dividerItemDecoration)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapterDataCity
        }
    }

    private fun initToolbar(){
        setSupportActionBar(toolbar_activity_home)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.choose_location)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        item?.itemId.let {
            return when (it) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }

}