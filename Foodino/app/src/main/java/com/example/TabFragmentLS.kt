package com.example.foodino

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodino.databinding.ActivityTabFragmentLsBinding

class TabFragmentLS : AppCompatActivity() {

    lateinit var b :ActivityTabFragmentLsBinding
    var array_fragment = ArrayList<Fragment>()
    var array_tab = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTabFragmentLsBinding.inflate(layoutInflater)
        setContentView(b.root)
        set_data()
        set_fragment()
        var d = AdapterTab(supportFragmentManager,array_fragment,array_tab)
        b.apply {
            viewPager.adapter = d
            tab.setupWithViewPager(viewPager)
        }
    }
    fun set_data(){
        array_tab.add("Login")
        array_tab.add("Signup")
    }
    fun set_fragment(){
        array_fragment.add(FragmentLogin())
        array_fragment.add(FragmentSignup())
    }
}