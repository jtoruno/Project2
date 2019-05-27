package com.jtoru.project2.Fragments

import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentFriendsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitle = mutableListOf<String>()


    fun addFragment(fragment: Fragment, title:String) {
        mFragmentList.add(fragment)
        mFragmentTitle.add(title)
    }


    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle[position]
    }


    override fun getItem(i: Int): Fragment {
        return mFragmentList[i]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}