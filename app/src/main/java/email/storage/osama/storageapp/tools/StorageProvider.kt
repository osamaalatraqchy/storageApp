package email.storage.osama.storageapp.tools

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.content.UriMatcher
import email.storage.osama.storageapp.Home
import java.lang.IllegalStateException

// TODO: UI <-> ContentResolver <-> StorageProvider <-> SqliteHelper//
// TODO: all database operation (CRUD) is done here//
// TODO: this class need to know: (Action -> CRUD) and (Data -> Uri) -> data need (UriMatcher)

class StorageProvider:ContentProvider() {

    ////////////TODO: URI Matcher////////////////
companion object {
    val MATCH_RESERV = 100
    val MATCH_RESERV_ID = 101
        var Matcher = UriMatcher(UriMatcher.NO_MATCH)

}
    init {
        //TODO: initial Uri Matcher for Two Uri//
        Matcher.addURI(StorageContract.URI_AUTH, StorageContract.URI_TABLE, MATCH_RESERV)
        Matcher.addURI(StorageContract.URI_AUTH, StorageContract.URI_TABLE+"#/", MATCH_RESERV_ID)
    }
////////////////////////////////////////////////TODO: End Matcher/////
    // initial SqlDbHelper to enabes integrate with it//
    override fun onCreate(): Boolean {
    // access to database by make instance from SQLiteOpenHelper subclass
     SqlDbHelper(context)

        return true
    }

    //TODO: connect with Add class in ContentResolver...
    override fun insert(uri: Uri, values: ContentValues): Uri? {
var match = Matcher.match(uri)
        when(match){ //contain (100 and 101)

            MATCH_RESERV ->{ //100

                return insertReserv(uri, values)
            }
        }
return null
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        var DB = SqlDbHelper(context).readableDatabase
        var cursor:Cursor? = null
        //TODO: uri that comes from (query) in (Add) Activity//
        var match = Matcher.match(uri)
        when (match){
            MATCH_RESERV ->{

      cursor = DB.query(StorageContract.TABLE_NAME, projection,selection,selectionArgs, null, null, sortOrder)
            }

            MATCH_RESERV_ID->{

                cursor= DB.query(StorageContract.TABLE_NAME, projection,
                        StorageContract._ID+"=?",
                        arrayOf(ContentUris.parseId(uri).toString()),
                        null,null,sortOrder)
            }
        }
// Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor
        cursor!!.setNotificationUri(context.contentResolver, uri)
       return cursor
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        var match = Matcher.match(uri)
        when(match){

            MATCH_RESERV_ID->{
                UpdateStorage(uri,values,StorageContract._ID+"=?",arrayOf(ContentUris.parseId(uri).toString()))

            }
        }
return 0
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        var database = SqlDbHelper(context).writableDatabase
        val match = Matcher.match(uri)
        var rowsDeleted:Int? = null
        when (match) {

            MATCH_RESERV -> {

                rowsDeleted = database.delete(StorageContract.TABLE_NAME, selection, selectionArgs)
            }
            MATCH_RESERV_ID -> {
                rowsDeleted = database.delete(StorageContract.TABLE_NAME,
                        StorageContract._ID + "=?", arrayOf(ContentUris.parseId(uri).toString()))
            }
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if(rowsDeleted !=null){
            context.contentResolver.notifyChange(uri,null)
        }
        return rowsDeleted!!
    }

    override fun getType(uri: Uri?): String {
        var match = Matcher.match(uri)
        when(match){
            MATCH_RESERV->  return StorageContract.CONTENT_LIST_TYPE
            MATCH_RESERV_ID-> return StorageContract.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknow url"+uri+"with match"+match)
        }
        return ""

    }
/////////insert///
     fun insertReserv(uri:Uri, values: ContentValues):Uri{
        //////////////////// TODO: connect with Database //
        var DB_Connect = SqlDbHelper(context).writableDatabase
        // return row id that inserted
        var insert_id = DB_Connect.insert(StorageContract.TABLE_NAME, null, values)

    // Notify all listeners that the data has changed for the pet content URI
    //connect between CursorLoader and ContentProvider
    context.contentResolver.notifyChange(uri,null)
        //take uri and id that inserted and return it to the ContentResolver//
        return ContentUris.withAppendedId(uri, insert_id)
    }
    //////////////////////////TODO: update/////////////////////////////
    private fun UpdateStorage(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?):Int{
        var DB = SqlDbHelper(context).writableDatabase
        var update = DB.update(StorageContract.TABLE_NAME, values, selection, selectionArgs)
        if(update !=0){
            context.contentResolver.notifyChange(uri,null)
        }
        return update

    }
}