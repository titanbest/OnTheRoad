package com.sergey.ontheroad.view.activity

import com.sergey.ontheroad.R
import com.sergey.ontheroad.view.base.BaseActivity
import com.sergey.ontheroad.view.fragments.MapsFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

//    private lateinit var viewModel: MainViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun fragment() = MapsFragment()
}
