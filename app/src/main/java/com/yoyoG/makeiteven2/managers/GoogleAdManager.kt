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

object GoogleAdManager {

    fun loadInterstitialAd(context: Context) {
        Constants.mInterstitialAd = InterstitialAd(context)
        if (Constants.TEST_MODE) {
            Constants.mInterstitialAd.adUnitId = Constants.ADD_MOB_TEST
        }
        else {
            Constants.mInterstitialAd.adUnitId = Constants.AD_MOB_INTERSTITIAL_AD
        }

        Constants.mInterstitialAd.loadAd(AdRequest.Builder().build())
        Constants.mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                Constants.mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
                Log.d("adMob", "onInterstitialAdLoaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                Log.d("adMob", p0!!.message)
            }
        }
    }

    fun showInterstitialAd() {
        if (Constants.mInterstitialAd.isLoaded) {
            Constants.mInterstitialAd.show()
        } else {
            Log.d("adMob", "The interstitial wasn't loaded yet.")
        }
    }

    fun loadRewardAD(context: Context) {
        if (Constants.TEST_MODE) {
            Constants.rewardedAd = RewardedAd(context, Constants.ADD_MOB_TEST)
        }
        else {
            Constants.rewardedAd = RewardedAd(context, Constants.AD_MOB_REWARD_AD)
        }
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.d("adMob", "onRewardedAdLoaded")
                Constants.rewardedAdLoaded.postValue(true)
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                Log.d("adMob", "onRewardedAdFailedToLoad ${adError.cause}")
                Constants.rewardedAdLoaded.postValue(false)
                //Toast.makeText(context, "check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }
        Constants.rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    fun showRewardVideo(context: Context, activityContext: Activity) {
        if (Constants.rewardedAd.isLoaded) {
            val adCallback = object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    Log.d("adMob", "onRewardedAdOpened")
                }

                override fun onRewardedAdClosed() {
                    Log.d("adMob", "onRewardedAdClosed")
                    loadRewardAD(context)

                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    Log.d("adMob", "onUserEarnedReward (${reward.amount.toString()})")
                    DatabaseHelper.addCoins(context, reward.amount)
                }

                override fun onRewardedAdFailedToShow(adError: AdError) {
                    Log.d("adMob", "onRewardedAdFailedToShow")
                }
            }
            Constants.rewardedAd.show(activityContext, adCallback)
        } else {
            Log.d("adMob", "The rewarded ad wasn't loaded yet.")
        }
    }
}