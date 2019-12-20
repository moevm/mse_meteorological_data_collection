package com.weather.etu.model

import android.os.Build
import android.os.Environment
import android.util.Log
import au.com.bytecode.opencsv.CSVWriter
import com.weather.core.remote.models.ParseRequest
import com.weather.core.remote.models.history.HistoryWeather
import com.weather.etu.base.getYearFromMil
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception

class CsvFileManager {

     companion object{
         const val DIR = "HistoryWeather"
         const val EXTENSION = ".csv"
     }

     fun getFiles(): Single<List<File>> {
         return Single.create{
             val dir = Environment.getExternalStoragePublicDirectory(DIR).apply { mkdirs() }
             val files = dir.listFiles()
             it.onSuccess(files?.toList() ?: listOf())
         }
     }

     fun saveHistoryWeatherInFile(data: HistoryWeather,request: ParseRequest):Single<File>{
        return Single.create{emitter->
            val startYear = request.startDateMil.getYearFromMil()
            val endYear = request.endDateMil.getYearFromMil()
            val file = getFileFromExternalStorage(createName(request.cityName,startYear,endYear,request.interval.name))
            val writer = encodingSetting(file!!)
            data.historyWeather.forEach {
                val line = mutableListOf<String>()
                line.add(it.first)
                line.add(it.second.temperature ?: "-")
                line.add(it.second.pressure ?: "-")
                line.add(it.second.overcast ?: "-")
                line.add(it.second.phenomenon ?: "-")
                line.add(it.second.wind ?: "-")
                line.add(it.second.directionWind ?: "-")
                writer.writeNext(line.toTypedArray())
            }
            writer.close()
            emitter.onSuccess(file)
        }
    }

    private fun encodingSetting(file:File):CSVWriter{
        val os = FileOutputStream(file)
        os.write(239)
        os.write(187)
        os.write(191)
        return CSVWriter(OutputStreamWriter(os, "UTF-8"))
    }

    private fun createName(city:String,startYear:String,endYear:String,interval:String):String{
        return "$city($startYear-$endYear-$interval)$EXTENSION".replace("/","")
    }

    private fun getFileFromExternalStorage(nameFile:String): File? {
        Log.d("HistoryWeather",nameFile)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //TODO для android 10 делается по другому
            null
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(DIR).apply { mkdirs() }
            File(dir,nameFile).apply { createNewFile() }
        }
    }
}