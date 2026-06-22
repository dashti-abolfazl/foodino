package com.example.foodino

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class AdapterTab(
    fm: FragmentManager,
    private val allfragment: ArrayList<Fragment>,
    private val alltab: ArrayList<String>
) : FragmentStatePagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int = allfragment.size

    override fun getItem(position: Int): Fragment =
        allfragment[position]

    override fun getPageTitle(position: Int): CharSequence =
        alltab[position]
}
