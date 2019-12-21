package com.weather.etu.presentation.chart_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.weather.core.remote.models.history.DataWeather
import com.weather.etu.R
import com.weather.etu.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chart.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter


class ChartActivity : BaseActivity<ChartActivityViewModel>() {

    companion object{
        private const val EXTRA_NAME = "EXTRA_NAME"
        fun newIntent(context:Context,path:String): Intent {
            return Intent(context,ChartActivity::class.java).apply {
                putExtra(EXTRA_NAME,path)
            }
        }
    }

    private lateinit var name:String

    override val viewModel by lazy {
        ViewModelProviders.of(this)[ChartActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        name = intent.getStringExtra(EXTRA_NAME)!!

        viewModel.historyWeather.observe(this, Observer {
            it?.let {history->
                updateChart(history.historyWeather)
            }
        })

        viewModel.fetchHistoryWeather(name)
    }

    private fun updateChart(historyWeather: List<Pair<String, DataWeather>>){
        val lineDataSet = LineDataSet(dataValues(historyWeather),"").apply {
            color = ContextCompat.getColor(this@ChartActivity, R.color.colorPrimary)
            valueTextColor = ContextCompat.getColor(this@ChartActivity, R.color.colorPrimaryDark)
            valueTextSize = 10f
            setDrawCircles(true)
        }

        with(chart_line_chart.xAxis){
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return historyWeather[value.toInt()].first
                }
            }
        }

        chart_line_chart.axisRight.isEnabled = false
        chart_line_chart.axisLeft.granularity = 1f

        with(chart_line_chart){
            legend.isEnabled = false
            data = LineData(lineDataSet)
            animateX(2500)
            invalidate()
        }
    }

    private fun dataValues(historyWeather: List<Pair<String,DataWeather>>):List<Entry>{
        val entries = mutableListOf<Entry>()
        for(i in 0 until historyWeather.size){
            historyWeather[i].second.temperature?.let {
                if(it!="-")
                    entries.add(Entry(i.toFloat(),it.toFloat() ))
            }
        }
        return entries
    }
}
