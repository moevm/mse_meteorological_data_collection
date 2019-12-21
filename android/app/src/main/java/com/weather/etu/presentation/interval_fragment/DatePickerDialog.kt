package com.weather.etu.presentation.interval_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weather.etu.R
import com.weather.etu.base.getTimeInMil
import kotlinx.android.synthetic.main.fragment_date_picker.*
import java.util.*

class DatePickerDialog : BottomSheetDialogFragment() {

    companion object {
        enum class INSTANCE_TYPE(val key: Int) {
            START(0),
            END(1)
        }

        private const val EXTRA_INST = "extra.inst"

        fun instance(type: INSTANCE_TYPE): DatePickerDialog {
            return DatePickerDialog().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_INST, type.key)
                }
            }
        }
    }

    private val type: INSTANCE_TYPE by lazy {
        if (arguments!!.getInt(EXTRA_INST) == 0) INSTANCE_TYPE.START
        else INSTANCE_TYPE.END
    }

    private val navViewModel by lazy { ViewModelProviders.of(activity!!)[IntervalFragmentViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c = Calendar.getInstance()
        c.timeInMillis = if (type == INSTANCE_TYPE.START) {
            navViewModel.getRequestParams().startDateMil
        } else {
            navViewModel.getRequestParams().endDateMil
        }

        datePicker.updateDate(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )

        btn_submit.setOnClickListener {
            val q = Calendar.getInstance()
            q.timeInMillis = datePicker.getTimeInMil()
            if (type == INSTANCE_TYPE.START) {
                navViewModel.updateStartDate(q)
            } else {
                navViewModel.updateEndDate(q)
            }

            dismiss()
        }
    }
}