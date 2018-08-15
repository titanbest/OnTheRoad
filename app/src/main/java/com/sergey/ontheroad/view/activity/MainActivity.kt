package com.sergey.ontheroad.view.activity

import android.os.Bundle
import com.sergey.ontheroad.R
import com.sergey.ontheroad.view.base.BaseActivity
import com.sergey.ontheroad.view.fragments.MapsFragment
import com.sergey.ontheroad.viewmodel.MainViewModel

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun fragment() = MapsFragment()
}
