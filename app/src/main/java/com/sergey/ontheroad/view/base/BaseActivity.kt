package com.sergey.ontheroad.view.base

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.inTransaction
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity(private val layoutId: Int) : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        fragment()?.let {
            if ((supportFragmentManager.findFragmentById(R.id.fragmentContainer) as BaseFragment).onBackPressed()) super.onBackPressed()
        }
    }

    private fun addFragment(savedInstanceState: Bundle?) = savedInstanceState ?: fragment()?.let {
        supportFragmentManager.inTransaction { add(R.id.fragmentContainer, it) }
    }

    abstract fun fragment(): BaseFragment?
}