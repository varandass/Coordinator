package ua.varandas.coordinator.billibng.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KinAppProduct(
        val product_id: String,
        val title: String,
        val description: String,
        val price: String,
        val priceAmountMicros: Long,
        val priceCurrencyCode: String,
        val type: KinAppProductType) : Parcelable
