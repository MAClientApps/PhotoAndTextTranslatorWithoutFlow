package com.texttrans.translator.app_data.utility;

import android.app.Activity;
import android.view.View;

import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.texttrans.translator.R;


public class AdsUtil {
    public static boolean showTAds = true;

    public static MaxRewardedAd sMaxTextRewardedAd;
    public static MaxInterstitialAd sMaxTextInterstitialAd;

    public static void initialiseTextAds(Activity activity) {
        if (AdsUtil.showTAds) {
            AppLovinSdk.getInstance (activity).setMediationProvider ("max");
            AppLovinSdk.initializeSdk (activity, new AppLovinSdk.SdkInitializationListener () {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                    try {
                        sMaxTextInterstitialAd = new MaxInterstitialAd (activity.getResources ().getString (R.string.intertial_ads), activity);
                        sMaxTextInterstitialAd.loadAd ();
                    } catch (Exception e) {

                    }
                    try {
                        sMaxTextRewardedAd = MaxRewardedAd.getInstance (activity.getResources ().getString (R.string.rewarded_ad), activity);
                        sMaxTextRewardedAd.loadAd ();
                    } catch (Exception e) {

                    }

                }


            });
        }
    }

    public static void setInstanceMaxTextRewardedAd(Activity activity) {
        try {
            if (sMaxTextRewardedAd == null) {
                sMaxTextRewardedAd = MaxRewardedAd.getInstance (activity.getResources ().getString (R.string.rewarded_ad), activity);
                sMaxTextRewardedAd.loadAd ();
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    public static void showTextMoviesAds(MaxAdView maxAdView) {

        if (AdsUtil.showTAds) {
            maxAdView.setVisibility (View.VISIBLE);
            maxAdView.loadAd ();
        } else {
            maxAdView.setVisibility (View.GONE);
        }


    }




}
