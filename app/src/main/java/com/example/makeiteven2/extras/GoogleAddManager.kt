package com.example.makeiteven2.extras

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.makeiteven2.room.DatabaseHelper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object GoogleAddManager {

     fun loadRewardAD(context : Context) {
         Constants.rewardedAd = RewardedAd(context, Constants.ADD_MOB_TEST)
             val adLoadCallback = object : RewardedAdLoadCallback() {
                 override fun onRewardedAdLoaded() {
                     Log.v("ad", "onRewardedAdLoaded")
                 }

                 override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                     Log.v("ad", "onRewardedAdFailedToLoad")
                 }
             }
             Constants.rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    fun loadRewardVideo(context : Context,activityContext: Activity) {
        if (Constants.rewardedAd.isLoaded) {
            val adCallback = object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    Log.v("ad", "onRewardedAdOpened")
                }

                override fun onRewardedAdClosed() {
                    Log.v("ad", "onRewardedAdClosed")
                    loadRewardAD(context)
                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    Log.v("ad", "onUserEarnedReward (${reward.amount.toString()})")
                    DatabaseHelper.addCoins(context, reward.amount)
                }

                override fun onRewardedAdFailedToShow(adError: AdError) {
                    Log.v("ad", "onRewardedAdFailedToShow")
                }
            }
            Constants.rewardedAd.show(activityContext, adCallback)
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.")
        }
    }
}