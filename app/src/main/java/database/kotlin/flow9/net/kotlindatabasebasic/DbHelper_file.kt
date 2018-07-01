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

class DbHelper_file(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var mContext: Context? = null
    var DATABASE_PATH: String? = null
    var DB_FILE_PATH: String? = null

    init {
        this.mContext = context
        DATABASE_PATH = "/data/data/"+context?.packageName+"/"+"databases/testdb.db";
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "testdb.db"
        const val TABLE_NAME    = "test_user"
        const val COL_NAME      = "name"
        const val COL_ID        = "id"
        const val COL_PASSWORD  = "password"
        const val COL_AGE       = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        copyDatabase(mContext?.assets, DATABASE_NAME, DATABASE_PATH)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val upgradeDbSQL = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(upgradeDbSQL)
    }

    private fun databaseExists(dbName: String?): Boolean {
        return File(dbName).exists()
    }

    fun checkDatabase(): Boolean ? {

        var checkDB: SQLiteDatabase? = null
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException) {

        }

        if (checkDB != null) {
            checkDB.close()
        }

        return checkDB?.isOpen
    }

    private fun copyDatabase(assetManager: AssetManager?, dbAssetPath: String, dbPath: String?) {
        if (databaseExists(DATABASE_NAME))  return
        var sqlInputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            sqlInputStream = assetManager?.open(dbAssetPath)
            outputStream = FileOutputStream(dbPath)
            val buffer = ByteArray(8192)
            var length: Int
            length = sqlInputStream?.read() ?: 0
            while (length > 0) {
                outputStream.write(buffer, 0, length)
                length = sqlInputStream?.read() ?: 0
            }
        } catch (e: SQLiteException) {
            throw RuntimeException("copy database failed!!", e)
        } finally {
            outputStream?.flush()
            outputStream?.close()
            sqlInputStream?.close()
        }
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