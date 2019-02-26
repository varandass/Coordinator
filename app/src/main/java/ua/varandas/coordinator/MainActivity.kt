package ua.varandas.coordinator

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.support.v4.onPageChangeListener
import ua.varandas.coordinator.models.CircleModel
import ua.varandas.coordinator.ui.MainUI
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val INITIALIZE = "aca-pp-pub-6163958446304162~1558229454"
        const val INTERSTITIAL_UNIT_ID = "ca-app-pub-6163958446304162/4647427224"
        const val BANNER_UNIT_ID = "ca-app-pub-6163958446304162/2816442940"
        const val ADMOB_ON_OFF = false

    }

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mainUI: MainUI
    private lateinit var pager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var privacyURL: URL
    lateinit var form: ConsentForm

    override fun onStart() {
        super.onStart()
        CircleModel.pointXY()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        MobileAds.initialize(this, INITIALIZE)
        mInterstitialAd = InterstitialAd(this)
        mainUI = MainUI()
        mainUI.setContentView(this)
        configureTabLayout()

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
                                privacyURL = URL("https://b24-wavzre.bitrix24.site/")
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

        initInterstitial(ADMOB_ON_OFF)
    }



    private fun configureTabLayout() {
        pager = find(IDs.PAGER) as ViewPager
        tabLayout = find(IDs.TAB_LAYOUT) as TabLayout
        pager.offscreenPageLimit = 1
        pager.adapter = ua.varandas.coordinator.adapters.TabPagerAdapter(supportFragmentManager, this)
        tabLayout.setupWithViewPager(pager, false)
        pager.onPageChangeListener {
            onPageSelected {
                when (it) {
                    1 -> if (mInterstitialAd.isLoaded) mInterstitialAd.show()
                }
            }
        }
    }

    private fun initInterstitial(onOrOff: Boolean) {
        if (onOrOff) {
            mInterstitialAd.adUnitId = INTERSTITIAL_UNIT_ID
            mInterstitialAd.loadAd(AdRequest.Builder().build())
            mInterstitialAd.adListener = (object : AdListener() {
                override fun onAdClosed() {
                    mInterstitialAd.loadAd(AdRequest.Builder().build())
                }
            })
        }
    }
}
