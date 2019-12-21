package com.weather.etu.presentation.statistic_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.weather.etu.R
import com.weather.etu.base.BaseActivity
import com.weather.etu.model.Statistic
import kotlinx.android.synthetic.main.activity_statistic.*

class StatisticActivity : BaseActivity<StatisticActivityViewModel>() {

    companion object{
        private const val EXTRA_NAME = "EXTRA_NAME"
        fun newIntent(context: Context, path:String): Intent {
            return Intent(context,StatisticActivity::class.java).apply {
                putExtra(EXTRA_NAME,path)
            }
        }
    }

    private lateinit var name:String

    override val viewModel by lazy {
        ViewModelProviders.of(this)[StatisticActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        name = intent.getStringExtra(EXTRA_NAME)!!

        viewModel.statistic.observe(this, Observer {
            it?.let {statistic->
                updateStatistic(statistic)
            }
        })

        viewModel.fetchHistoryWeather(name)
    }

    private fun updateStatistic(statistic:Statistic){
        Log.d("StatisticActivityLog",statistic.toString())
        statistic_average_temperature.text = statistic.average_temperature.toString()
        statistic_min_temperature.text = statistic.min_temperature.toString()
        statistic_max_temperature.text = statistic.max_temperature.toString()
        statistic_mod_temperature.text = statistic.mod_temperature.toString()

        statistic_average_pressure.text = statistic.average_pressure.toString()
        statistic_min_pressure.text = statistic.min_pressure.toString()
        statistic_max_pressure.text = statistic.max_pressure.toString()
        statistic_mod_pressure.text = statistic.mod_pressure.toString()

        statistic_average_wind.text = statistic.average_wind.toString()
        statistic_min_wind.text = statistic.min_wind.toString()
        statistic_max_wind.text = statistic.max_wind.toString()
        statistic_mod_wind.text = statistic.mod_wind.toString()
    }
}
