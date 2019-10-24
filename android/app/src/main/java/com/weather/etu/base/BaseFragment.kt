package com.weather.etu.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment<T: BaseViewModel>: Fragment() {
    protected abstract val layoutId: Int

    protected abstract val viewModel: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorLivaData.observe(this, Observer {
            showErrorSnackbar(view, it)
        })
        bindViews?.invoke(view)
    }

    protected open val bindViews: ((View) -> Unit)? = null
}