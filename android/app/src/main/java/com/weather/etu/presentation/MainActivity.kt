package com.weather.etu.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.weather.etu.R
import com.weather.etu.base.BaseActivity
import com.weather.etu.presentation.chart_fragment.ChartFragment
import com.weather.etu.presentation.interval_fragment.IntervalFragment
import com.weather.etu.presentation.today_fragment.TodayFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainActivityViewModel>() {

    companion object {
        enum class STATE {
            TODAY,
            INTERVAL,
            CHART
        }
    }

    override val viewModel by lazy {
        ViewModelProviders.of(this)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.stateLiveData.observe(this, Observer {
            when (it) {
                Companion.STATE.TODAY -> view_pager.currentItem = 0
                Companion.STATE.INTERVAL -> view_pager.currentItem = 1
                Companion.STATE.CHART -> view_pager.currentItem = 2
            }
        })

        bindPager()
        bindNavigation()
    }

    private inline fun bindPager() {
        view_pager.adapter = ViewPagerAdapter()
        view_pager.offscreenPageLimit = 2
    }

    private inline fun bindNavigation() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_first -> viewModel.onTodayClicked()
                R.id.menu_second -> viewModel.onIntervalCLicked()
                R.id.menu_third -> viewModel.onChartClicked()
                else -> false
            }
        }
    }

    private inner class ViewPagerAdapter : FragmentPagerAdapter(
        supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TodayFragment()
                1 -> IntervalFragment()
                2 -> ChartFragment()
                else -> throw
                IllegalArgumentException("wrong position, expected 0..2, but found: $position")
            }
        }

        override fun getCount() = 3
    }
}
