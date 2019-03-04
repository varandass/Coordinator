package ua.varandas.coordinator

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.support.v4.onPageChangeListener
import ua.varandas.coordinator.ext.prefs
import ua.varandas.coordinator.firebase.ads.Ads
import ua.varandas.coordinator.firebase.ads.Ads.mInterstitialAd
import ua.varandas.coordinator.firebase.ads.URLConnection
import ua.varandas.coordinator.models.CircleModel
import ua.varandas.coordinator.ui.MainUI
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var mainUI: MainUI
    private lateinit var pager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var privacyURL: URL
    private lateinit var btnADS: Button
    lateinit var form: ConsentForm

    override fun onStart() {
        super.onStart()
        CircleModel.pointXY()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mainUI = MainUI()
        mainUI.setContentView(this)
        btnADS = find(IDs.REMOVE_ADS_BTN)
        configureTabLayout()
        addOrCloseAD()

        btnADS.setOnClickListener { addRevardedAD() }
    }
    private fun addRevardedAD() {
        if (Ads.mRewardedVideoAd !== null && Ads.mRewardedVideoAd!!.isLoaded) {
            Log.d(TAG, "Реклама загружена")
            Ads.mRewardedVideoAd!!.show()
        } else {
            Log.d(TAG, "Реклама не загружена")
            val toast = Toast.makeText(this, resources.getText(R.string.toast_btn_ads), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
    private fun addOrCloseAD() {
        if (URLConnection.isNetAvailable && isTimeUp() && !prefs.isAdsDisabled) {
            inEEA()
            Ads.enableAds(this)
        } else {
            Ads.disableAds(this)
        }
    }
    private fun isTimeUp(): Boolean = System.currentTimeMillis() > prefs.estimatedAdsTime
    private fun configureTabLayout() {
        pager = find(IDs.PAGER) as ViewPager
        tabLayout = find(IDs.TAB_LAYOUT) as TabLayout
        pager.offscreenPageLimit = 1
        pager.adapter = ua.varandas.coordinator.adapters.TabPagerAdapter(supportFragmentManager, this)
        tabLayout.setupWithViewPager(pager, false)
        pager.onPageChangeListener {
            onPageSelected {
                when (it) {
                    0 -> addOrCloseAD()
                    1 -> addInterstitialAD()
                    2 -> addInterstitialAD()
                }
            }
        }

    }
    private fun addInterstitialAD() {
        if (Ads.mInterstitialAd !== null && Ads.mInterstitialAd!!.isLoaded) {
            Log.d(TAG, "Реклама загружена")
            Ads.mInterstitialAd!!.show()
        } else {
            Log.d(TAG, "Реклама не загружена")
        }
    }
    private fun inEEA() {
        Log.d(TAG, "EEA GO!!!!!!!!!!!!!")
        val consentInformation = ConsentInformation.getInstance(applicationContext)
        val publisherIds = arrayOf("pub-6163958446304162")
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onFailedToUpdateConsentInfo(reason: String?) {}
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {
                val inEEA = ConsentInformation.getInstance(applicationContext).isRequestLocationInEeaOrUnknown

                if (inEEA) {
                    when (consentStatus) {
                        ConsentStatus.PERSONALIZED -> {
                        }
                        ConsentStatus.NON_PERSONALIZED -> {
                            val extras = Bundle()
                            extras.putString("npa", "1")
                            AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
                        }
                        else -> {

                            try {
                                privacyURL = URL("https://github.com/varandass/Coordinator/blob/master/privacy_policy_coordinator.md")
                            } catch (e: MalformedURLException ) {
                                e.printStackTrace()
                            }
                            form = ConsentForm.Builder(this@MainActivity, privacyURL).withListener(object : ConsentFormListener() {
                                override fun onConsentFormLoaded() {
                                    form.show()
                                }

                                override fun onConsentFormOpened() {

                                }

                                override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                                    if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                                        val extras = Bundle()
                                        extras.putString("npa", "1")
                                        AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
                                    }
                                }

                                override fun onConsentFormError(reason: String?) {

                                }
                            }).withNonPersonalizedAdsOption().withNonPersonalizedAdsOption()
                                    //.withAdFreeOption()
                                    .build()
                            form.load()
                        }
                    }
                }
            }
        })
    }
}
