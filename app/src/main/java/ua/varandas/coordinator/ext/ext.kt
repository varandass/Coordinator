package ua.varandas.coordinator.ext

import android.support.design.widget.TabLayout
import android.support.v4.app.*
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import org.jetbrains.anko.dip
import kotlin.math.PI


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun EditText.asFloat() = if (text.isEmpty()) 0f else text.toString().toFloat()

fun toRad(deg: Float) = (deg * PI) / 180
fun toDeg(rad: Float) = (rad * 180) / PI


fun TabLayout.tabTextSize(dip: Float) {
    val field = TabLayout::class.java.getDeclaredField("mTabTextSize")
    field.isAccessible = true
    field.set(this, dip(dip))
}

fun TabLayout.tabBackgroundResource(res: Int) {
    val field = TabLayout::class.java.getDeclaredField("mTabBackgroundResId")
    field.isAccessible = true
    field.set(this, res)
}
