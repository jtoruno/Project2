package com.jtoru.project2.Actitivies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.jtoru.project2.Fragments.*
import com.jtoru.project2.R

class SearchActivity : AppCompatActivity() {
    private lateinit var viewPagerSearch: ViewPager
    private var TAG:String = "SearchActivity"
    private lateinit var mFragmentSearchAdapter: FragmentSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.title = "Search!"

        //Fragments
        mFragmentSearchAdapter = FragmentSearchAdapter(supportFragmentManager)
        viewPagerSearch = findViewById(R.id.viewpagerSearch)
        setupViewPager(viewPagerSearch)
        var tabLayout: TabLayout = findViewById(R.id.tabsSearch)
        tabLayout.setupWithViewPager(viewPagerSearch)
    }

    fun setupViewPager(viewPager:ViewPager)
    {
        val adapter = FragmentFriendsAdapter(supportFragmentManager)
        adapter.addFragment(FragmentPeopleSearch(),"People")
        adapter.addFragment(FragmentPostsSearch(),"Posts")
        viewPager.adapter = adapter


    }
}
