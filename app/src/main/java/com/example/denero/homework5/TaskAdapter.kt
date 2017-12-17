package com.example.denero.homework5

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.task_layout.view.*

/**
 * Created by DENERO on 17.12.2017.
 */
class TaskAdapter(var data:ArrayList<Task>,var list:RecyclerView):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var position:Int = 0
    lateinit var dbHelper:DBHelper
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskViewHolder =
            TaskViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.task_layout,null))


    override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {
        this.position = data[position].id
        dbHelper = DBHelper(holder?.v!!.context)
        var sPref = holder?.v!!.context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
        if(data[position].complete<1 || sPref.getBoolean("showAll",true)){
                       holder?.v?.setOnLongClickListener {
                var builder = AlertDialog.Builder(holder.v.context)
                builder.setPositiveButton("Update",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        showDialogUpdate(holder.v.context,position)
                    }
                })
                        .setNegativeButton("Delete",object :DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                dbHelper.DeleteTask(data[position].id,data,list)
                            }
                        })
                builder.create()
                builder.show()
                true
            }
            holder?.id?.setText((position+1).toString())
            holder?.title?.setText(data[position].title)
            holder?.description?.setText(data[position].description)
            var temCheck = if(data[position].complete>0)true else false
            holder?.complete?.isChecked = temCheck
            holder?.complete?.setOnClickListener {
                temCheck = !temCheck
                dbHelper.checkedTask(data[position].id,if(temCheck)1 else 0,data,list)
                holder?.complete?.isChecked = temCheck
            }
        }
        else{

        }


    }



    class TaskViewHolder(var v: View):RecyclerView.ViewHolder(v){
        var id = v.id_task
        var title = v.title_task
        var description = v.description_task
        var complete = v.complete_task


    }
    fun showDialogUpdate(context: Context,position: Int){
        var builder = AlertDialog.Builder(context)
        var inflater = LayoutInflater.from(context)
        var linearLayout = inflater.inflate(R.layout.create_dialog,null)
        builder.setView(linearLayout)
                .setPositiveButton("OK",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var edtTitle = linearLayout.findViewById<EditText>(R.id.edt_input_title)
                        var edtDescr = linearLayout.findViewById<EditText>(R.id.input_description)
                        dbHelper.UpdateTask(data[position].id,edtTitle.text.toString(),edtDescr.text.toString(),data,list)
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