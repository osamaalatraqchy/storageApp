package email.storage.osama.storageapp

import android.app.Activity
import android.app.LoaderManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import email.storage.osama.storageapp.R.id.spinner
import email.storage.osama.storageapp.tools.StorageContract
import email.storage.osama.storageapp.tools.StorageContract.R_TIME
import kotlinx.android.synthetic.main.activity_add.*

class Add : Activity(), LoaderManager.LoaderCallbacks<Cursor> {
    var currentPetUri: Uri? =null
    var time = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add)
////////////////////////////////////////////////////////////////////////////////////////////////////////
        // TODO: Examine the intent that was used to launch this activity,
        // TODO: in order to figure out if we're creating a new pet or editing an existing one.
        var intent = getIntent()
        //TODO: act the uri that comes from (show) activity, that spicific for one row in DB//
          currentPetUri = intent.data
        // If the intent DOES NOT contain a pet content URI, then we know that we are creating a new pet.
        if(currentPetUri ==null){
            add.setText("اضافة")
            delete.visibility = View.GONE
        }else{
            add.setText("تعديل")
            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(0, null, this@Add)
        }
        ////////////////////////////////////////////////////////////////////////////////


        SpinnerInit()

        ///////////////////////
    }


    //////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
// Since the editor shows all pet attributes, define a projection that contains
// all columns from the pet table
        val projection = arrayOf(
                StorageContract._ID,
                StorageContract.R_NAME,
                StorageContract.R_GENDER,
                StorageContract.R_TIME,
                StorageContract.R_ADRESS,
                StorageContract.R_PHONE

        )
        //TODO: connect with contentResolver and query data with spicific Uri///
        return CursorLoader(this, currentPetUri
                ,projection,null,null,null)

    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if(cursor.moveToFirst()){
            // TODO: same as those in {@StorageCursorAdapter} class//
            var cname = cursor.getColumnIndex(StorageContract.R_NAME)
            var caddress = cursor.getColumnIndex(StorageContract.R_ADRESS)
            var cphone = cursor.getColumnIndex(StorageContract.R_PHONE)
            var cgender = cursor.getColumnIndex(StorageContract.R_GENDER)
            var ctime = cursor.getColumnIndex(StorageContract.R_TIME)

            var readName = cursor.getString(cname)
            var readaddress = cursor.getString(caddress)
            var readphone = cursor.getString(cphone)
           // var readgender = cursor.getString(cgender)
           // var readtime = cursor.getString(ctime)

            r_name.setText(readName)
            r_address.setText(readaddress)
            r_phone.setText(readphone)
           // r_gender.checkedRadioButtonId

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // If the loader is invalidated, clear out all the data from the input fields.
        r_name.setText("")
        r_address.setText("")
        r_phone.setText("")
        spinner.setSelection(0)
        r_gender.check(0)
    }


    ////////////////////////////////TODO://spinner initial//////////
    private fun SpinnerInit(){
// Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        var SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item)
        // Specify dropdown layout style - simple list view with 1 item per line
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = SpinnerAdapter

        ///on item select//
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val itemSelected = parent!!.getItemAtPosition(position) as String


                if (!TextUtils.isEmpty(itemSelected)) {
                    if (itemSelected == getString(R.string.mor)) {
                        time = StorageContract.TIME_MORNING
                    } else if (itemSelected == getString(R.string.eve)) {
                        time = StorageContract.TIME_NIGHT
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                time = StorageContract.TIME_MORNING
            }

        }
    }
    //////////////////////////TODO: End Spinner/////////////////////////////////////////////////

    //TODO: Insert new Reservation with contentResolver//

     fun insert_reservation(view:View){
//TODO: int value//
        var r_name = r_name.text.toString().trim()
        var r_address = r_address.text.toString().trim()
        var r_phone = r_phone.text.toString().trim()
         var r_gender = gender()
//TODO: put value as key(from table column) and values(from UI)//
        var values = ContentValues()
        values.put(StorageContract.R_NAME, r_name)
        values.put(StorageContract.R_ADRESS, r_address)
        values.put(StorageContract.R_PHONE, r_phone)
        values.put(StorageContract.R_GENDER, r_gender)
        values.put(StorageContract.R_TIME, time)
         if (currentPetUri == null) {
//TODO: insert data from ((UI)) to DB using ((contentResolver)) -
// TODO -> that connect between ((StorageProvider->insert())) and ((UI))
             var inserted = contentResolver.insert(StorageContract.CONTENT_URI, values)
             if (inserted != null) {
                 Toast.makeText(this, "insert success", Toast.LENGTH_SHORT).show()
             }
         }else{
//TODO: (puth the Selection and selectionargs here)//
             var rowAffected = contentResolver.update(currentPetUri, values, null, null)

             if (rowAffected == null) {
                 // If the new content URI is null, then there was an error with insertion.
                 Toast.makeText(this, "error in update",
                         Toast.LENGTH_SHORT).show()
             } else {
                 // Otherwise, the insertion was successful and we can display a toast.
                 Toast.makeText(this, "Update success",
                         Toast.LENGTH_SHORT).show()
             }
         }

    }
    ////////////////////delete button//////////////////////

    fun delete(view:View) {
// Only perform the delete if this is an existing pet.
        if (currentPetUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            var rowdeleted = contentResolver.delete(currentPetUri, null, null)
            // Show a toast message depending on whether or not the delete was successful.
            if (rowdeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "delete fail",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Deleted success",
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish()
    }

    ////////////////////////////////////////

   private fun gender():String{
        //(checked) variable has the id from checked radio in radioGroup
        var checked = r_gender.checkedRadioButtonId
        //(rb) varable has radioButton that checkedd
        var rb = findViewById<RadioButton>(checked)
        return rb.text.toString()
    }
}
