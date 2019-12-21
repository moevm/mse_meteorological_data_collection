package com.weather.etu.presentation.files_fragment

import androidx.lifecycle.MutableLiveData
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.etu.model.CsvFileManager
import com.weather.etu.model.State
import javax.inject.Inject

class FilesFragmentViewModel:BaseViewModel() {

    @Inject
    lateinit var csvFileManager: CsvFileManager

    val state = MutableLiveData<State>()

    init {
        App.component.inject(this)
        state.postValue(State.LoadingState())
    }

    fun fetchFiles(){
        state.postValue(State.LoadingState())
        disposable.add(
            csvFileManager
                .getFiles()
                .safeSubscribe{
                    if(it.isEmpty()){
                        state.postValue(State.NoItemsState())
                    }else{
                        state.postValue(State.LoadedState(it))
                    }
                }
        )
    }

    fun deleteFile(name:String){
        disposable.add(
            csvFileManager.deleteFile(name)
                .safeSubscribe {
                    fetchFiles()
                }
        )
    }

}