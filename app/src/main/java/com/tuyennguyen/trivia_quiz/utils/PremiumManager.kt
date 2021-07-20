package com.tuyennguyen.trivia_quiz.utils


class PremiumManager {
    companion object {
        private var instance: PremiumManager? = null
        fun getInstance(): PremiumManager? {
            if (instance == null) {
                instance = PremiumManager()
            }
            return instance
        }

        private const val BUY_VIP_A_WEEK = "buy_vip_a_week"
        private const val BUY_VIP_A_MONTH = "buy_vip_a_month"
        private const val BUY_VIP_THREE_MONTH = "buy_vip_three_month"
        private const val BUY_VIP_SIX_MONTH = "buy_vip_six_month"
        private const val BUY_VIP_A_YEAR = "buy_vip_a_year"

        val billingIds = listOf(
//            "android.test.purchased",
            BUY_VIP_A_WEEK,
            BUY_VIP_A_MONTH,
            BUY_VIP_THREE_MONTH,
            BUY_VIP_SIX_MONTH,
            BUY_VIP_A_YEAR,
        )
    }

    var isPremium = false
}