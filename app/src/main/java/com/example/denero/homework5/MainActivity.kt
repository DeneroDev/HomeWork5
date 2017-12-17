package com.example.denero.homework5

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences.Editor



class MainActivity : AppCompatActivity() {
    lateinit var dbHelper:DBHelper
    lateinit var data:ArrayList<Task>
    lateinit var list:RecyclerView
    var showAll=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        dbHelper = DBHelper(this)
   //     ClearDB()

        list = listX
        data = dbHelper.initData()
        list.layoutManager = LinearLayoutManager(applicationContext)

        list.adapter = TaskAdapter(data,list)
        list.adapter.notifyDataSetChanged()


        fab.setOnClickListener { view ->
            Toast.makeText(applicationContext,"press",Toast.LENGTH_LONG).show()
            showDialogAdd()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_complete -> {
                var sPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val ed = sPref.edit()
                ed.putBoolean("showAll",true)
                ed.commit()
                dbHelper.NotifyData(data,list)
                true}
            R.id.action_not_complete -> {var sPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val ed = sPref.edit()
                ed.putBoolean("showAll",false)
                ed.commit()
                dbHelper.NotifyData(data,list)
                true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDialogAdd(){
        var builder = AlertDialog.Builder(this)
        var inflater = this.layoutInflater
        var linearLayout = inflater.inflate(R.layout.create_dialog,null)
        builder.setView(linearLayout)
                .setPositiveButton("OK",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var edtTitle = linearLayout.findViewById<EditText>(R.id.edt_input_title)
                        var edtDescr = linearLayout.findViewById<EditText>(R.id.input_description)
                        dbHelper.AddTask(edtTitle.text.toString(),edtDescr.text.toString())
                        dbHelper.NotifyData(data,list)
                    }
                })
                .setNegativeButton("Cancel",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.cancel()
                    }
                })

        builder.create()
        builder.show()
    }




}
