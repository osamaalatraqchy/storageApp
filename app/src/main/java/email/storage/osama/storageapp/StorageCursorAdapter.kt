package email.storage.osama.storageapp

import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import email.storage.osama.storageapp.tools.StorageContract

class StorageCursorAdapter( context:Context,  cursor:Cursor?):CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
      return LayoutInflater.from(context).inflate(R.layout.show_list, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {

        var name = view.findViewById<TextView>(R.id.name)
        var address = view.findViewById<TextView>(R.id.address)
        var phone = view.findViewById<TextView>(R.id.phone)

        var cname = cursor.getColumnIndex(StorageContract.R_NAME)
        var caddress = cursor.getColumnIndex(StorageContract.R_GENDER)
        var ctime = cursor.getColumnIndex(StorageContract.R_TIME)

        var readName = cursor.getString(cname)
        var readaddress = cursor.getString(caddress)
        var readtime = cursor.getString(ctime)

        name.text = readName
        address.text = readaddress
        phone.text = readtime

    }
}