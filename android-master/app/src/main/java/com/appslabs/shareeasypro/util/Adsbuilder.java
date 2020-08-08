package com.appslabs.shareeasypro.util;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class Adsbuilder {
    Context context;
    InterstitialAd interstitialAd;
    LinearLayout adContainer;
    AdView mAdView;
    String TAG = "FbAds";
    //final String APPID="3057050281076999";

    final String ADS_PLACEMENT_ID_INTERSTITAL = "339939833675522_339940540342118";
    final String ADS_PLACEMENT_ID_BANNER = "339939833675522_339940823675423";


    public Adsbuilder(Context ctx, LinearLayout linearLayout) {
        context = ctx;
        adContainer = linearLayout;
    }

    public void buildAdsListner() {
        interstitialAd = new InterstitialAd(context, getAppId());

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
    }

    public void loadAds() {
        interstitialAd.loadAd();
    }

    public void showAds() {
        if(interstitialAd.isAdLoaded())
        interstitialAd.show();
    }

    public void destroyInterstialAds() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }

    }

    public void destroyBannerAds() {
        if (mAdView != null) {
            mAdView.destroy();
        }

    }

    public void loadBannerAds() {
        mAdView = new AdView(context, ADS_PLACEMENT_ID_BANNER, AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        adContainer.addView(mAdView);

        // Request an ad
        mAdView.loadAd();

    }

    public String getAppId() {
        return ADS_PLACEMENT_ID_INTERSTITAL;
    }

}
