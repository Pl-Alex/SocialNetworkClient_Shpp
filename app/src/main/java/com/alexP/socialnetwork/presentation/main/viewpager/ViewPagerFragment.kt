package com.alexP.socialnetwork.presentation.main.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexP.socialnetwork.databinding.FragmentViewpagerBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexP.socialnetwork.presentation.main.contacts.ContactsFragment
import com.alexP.socialnetwork.presentation.main.myprofile.MyProfileFragment

class ViewPagerFragment : BaseFragment<FragmentViewpagerBinding>() {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentViewpagerBinding {
        return FragmentViewpagerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = ViewPagerAdapter(this)
    }
}

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MyProfileFragment()
        1 -> ContactsFragment()
        else -> {
            throw IllegalArgumentException("Invalid position")
        }
    }
}