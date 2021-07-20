package com.tuyennguyen.trivia_quiz.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.billingclient.api.*
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.ui.main.MainActivity
import com.tuyennguyen.trivia_quiz.utils.PremiumManager

class SplashActivity : AppCompatActivity(), PurchasesUpdatedListener, BillingClientStateListener,
    PurchasesResponseListener {

    private lateinit var billingClient: BillingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(this)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {

    }

    override fun onBillingServiceDisconnected() {
        PremiumManager.getInstance()?.isPremium = false

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, this)
        } else {
            PremiumManager.getInstance()?.isPremium = false
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onQueryPurchasesResponse(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>
    ) {
        PremiumManager.getInstance()?.isPremium = purchases.isNotEmpty()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}