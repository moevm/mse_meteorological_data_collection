package com.weather.etu.presentation.files_fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.etu.BuildConfig
import com.weather.etu.R
import com.weather.etu.model.State
import com.weather.etu.presentation.BaseMainActivityFragment
import kotlinx.android.synthetic.main.fragment_files.*
import java.io.File
import java.net.URLConnection


class FilesFragment : BaseMainActivityFragment<FilesFragmentViewModel>() {

    private val adapter by lazy { FilesAdapter() }

    override val viewModel by lazy {
        ViewModelProviders.of(this)[FilesFragmentViewModel::class.java]
    }

    override val layoutId: Int
        get() = R.layout.fragment_files

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFiles()
    }

    override val bindViews = { _: View ->

        adapter.attachCallback(object : com.weather.etu.base.BaseAdapter.BaseAdapterCallback<File>{

            override fun onItemClick(model: File, view: View) {
                openFile(model)
            }

            override fun onLongClick(model: File, view: View): Boolean {
                return false
            }
        })

        with(files_recycler_view){
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@FilesFragment.adapter
        }

        viewModel.state.observe(this,androidx.lifecycle.Observer {state->
            when(state){
                is State.LoadingState -> {
                    files_progress_bar.visibility = View.VISIBLE
                    files_recycler_view.visibility = View.INVISIBLE
                    files_empty_data.visibility = View.INVISIBLE
                }
                is State.NoItemsState -> {
                    files_empty_data.visibility = View.VISIBLE
                    files_recycler_view.visibility = View.INVISIBLE
                    files_progress_bar.visibility = View.INVISIBLE
                }
                is State.LoadedState<*> -> {
                    adapter.updateItems(state.data.map { it as File })
                    files_recycler_view.visibility = View.VISIBLE
                    files_empty_data.visibility = View.INVISIBLE
                    files_progress_bar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun openFile(file:File){
        file.setReadable(true)
        val path = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", file)
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(path, mimeType)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }.let {
            try {
                startActivity(it)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context!!, "There's no program to open this file", Toast.LENGTH_LONG).show()
            }
        }
    }
}
