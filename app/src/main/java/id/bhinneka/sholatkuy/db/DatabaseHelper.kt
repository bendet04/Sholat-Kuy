package id.bhinneka.sholatkuy.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import id.bhinneka.sholatkuy.model.Data
import id.bhinneka.sholatkuy.model.DataSholat
import java.sql.SQLException

class DatabaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?,
                     version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    private val TAG = javaClass.simpleName

    companion object {
        val DATABASE_NAME = "sholatkuy.db"
        val DATABASE_VERSION = 1
    }

    private val CITY_TABLE_NAME = "kota"
    private val CITY_TABLE_NAME_COLUMN_ID = "id"
    private val CITY_TABLE_NAME_COLUMN_CITY_NAME = "name"
    private val CITY_TABLE_SELECT = "select * from $CITY_TABLE_NAME"
    private val CITY_TABLE_CREATE = "create table if not exists $CITY_TABLE_NAME " +
            "($CITY_TABLE_NAME_COLUMN_ID integer primary key," +
            "$CITY_TABLE_NAME_COLUMN_CITY_NAME text " +
            ")"
    private val CITY_TABLE_DROP = "drop table if exists $CITY_TABLE_NAME"
    private val CITY_TABLE_SELECTED = "kota_selected"
    private val CITY_TABLE_SELECTED_CREATE = "create table if not exists $CITY_TABLE_SELECTED " +
            "($CITY_TABLE_NAME_COLUMN_ID integer primary key," +
            "$CITY_TABLE_NAME_COLUMN_CITY_NAME text " +
            ")"
    private val CITY_TABLE_SELECTED_DROP = "drop table if exists $CITY_TABLE_SELECTED"
    private val CITY_TABLE_SELECTED_SELECT = "select * from $CITY_TABLE_SELECTED"
    private val SCHEDULE_PRAYER_TABLE = "jadwal_sholat"
    private val SCHEDULE_PRAYER_ID = "id"
    private val SCHEDULE_PRAYER_MONTH = "bulan"
    private val SCHEDULE_PRAYER_YEAR = "tahun"
    private val SCHEDULE_PRAYER_DATE = "tanggal"
    private val SCHEDULE_PRAYER_IMSYAK = "imsyak"
    private val SCHEDULE_PRAYER_SHUBUH = "shubuh"
    private val SCHEDULE_PRAYER_TERBIT = "terbit"
    private val SCHEDULE_PRAYER_DHUHA = "dhuha"
    private val SCHEDULE_PRAYER_DZUHUR = "dzuhur"
    private val SCHEDULE_PRAYER_ASHR = "ashr"
    private val SCHEDULE_PRAYER_MAGHRIB = "maghrib"
    private val SCHEDULE_PRAYER_ISYA = "isya"
    private val SCHEDULE_PRAYER_CREATE = "create table if not exists $SCHEDULE_PRAYER_TABLE " +
            "($SCHEDULE_PRAYER_TABLE integer primary key autoincrement," +
            "$SCHEDULE_PRAYER_YEAR varchar(4)," +
            "$SCHEDULE_PRAYER_MONTH varchar(2)," +
            "$SCHEDULE_PRAYER_DATE varchar(2), " +
            "$SCHEDULE_PRAYER_IMSYAK varchar(5)," +
            "$SCHEDULE_PRAYER_SHUBUH varchar(5)," +
            "$SCHEDULE_PRAYER_TERBIT varchar(5)," +
            "$SCHEDULE_PRAYER_DHUHA varchar(5)," +
            "$SCHEDULE_PRAYER_DZUHUR varchar(5)," +
            "$SCHEDULE_PRAYER_ASHR varchar(5)," +
            "$SCHEDULE_PRAYER_MAGHRIB varchar(5)," +
            "$SCHEDULE_PRAYER_ISYA varchar(5)" +
            ")"
    private val SCHDULE_PRAYER_DROP = "drop table if exists $SCHEDULE_PRAYER_TABLE"

    /**
     * @description For create database
     * @param sqliteDatabase (SQLiteDatabase) object SQLiteDatabase
     */
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CITY_TABLE_CREATE)
            db?.execSQL(CITY_TABLE_SELECTED_CREATE)
            db?.execSQL(SCHEDULE_PRAYER_CREATE)
        } catch (sqle: SQLException) {
            sqle.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL(CITY_TABLE_DROP)
            db?.execSQL(CITY_TABLE_CREATE)
            db?.execSQL(CITY_TABLE_SELECTED_DROP)
            db?.execSQL(CITY_TABLE_SELECTED_CREATE)
            db?.execSQL(SCHDULE_PRAYER_DROP)
            db?.execSQL(SCHEDULE_PRAYER_CREATE)
        } catch (sqle: SQLException) {
            sqle.printStackTrace()
        }
    }

    fun insertDataCity(data: Data): Long {
        try {
            val sqliteDatabase = writableDatabase
            val contentValue = ContentValues()
            contentValue.put(CITY_TABLE_NAME_COLUMN_ID, data.id)
            contentValue.put(CITY_TABLE_NAME_COLUMN_CITY_NAME, data.namaKota)
            return sqliteDatabase.insert(
                    CITY_TABLE_NAME,
                    null,
                    contentValue
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun insertDataCity(listData: List<Data>): Int {
        try {
            val sqliteDatabase = writableDatabase
            val queryInsert = "insert into $CITY_TABLE_NAME " +
                    "($CITY_TABLE_NAME_COLUMN_ID, $CITY_TABLE_NAME_COLUMN_CITY_NAME) " +
                    "values " +
                    "(?, ?)"
            sqliteDatabase.beginTransaction()
            val sqliteStatement = sqliteDatabase.compileStatement(queryInsert)
            for (data in listData) {
                sqliteStatement.bindString(1, data.id)
                sqliteStatement.bindString(2, data.namaKota)
                sqliteStatement.execute()
                sqliteStatement.clearBindings()
            }
            sqliteDatabase.setTransactionSuccessful()
            sqliteDatabase.endTransaction()
            return 1
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteDataCity(): Int {
        try {
            val sqliteDatabase = writableDatabase
            return sqliteDatabase.delete(
                    CITY_TABLE_NAME,
                    null,
                    null
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteDataCityById(id: Int): Int {
        try {
            val sqliteDatabase = writableDatabase
            return sqliteDatabase.delete(
                    CITY_TABLE_NAME,
                    "$CITY_TABLE_NAME_COLUMN_ID = ?",
                    Array(1, { id.toString() })
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun countDataCity(): Int {
        val itemCount: Int
        try {
            val sqliteDatabase = writableDatabase
            val cursor = sqliteDatabase.rawQuery(
                    CITY_TABLE_SELECT,
                    null,
                    null
            )
            itemCount = cursor.count
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        return itemCount
    }

    fun getDataCity(): List<Data> {
        val listDataCity = ArrayList<Data>()
        try {
            val sqliteDatabase = writableDatabase
            val cursor = sqliteDatabase.rawQuery(
                    CITY_TABLE_SELECT,
                    null,
                    null
            )
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val dataKota = Data(
                            id = cursor.getInt(cursor.getColumnIndex(CITY_TABLE_NAME_COLUMN_ID)).toString(),
                            namaKota = cursor.getString(cursor.getColumnIndex(CITY_TABLE_NAME_COLUMN_CITY_NAME))
                    )
                    listDataCity.add(dataKota)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listDataCity
    }

    fun insertCitySelected(data: Data): Long {
        try {
            val sqliteDatabase = writableDatabase
            val contentValues = ContentValues()
            contentValues.put(CITY_TABLE_NAME_COLUMN_ID, data.id)
            contentValues.put(CITY_TABLE_NAME_COLUMN_CITY_NAME, data.namaKota)
            return sqliteDatabase.insert(
                    CITY_TABLE_SELECTED,
                    null,
                    contentValues
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteCitySelected(): Int {
        try {
            val sqliteDatabase = writableDatabase
            return sqliteDatabase.delete(
                    CITY_TABLE_SELECTED,
                    null,
                    null
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun getDataCitySelected(): List<Data> {
        val listDataCity = ArrayList<Data>()
        try {
            val sqliteDatabase = writableDatabase
            val cursor = sqliteDatabase.rawQuery(
                    CITY_TABLE_SELECTED_SELECT,
                    null,
                    null
            )
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val dataKota = Data(
                            id = cursor.getInt(cursor.getColumnIndex(CITY_TABLE_NAME_COLUMN_ID)).toString(),
                            namaKota = cursor.getString(cursor.getColumnIndex(CITY_TABLE_NAME_COLUMN_CITY_NAME))
                    )
                    listDataCity.add(dataKota)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listDataCity
    }
 /**
    fun insertPrayerScedule(listData: List<DataSholat>, date: String): Long {
        try {
            val sqliteDatabase = writableDatabase
            val queryInsert = "insert into $SCHEDULE_PRAYER_TABLE " +
                    "($CITY_TABLE_NAME_COLUMN_ID, $CITY_TABLE_NAME_COLUMN_CITY_NAME) " +
                    "values " +
                    "(?, ?)"
            sqliteDatabase.beginTransaction()
            val sqliteStatement = sqliteDatabase.compileStatement(queryInsert)
            for (data in listData) {
                sqliteStatement.bindString(1, data.)
                sqliteStatement.bindString(2, data.namaKota)
                sqliteStatement.execute()
                sqliteStatement.clearBindings()
            }
            sqliteDatabase.setTransactionSuccessful()
            sqliteDatabase.endTransaction()
            return 1
        }
    }
**/

}