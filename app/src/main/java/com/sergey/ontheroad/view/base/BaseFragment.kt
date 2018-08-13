package com.sergey.ontheroad.view.base

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.appContext
import com.sergey.ontheroad.extension.viewContainer
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment(private val layoutId: Int) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected var binding: ViewDataBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding?.root ?: inflater.inflate(layoutId, container, false)
//        return inflater.inflate(layoutId, container, false)
    }

    open fun onBackPressed(): Boolean {
        return true
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun notify(message: String?) = message?.let {
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()
    } ?: Unit

    internal fun notifyWithAction(message: String, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary))
        snackBar.show()
    }
}
