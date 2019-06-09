package com.jtoru.project2.Actitivies


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.jtoru.project2.Fragments.*
import com.jtoru.project2.R




class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private var TAG:String = "MainActivity"
    private lateinit var mFragmentMainAdapter: FragmentMainAdapter
    private lateinit var tabLay:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "TinderTec"
        //Fragments
        mFragmentMainAdapter = FragmentMainAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)
        tabLay = findViewById(R.id.tabs)
        tabLay.setupWithViewPager(viewPager)
        tabLay.getTabAt(0)?.setIcon(R.drawable.ic_art_track_black_24dp)
        tabLay.getTabAt(1)?.setIcon(R.drawable.ic_people_black_24dp)
        tabLay.getTabAt(2)?.setIcon(R.drawable.ic_notifications_none_black_24dp)
        tabLay.getTabAt(3)?.setIcon(R.drawable.ic_menu_black_24dp)
        tabLay.getTabAt(0)?.icon?.setTint(resources.getColor(R.color.colorPrimary, null))
        tabLay.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.search_action)
        {
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
            return true
        }
        else
        {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if(tabLay.selectedTabPosition == 0)
        {
            val dialogBack = AlertDialog.Builder(this)
            dialogBack.setTitle("Exit")
            dialogBack.setMessage("Do you want to close session?")
            dialogBack.setPositiveButton("Yes"){
                    dialog,_ ->
                dialog.dismiss()
                finish()
                AuthUI.getInstance().signOut(this)
                finishAffinity()
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            }
            dialogBack.setNegativeButton("No"){
                    dialog,_ ->
                dialog.dismiss()
            }
            val d = dialogBack.create()
            d.show()
        }
        else
        {
            viewPager.currentItem = 0
        }
    }

}
