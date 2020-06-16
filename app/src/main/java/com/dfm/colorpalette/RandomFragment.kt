package com.dfm.colorpalette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class RandomFragment : Fragment() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.random_fragment, container, false)

        viewPager = view.findViewById(R.id.viewpager) as ViewPager
        val adapter = ColorViewPagerAdapter(childFragmentManager)
        viewPager?.adapter = adapter

        tabLayout = view.findViewById(R.id.tabs) as TabLayout
        tabLayout?.setupWithViewPager(viewPager)

        return view
    }
}