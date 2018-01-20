package email.storage.osama.storageapp.tools

import android.content.ContentResolver
import android.content.UriMatcher
import android.net.Uri
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import email.storage.osama.storageapp.R

//Contract to save the table name and column//
object StorageContract: BaseColumns{

val TABLE_NAME = "reservation"
    val _ID = BaseColumns._ID
    val R_NAME = "r_name"
    val R_PHONE = "r_phone"
    val R_ADRESS = "r_address"
    val R_TIME = "r_time"
    val R_GENDER = "r_gender"
    
    //

    val TIME_MORNING = "morning"
    val TIME_NIGHT = "evening"

    ///////////////////////////////TODO: Uri///////////////
    val URI_AUTH = "email.storage.osama.storageapp"
    val BASE_URI = Uri.parse("content://"+ URI_AUTH)
    val URI_TABLE = "reservation/"
    val CONTENT_URI:Uri = Uri.withAppendedPath(BASE_URI, URI_TABLE) //final Uri to use by ContentProvider//
    ///////////////////////////////////////////////////////////////////////

    //* The MIME type of the {@link #CONTENT_URI} for a list of pets.
    val CONTENT_LIST_TYPE =  ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_AUTH + "/" + URI_TABLE
    //The MIME type of the {@link #CONTENT_URI} for a single pet.
    val CONTENT_ITEM_TYPE =  ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + URI_AUTH + "/" + URI_TABLE

}
