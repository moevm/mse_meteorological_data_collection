package com.weather.etu.presentation.files_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weather.etu.R
import com.weather.etu.base.BaseAdapter
import kotlinx.android.synthetic.main.item_list_file.view.*
import java.io.File
import java.text.SimpleDateFormat


class FilesAdapter: BaseAdapter<File>()  {

    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<File> {
        return FileViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_file, parent, false))
    }

    private inner class FileViewHolder(v: View): BaseAdapter.BaseViewHolder<File>(v) {

        override fun bind(model: File) {
            with(itemView){
                if(adapterPosition!=0){
                    top_line.visibility = View.INVISIBLE
                }
                item_list_name.text = model.name
                item_list_meta.text = "${getSize(model)} | ${getDate(model)}"
            }
        }
    }


    private fun getSize(file:File):String{
        val size = file.length().toFloat()
        return if((size/1000).toInt()!=0){
            "${Math.round(size/1000 * 100.0) / 100.0}KB"
        }else{
            "${Math.round(size * 100.0) / 100.0}B"
        }
    }

    private fun getDate(file:File):String{
        return simpleDateFormat.format(file.lastModified())
    }
}