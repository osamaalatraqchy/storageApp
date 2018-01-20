package email.storage.osama.storageapp

import android.app.LoaderManager
import android.content.ContentUris
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import email.storage.osama.storageapp.tools.StorageContract
import kotlinx.android.synthetic.main.activity_show.*

class Show : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        val loader = 1
        var adapter:StorageCursorAdapter? = null
    }
/////////////////TODO: cursorLoader//////////////
    //TODO: create the CursorLoader and define the data that we need query from ContentProvider//
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
    val projection = arrayOf(
            StorageContract._ID,
            StorageContract.R_NAME,
            StorageContract.R_GENDER,
            StorageContract.R_TIME
    )
    //TODO: connect with contentResolver and query data with spicific Uri///
    return CursorLoader(this, StorageContract.CONTENT_URI
    ,projection,null,null,null)

    }

    //TODO: when loader has finished loading data, and has a cursor//
    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
       adapter!!.swapCursor(data)
    }

    //TODO: when current cursor is destroy
    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter!!.swapCursor(null)
    }
    /////////////////TODO: End cursorLoader//////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        //TODO: this in (oncreate()), thus initial the adapter with null, because (adapter) is update in
        //TODO-> (swapCursor) in onLoadFinished//
        adapter = StorageCursorAdapter(this, null)

        list.adapter = adapter
        ///////////////////////////////////////
        //TODO:send intent to EditorActivity class containes the id of item in database that click on it//
        //TODO: connect with in (Add) activity//
        list.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
            var intent = Intent(this,Add::class.java)
            var currentPetUri = ContentUris.withAppendedId(StorageContract.CONTENT_URI, id)
            intent.setData(currentPetUri)
            startActivity(intent)

        }
        ///////////////////////////////////


        //TODO: start the CursorLoader
        loaderManager.initLoader(loader,null,this)
    }
////////////////////TODO: maenue/////////////////////////////////////
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menue_show, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
     R.id.exit ->{
         moveTaskToBack(true)
         android.os.Process.killProcess(1)
         System.exit(1)
     }
            R.id.delete -> {
                var delete = contentResolver.delete(StorageContract.CONTENT_URI, null, null)
                if(delete ==0){
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(this, "delete fail",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(this, "Deleted success",
                            Toast.LENGTH_SHORT).show();

                }
            }



        }
        return true
    }
    ////////////////////TODO: End maenue/////////////////////////////////////
}
