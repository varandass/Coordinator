package ua.varandas.coordinator

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.remove_ads_dialog.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.support.v4.onPageChangeListener
import ua.varandas.coordinator.billibng.model.KinAppProductType
import ua.varandas.coordinator.billibng.model.KinAppPurchase
import ua.varandas.coordinator.billibng.model.KinAppPurchaseResult
import ua.varandas.coordinator.billibng.model.managers.KinAppManager
import ua.varandas.coordinator.ext.prefs
import ua.varandas.coordinator.firebase.ads.Ads
import ua.varandas.coordinator.firebase.ads.Ads.mInterstitialAd
import ua.varandas.coordinator.firebase.ads.URLConnection
import ua.varandas.coordinator.models.CircleModel
import ua.varandas.coordinator.ui.MainUI
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity(), KinAppManager.KinAppListener {

    private val TAG = "MainActivity"

    private val RSA = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlOUn1gbAU1PhwWZh0v8OwFd+E2Ecd7rfj0mrP1QrUNRPjrw83hAV/HdsMq1lna17DKkHtyDVV2JENqGeFRxawehm3uLB82BlZd34ztwiI0ShVX/VssJ6j6TonUxot3S9vCtg1H5t/NQi3vz7WsmIxjZvm50iWf724m1iCna7Kl5iw09OrpvSAf/t5ZFMulyQHQ+6tHahzifLlGH/e7mFIY2uVz1VkMSSRLOs+lSVNgGEoXFffmfdfwtr6qeD5uh0Ns7Dc/NsZTyX0f2MPIV3JdgE+GGPdU1Lq/6zUV8UJ5aZ3fxqTTMClbHsksf4U4mV0XDhqaAahapLbO9NzFwOywIDAQAB"
    private val product_id = "ads_remove"

    private lateinit var mainUI: MainUI
    private lateinit var pager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var privacyURL: URL
    private lateinit var btnADS: Button
    lateinit var form: ConsentForm

    private val billingManager = KinAppManager(this, RSA)
    private var isBillingReady = false

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
        billingManager.bind(this)
        configureTabLayout()
        addOrCloseAD()

        btnADS.setOnClickListener { showAdsDialog() }
    }

    private fun showAdsDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.remove_ads_dialog, null)
        val builder = AlertDialog.Builder(this).setView(mDialogView)
        val showDialod = builder.show()

        mDialogView.ads_cancel.setOnClickListener { showDialod.dismiss() }
        mDialogView.ads_ok.setOnClickListener {
            val check = mDialogView.radioGroup.checkedRadioButtonId
            when (check) {
                R.id.radioFreeRemove -> addRevardedAD()
                R.id.radioCashRemove -> removeAdsForever()
            }
            showDialod.dismiss()
        }
    }

    override fun onBillingReady() {
        isBillingReady = true
        if (billingManager.isBuy(product_id, KinAppProductType.INAPP)) {
            prefs.isAdsDisabled = true
            Ads.disableAds(this)
        } else {
            prefs.isAdsDisabled = false
            Ads.enableAds(this)
        }
    }


    override fun onPurchaseFinished(purchaseResult: KinAppPurchaseResult, purchase: KinAppPurchase?) {
        when (purchaseResult) {
            KinAppPurchaseResult.SUCCESS -> {
                Log.d(TAG, "Покупка прошла УСПЕШНО")
                prefs.isAdsDisabled = true
                Ads.disableAds(this)
            }
            KinAppPurchaseResult.ALREADY_OWNED -> {
                Log.d(TAG, "Продукт уже КУПЛЕН")
                prefs.isAdsDisabled = true
                Ads.disableAds(this)
            }
            KinAppPurchaseResult.INVALID_PURCHASE -> {
                Log.d(TAG, "Покупка НЕДЕЙСТВИТЕЛЬНА")
                prefs.isAdsDisabled = false
                Ads.enableAds(this)
            }
            KinAppPurchaseResult.INVALID_SIGNATURE -> {
                Log.d(TAG, "Отмечено как НЕДЕЙСТВИТЕЛЬНО")
                prefs.isAdsDisabled = false
                Ads.enableAds(this)
            }
            KinAppPurchaseResult.CANCEL -> {
                Log.d(TAG, "Покупка ОТМЕНЕНА пользователем")
                prefs.isAdsDisabled = false
                Ads.enableAds(this)
            }
        }
    }

    private fun removeAdsForever() {
        if (isBillingReady) {
            billingManager.purchase(this, product_id, KinAppProductType.INAPP)
        }
    }

    override fun onDestroy() {
        billingManager.unbind()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingManager.verifyPurchase(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
