package ua.varandas.coordinator.ui

import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager
import ua.varandas.coordinator.R
import ua.varandas.coordinator.ext.tabTextSize


class MainUI : AnkoComponent<ua.varandas.coordinator.MainActivity> {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout


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

                if(ua.varandas.coordinator.MainActivity.ADMOB_ON_OFF){
                    val adView = AdView(ui.ctx)
                    adView.adSize = AdSize.BANNER
                    adView.adUnitId = ua.varandas.coordinator.MainActivity.BANNER_UNIT_ID
                    adView.loadAd(AdRequest.Builder().build())
                    addView(adView)
                }


            }.lparams(matchParent, wrapContent)

            viewPager = viewPager {
                id = ua.varandas.coordinator.IDs.PAGER
            }.lparams(matchParent, matchParent)


        }

    }.view
}
