package com.weather.etu.presentation.interval_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.weather.core.remote.models.City
import com.weather.core.remote.models.Interval
import com.weather.core.remote.models.firebase.CountryFS
import com.weather.etu.R
import com.weather.etu.base.limitDropdownHeight
import com.weather.etu.base.onItemSelected
import com.weather.etu.presentation.BaseMainActivityFragment
import kotlinx.android.synthetic.main.fragment_interval.*
import java.util.*


class IntervalFragment : BaseMainActivityFragment<IntervalFragmentViewModel>() {
    companion object {
        const val CODE_REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 1
    }

    private var havePermissions: Boolean? = null

    override fun onResume() {
        super.onResume()
        if (!checkExternalStoragePermissions() && havePermissions == null) {
            requestExternalStoragePermissions()
        }
    }

    override val viewModel by lazy {
        ViewModelProviders
            .of(activity!!)[IntervalFragmentViewModel::class.java]
    }

    override val layoutId: Int
        get() = R.layout.fragment_interval


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_REQUEST_EXTERNAL_STORAGE_PERMISSIONS -> {
                havePermissions =
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            context!!,
                            "Разрешения на работу с файлами даны",
                            Toast.LENGTH_LONG
                        ).show()
                        true
                    } else {
                        Toast.makeText(
                            context!!,
                            "\"Разрешения на работу с файлами не даны",
                            Toast.LENGTH_LONG
                        ).show()
                        false
                    }
            }
        }
    }

    private fun requestExternalStoragePermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            CODE_REQUEST_EXTERNAL_STORAGE_PERMISSIONS
        )
    }

    private fun checkExternalStoragePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val countriesAdapter by lazy {
        ArrayAdapter<CountryFS>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
            }
    }

    private val areasAdapter by lazy {
        ArrayAdapter<String>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
            }
    }

    private val citiesAdapter by lazy {
        ArrayAdapter<City>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
            }
    }

    private val intervalsSpinner by lazy {
        ArrayAdapter<Interval>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
                addAll(Interval.DAY, Interval.MONTH, Interval.QUARTER)
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchCountries()
    }

    @SuppressLint("SetTextI18n")
    override val bindViews = { _: View ->
        countries_spinner.apply {
            limitDropdownHeight()
            setTitle("Выберите страну")
            adapter = countriesAdapter
            onItemSelected(viewModel::onCountrySelected)
        }

        areas_spinner.apply {
            limitDropdownHeight()
            setTitle("Выберите область")
            adapter = areasAdapter
            onItemSelected(viewModel::onAreaSelected)
        }

        cities_spinner.apply {
            limitDropdownHeight(500)
            setTitle("Выберите город")
            adapter = citiesAdapter
            onItemSelected(viewModel::onCitySelected)
        }

        intervals_spinner.apply {
            limitDropdownHeight(500)
            setTitle("Выберите интервал")
            adapter = intervalsSpinner
            onItemSelected(viewModel::onIntervalSelected)
        }

        cv_first_time.setOnClickListener {
            DatePickerDialog.instance(DatePickerDialog.Companion.INSTANCE_TYPE.START)
                .show(childFragmentManager, "")
        }

        cv_second_time.setOnClickListener {
            DatePickerDialog.instance(DatePickerDialog.Companion.INSTANCE_TYPE.END)
                .show(childFragmentManager, "")
        }

        btn_search.setOnClickListener { viewModel.onSubmit() }


        viewModel.countriesLD.observe(this, Observer {
            with(countriesAdapter) {
                clear()
                addAll(it)
            }
        })

        viewModel.areasLD.observe(this, Observer {
            with(areasAdapter) {
                clear()
                addAll(it)
            }
        })

        viewModel.citiesLD.observe(this, Observer {
            with(citiesAdapter) {
                clear()
                addAll(it)
            }
        })

        viewModel.startDateLD.observe(this, Observer {
            tv_time_start.text =
                "${it.get(Calendar.DAY_OF_MONTH)}/${it.get(Calendar.MONTH) + 1}/${it.get(Calendar.YEAR)}"
        })

        viewModel.endDateLD.observe(this, Observer {
            tv_time_finish.text =
                "${it.get(Calendar.DAY_OF_MONTH)}/${it.get(Calendar.MONTH) + 1}/${it.get(Calendar.YEAR)}"
        })

        viewModel.fileHistoryWeatherLD.observe(this, Observer {
            if (it != null) {
                Snackbar.make(
                    btn_search,
                    "Файл находится: ${it.absolutePath}",
                    Snackbar.LENGTH_LONG
                ).show()
                btn_search.visibility = View.VISIBLE
                search_progress_bar.visibility = View.GONE
            } else {
                btn_search.visibility = View.GONE
                search_progress_bar.visibility = View.VISIBLE
            }
        })
    }
}