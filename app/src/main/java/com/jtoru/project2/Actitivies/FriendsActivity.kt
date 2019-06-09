package com.jtoru.project2.Actitivies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import com.jtoru.project2.Fragments.*
import com.jtoru.project2.R

class FriendsActivity : AppCompatActivity() {
    private lateinit var viewPagerFriends: ViewPager
    private var TAG:String = "MainActivity"
    private var id: String = ""
    private lateinit var mFragmentFriendsAdapter: FragmentFriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        supportActionBar?.title = "Friends"
        id = this.intent.getStringExtra("id")
        //Fragments
        mFragmentFriendsAdapter = FragmentFriendsAdapter(supportFragmentManager)
        viewPagerFriends = findViewById(R.id.viewpagerFriends)
        setupViewPager(viewPagerFriends)
        var tabLayout: TabLayout = findViewById(R.id.tabsFriends)
        tabLayout.setupWithViewPager(viewPagerFriends)
    }
    fun setupViewPager(viewPager:ViewPager)
    {
        val adapter = FragmentFriendsAdapter(supportFragmentManager)
        adapter.addFragment(FragmentAllFriends(),"ALL")
        adapter.addFragment(FragmentMutualFriends(),"MUTUAL")
        viewPager.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }
    fun getId():String{
        return this.id
    }
}
