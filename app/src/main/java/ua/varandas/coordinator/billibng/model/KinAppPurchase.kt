package ua.varandas.coordinator.billibng.model

//import android.os.Parcelable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class KinAppPurchase(
        val orderId: String,
        val productId: String,
        val purchaseTime: Long,
        val purchaseToken: String,
        val purchaseState: KinAppPurchaseState,
        val packageName: String,
        val developerPayload: String,
        val autoRenewing: Boolean) : Parcelable
