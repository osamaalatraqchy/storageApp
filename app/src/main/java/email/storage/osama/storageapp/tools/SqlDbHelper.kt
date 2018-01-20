package email.storage.osama.storageapp.tools

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//class to make db and table and determin the db version//
class SqlDbHelper(val context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "storage.db"
        val DB_VERSION = 1
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_TABLE = ("CREATE TABLE " + StorageContract.TABLE_NAME + " ("+
                StorageContract._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                StorageContract.R_NAME+ " TEXT NOT NULL,"+
                StorageContract.R_ADRESS+ " TEXT NOT NULL,"+
                StorageContract.R_PHONE+ " TEXT NOT NULL,"+
                StorageContract.R_GENDER+ " TEXT NOT NULL,"+
                StorageContract.R_TIME+ " TEXT NOT NULL );")
        db!!.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}