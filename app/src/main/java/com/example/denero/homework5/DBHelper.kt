package com.example.denero.homework5

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.provider.SyncStateContract.Helpers.update
import android.R.attr.name



/**
 * Created by DENERO on 17.12.2017.
 */
class DBHelper(var context:Context):SQLiteOpenHelper(context,"myDB",null,1) {


    override fun onCreate(p0: SQLiteDatabase?) {
        Log.d("SPAWN:", "--- onCreate database ---");
        p0?.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "description text,"
                + "complete integer"+");")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun NotifyData(data:ArrayList<Task>,list:RecyclerView){
        var data:ArrayList<Task>
        var db = this.readableDatabase
        var c: Cursor = db.query("mytable",null,null,null,null,null,null)
        c.moveToFirst()
        if( c != null && c.moveToFirst() ) {
            Log.d("KOL_VO:", c.count.toString())
            val idColIndex = c.getColumnIndex("id")
            val titleColIndex = c.getColumnIndex("title")
            val descrColIndex = c.getColumnIndex("description")
            val complColIndex = c.getColumnIndex("complete")
            data = ArrayList<Task>()
            var i:Int = 0
            var sPref = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
            while (i<c.count){
                if(c.getInt(complColIndex)<1 || sPref.getBoolean("showAll",true)){
                    data.add(Task(
                            c.getInt(idColIndex),
                            c.getString(titleColIndex),
                            c.getString(descrColIndex),
                            c.getInt(complColIndex)
                    ))
                    c.moveToPosition(i+1)
                    i++
                }
                else
                { c.moveToPosition(i+1)
                    i++}
            }
            list.adapter = TaskAdapter(data,list)
            list.adapter.notifyDataSetChanged()
            c.close()
            db.close()
        }
    }

    fun ClearDB(){
        var db: SQLiteDatabase = this.writableDatabase
        db.delete("mytable",null,null)
    }

    fun AddTask(title:String,descr:String){
        var cv = ContentValues()
        var db:SQLiteDatabase = this.writableDatabase
        cv.put("title",title)
        cv.put("description",descr)
        cv.put("complete",false)
        var id = db.insert("mytable",null,cv)
        Log.d("NEW ID:",id.toString())
        db.close()
    }

    fun UpdateTask(id:Int,title: String,descr: String,data:ArrayList<Task>,list:RecyclerView){
        var cv = ContentValues()
        var db: SQLiteDatabase = this.writableDatabase
        cv.put("title", title)
        cv.put("description", descr)
        // обновляем по id
        db.update("mytable", cv, "id = "+id,null)
        db.close()
        NotifyData(data,list)
    }

    fun DeleteTask(id:Int,data:ArrayList<Task>,list:RecyclerView){
        var cv = ContentValues()
        var db: SQLiteDatabase = this.writableDatabase
        db.delete("mytable","id = "+id,null)
        db.close()
        NotifyData(data,list)
    }

    fun initData(): ArrayList<Task> {
        var data:ArrayList<Task>
        var db = this.readableDatabase
        var c:Cursor = db.query("mytable",null,null,null,null,null,null)
        c.moveToFirst()
        if( c != null && c.moveToFirst() ){
            Log.d("KOL_VO:",c.count.toString())
            val idColIndex = c.getColumnIndex("id")
            val titleColIndex = c.getColumnIndex("title")
            val descrColIndex = c.getColumnIndex("description")
            val complColIndex = c.getColumnIndex("complete")
           var sPref = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
            data = ArrayList<Task>()
            var i:Int = 0
            while (i<c.count){
             if(c.getInt(complColIndex)<1 || sPref.getBoolean("showAll",true)){
                 data.add(Task(
                         c.getInt(idColIndex),
                         c.getString(titleColIndex),
                         c.getString(descrColIndex),
                         c.getInt(complColIndex)
                 ))
                 c.moveToPosition(i+1)
                 i++
                }
                else
             { c.moveToPosition(i+1)
                    i++}
            }
            c.close()
            db.close()
        }
        else
            data = ArrayList<Task>()

        return data

    }

    fun checkedTask(id:Int,check:Int,data:ArrayList<Task>,list:RecyclerView){
        var cv = ContentValues()
        var db: SQLiteDatabase = this.writableDatabase
        cv.put("complete",check)
        // обновляем по id
        db.update("mytable", cv, "id = "+id,null)
        db.close()
        NotifyData(data,list)
    }
}