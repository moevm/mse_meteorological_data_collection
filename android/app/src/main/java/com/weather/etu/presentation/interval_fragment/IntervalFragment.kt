package com.weather.etu.presentation.interval_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.weather.core.remote.models.Interval
import com.weather.core.remote.models.firebase.CountryFS
import com.weather.etu.R
import com.weather.etu.base.limitDropdownHeight
import com.weather.etu.base.onItemSelected
import com.weather.etu.presentation.BaseMainActivityFragment
import kotlinx.android.synthetic.main.fragment_interval.*
import java.util.*


class IntervalFragment : BaseMainActivityFragment<IntervalFragmentViewModel>() {

    override val viewModel by lazy {
        ViewModelProviders
            .of(activity!!)[IntervalFragmentViewModel::class.java]
    }

    override val layoutId: Int
        get() = R.layout.fragment_interval

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
        ArrayAdapter<Pair<String, String>>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
            }
    }

    private val intervalsSpinner by lazy {
        ArrayAdapter<Interval>(context!!, R.layout.item_spinner_default, arrayListOf())
            .apply {
                setDropDownViewResource(R.layout.item_spinner_opened)
                addAll(Interval.DAY, Interval.MONTH, Interval.SEASON, Interval.YEAR)
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
            adapter = countriesAdapter
            onItemSelected(viewModel::onCountrySelected)
        }

        areas_spinner.apply {
            limitDropdownHeight()
            adapter = areasAdapter
            onItemSelected(viewModel::onAreaSelected)
        }

        cities_spinner.apply {
            limitDropdownHeight(500)
            adapter = citiesAdapter
            onItemSelected(viewModel::onCitySelected)
        }

        intervals_spinner.apply {
            limitDropdownHeight(500)
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
    }
}