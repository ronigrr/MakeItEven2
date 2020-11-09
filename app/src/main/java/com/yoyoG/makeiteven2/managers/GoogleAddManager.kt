package com.yoyoG.makeiteven2.managers

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.room.DatabaseHelper

object GoogleAddManager {

    fun loadInterstitialAd(context: Context) {
        Constants.mInterstitialAd = InterstitialAd(context)
//        Constants.mInterstitialAd.adUnitId = Constants.ADD_MOB_TEST
        Constants.mInterstitialAd.adUnitId = Constants.AD_MOB_INTERSTITIAL_AD
        Constants.mInterstitialAd.loadAd(AdRequest.Builder().build())
        Constants.mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                Constants.mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
                Log.d("ad", "onInterstitialAdLoaded")
            }
        }
    }

    fun showInterstitialAd() {
        if (Constants.mInterstitialAd.isLoaded) {
            Constants.mInterstitialAd.show()
        } else {
            Log.d("ad", "The interstitial wasn't loaded yet.")
        }
    }

    fun loadRewardAD(context: Context) {
//        Constants.rewardedAd = RewardedAd(context, Constants.ADD_MOB_TEST)
        Constants.rewardedAd = RewardedAd(context, Constants.AD_MOB_REWARD_AD)
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.d("ad", "onRewardedAdLoaded")
                Constants.rewardedAdLoaded.postValue(true)
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                Log.d("ad", "onRewardedAdFailedToLoad")
                Constants.rewardedAdLoaded.postValue(false)
                Toast.makeText(context, "check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }
        Constants.rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    fun showRewardVideo(context: Context, activityContext: Activity) {
        if (Constants.rewardedAd.isLoaded) {
            val adCallback = object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    Log.d("ad", "onRewardedAdOpened")
                }

                override fun onRewardedAdClosed() {
                    Log.d("ad", "onRewardedAdClosed")
                    loadRewardAD(context)
                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    Log.d("ad", "onUserEarnedReward (${reward.amount.toString()})")
                    DatabaseHelper.addCoins(context, reward.amount)
                }

                override fun onRewardedAdFailedToShow(adError: AdError) {
                    Log.d("ad", "onRewardedAdFailedToShow")
                }
            }
            Constants.rewardedAd.show(activityContext, adCallback)
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.")
        }
    }
}