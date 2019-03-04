package ua.varandas.coordinator.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager
import ua.varandas.coordinator.IDs
import ua.varandas.coordinator.R
import ua.varandas.coordinator.ext.tabTextSize
import ua.varandas.coordinator.firebase.ads.Ads


class MainUI : AnkoComponent<ua.varandas.coordinator.MainActivity> {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout


    @SuppressLint("SetTextI18n")
    override fun createView(ui: AnkoContext<ua.varandas.coordinator.MainActivity>) = ui.apply {

        verticalLayout {
            appBarLayout {

                tabLayout = tabLayout {
                    id = ua.varandas.coordinator.IDs.TAB_LAYOUT
                    setSelectedTabIndicatorColor(Color.WHITE)
                    setSelectedTabIndicatorHeight(0)
                    @Suppress("DEPRECATION") setTabTextColors(resources.getColor(R.color.colorTabText), resources.getColor(R.color.colorText))
                    tabTextSize(18f)
                }.lparams(matchParent, wrapContent)

                linearLayout {
                    id = IDs.ADS_LAYOUT
                    orientation = LinearLayout.VERTICAL
                    visibility = View.GONE

                    val adView = AdView(ui.ctx)
                    adView.id = IDs.AD_VIEW
                    adView.adSize = AdSize.BANNER
                    adView.adUnitId = Ads.BANNER_UNIT_ID
                    addView(adView)

                    button {
                        id = IDs.REMOVE_ADS_BTN
                        backgroundColor = ContextCompat.getColor(ui.ctx, R.color.colorAccent)
                        setText(R.string.reclama_btn)
                    }.lparams(matchParent, wrapContent) {
                        gravity = Gravity.CENTER
                        topMargin = dip(2)
                    }
                }
            }.lparams(matchParent, wrapContent)

            viewPager = viewPager {
                id = ua.varandas.coordinator.IDs.PAGER
            }.lparams(matchParent, matchParent)


        }

    }.view
}
