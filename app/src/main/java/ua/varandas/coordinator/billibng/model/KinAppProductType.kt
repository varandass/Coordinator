package ua.varandas.coordinator.billibng.model

import ua.varandas.coordinator.billibng.model.managers.KinAppManager


enum class KinAppProductType(val value: String) {
    INAPP(KinAppManager.INAPP_TYPE), SUBSCRIPTION(KinAppManager.SUBS_TYPE)
}