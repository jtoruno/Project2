package com.jtoru.project2.Actitivies

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.Menu
import com.jtoru.project2.Fragments.*
import com.jtoru.project2.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private var TAG:String = "MainActivity"
    private lateinit var mFragmentMainAdapter: FragmentMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "TinderTec"
        //Fragments
        mFragmentMainAdapter = FragmentMainAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)
        var tabLayout:TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_art_track_black_24dp)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_people_black_24dp)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_notifications_none_black_24dp)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_menu_black_24dp)
        tabLayout.getTabAt(0)?.icon?.setTint(resources.getColor(R.color.colorPrimary, null))
        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                 p0?.icon?.setTint(resources.getColor(R.color.CustomGray, null))
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.icon?.setTint(resources.getColor(R.color.colorPrimary, null))
            }
        })
    }

    fun setupViewPager(viewPager:ViewPager)
    {
        val adapter = FragmentMainAdapter(supportFragmentManager)
        adapter.addFragment(FragmentTimeline())
        adapter.addFragment(FragmentFriends())
        adapter.addFragment(FragmentNotifications())
        adapter.addFragment(FragmentProfile())
        viewPager.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }
}
