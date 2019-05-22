package com.jtoru.project2.Fragments

import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class FragmentMainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragmentList = mutableListOf<Fragment>()


    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }


    override fun getItem(i: Int): Fragment {
        return mFragmentList[i]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}