package database.kotlin.flow9.net.kotlindatabasebasic

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class DbHelper(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var mContext: Context? = null
    var DATABASE_PATH: String? = null

    init {
        this.mContext = context
        DATABASE_PATH = "/data/data/"+context?.packageName+"/"+"databases/";
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "test_db.db"
        const val TABLE_NAME    = "test_user"
        const val COL_NAME      = "name"
        const val COL_ID        = "id"
        const val COL_PASSWORD  = "password"
        const val COL_AGE       = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createDbSQL = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID        TEXT NOT NULL, " +
                "$COL_NAME      TEXT NOT NULL, " +
                "$COL_PASSWORD  TEXT NOT NULL, " +
                "$COL_AGE       INTEGER, " +
                "PRIMARY KEY($COL_NAME)" +
                ");"
        db?.execSQL(createDbSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val upgradeDbSQL = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(upgradeDbSQL)
    }

    private fun databaseExists(dbName: String?): Boolean {
        return File(dbName).exists()
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_ID, user.id)
        values.put(COL_PASSWORD, user.password)
        values.put(COL_AGE, user.age)
        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getUser(): ArrayList<User> {
        val selectSQL = "SELECT * FROM $TABLE_NAME"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSQL, null)
        val userList = ArrayList<User>()
        while (cursor.moveToNext()) {
            val selectedId = cursor.getString(cursor.getColumnIndex(COL_ID))
            val selectedName = cursor.getString(cursor.getColumnIndex(COL_NAME))
            val selectedPassword = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))
            val selectedAge = cursor.getInt(cursor.getColumnIndex(COL_AGE))
            userList.add(User(selectedId, selectedName, selectedPassword, selectedAge))
        }
        cursor.close()
        db.close()
        return userList
    }

    fun updateUser(user: User) {
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_ID, user.id)
        values.put(COL_PASSWORD, user.password)
        values.put(COL_AGE, user.age)
        val db = writableDatabase
        db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(user.id))
        db.close()
    }

    fun deleteUser(id: String) {
        val deleteSQL = "DELETE FROM $TABLE_NAME WHERE $COL_ID=$id"
        val db = writableDatabase
        db.execSQL(deleteSQL)
        db.close()
    }

}