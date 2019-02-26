package ua.varandas.coordinator.adapters


import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ua.varandas.coordinator.R
import ua.varandas.coordinator.fragments.DefineFragment
import ua.varandas.coordinator.fragments.OutputFragment
import ua.varandas.coordinator.fragments.PreviewFragment

class TabPagerAdapter(fm: FragmentManager, ctx : Context) : FragmentPagerAdapter(fm) {

    private val define = ctx.resources.getString(R.string.define)
    private val preview = ctx.resources.getString(R.string.preview)
    private val output = ctx.resources.getString(R.string.output)

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> DefineFragment()
        1 -> PreviewFragment()
        2 -> OutputFragment()
        else -> null
    }
    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> define
        1 -> preview
        2 -> output
        else -> ""
    }
    override fun getCount(): Int = 3
}