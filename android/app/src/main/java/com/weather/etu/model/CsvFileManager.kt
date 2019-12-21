package com.weather.etu.model

import android.os.Build
import android.os.Environment
import android.util.Log
import au.com.bytecode.opencsv.CSVWriter
import com.weather.core.remote.models.ParseRequest
import com.weather.core.remote.models.history.DataWeather
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
import android.widget.Toast
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader


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

     fun deleteFile(name:String):Completable{
         return Completable.create {
             val file = getFileFromExternalStorage(name)
             file?.delete()
             it.onComplete()
         }
     }

     fun fetchHistoryWeatherFromFile(name:String):Single<HistoryWeather>{
         return Single.create {
             val file = getFileFromExternalStorage(name)!!
             val historyWeather = mutableListOf<Pair<String, DataWeather>>()
             val reader = CSVReader(FileReader(file.absolutePath))
             var nextLine: Array<String>? = reader.readNext()
             while (nextLine != null) {
                 val date = nextLine[0]
                 val dataWeather = DataWeather(
                     nextLine[1],
                     nextLine[2],
                     nextLine[3],
                     nextLine[4],
                     nextLine[5],
                     nextLine[6]
                 )
                 historyWeather.add(Pair(date,dataWeather))
                 nextLine = reader.readNext()
             }
             it.onSuccess(HistoryWeather(historyWeather))
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