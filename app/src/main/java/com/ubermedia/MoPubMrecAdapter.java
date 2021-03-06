package com.ubermedia;

import android.content.Context;
import android.util.Log;

import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

import ubermedia.com.ubermedia.CBAdapterBannerView;
import ubermedia.com.ubermedia.CBListener;
import ubermedia.com.ubermedia.ClearBid;

public class MoPubMrecAdapter extends CustomEventBanner implements CBListener {
    private final String CLASS_TAG = "ClearBid";

    private CustomEventBannerListener mBannerListener;

    @Override
    protected void loadBanner(final Context context, final CustomEventBanner.CustomEventBannerListener customEventBannerListener,
                              final Map<String, Object> localExtras,
                              final Map<String, String> serverExtras) {

        Log.d(CLASS_TAG, "MOPUB CUSTOM ADAPTER WAS CALLED");

        mBannerListener = customEventBannerListener;

        String adUnit = "default_ad_unit";
        String adUnitKey = "ad_unit_id";

        if (serverExtras.containsKey(adUnitKey)) {
            adUnit = serverExtras.get(adUnitKey).toString();
        }

        Log.d(CLASS_TAG, "Ad Unit Received: " + adUnit);

        CBAdapterBannerView bannerView = ClearBid.getAdapterBannerView(context, adUnit, this);
        ClearBid.removeCacheAdPlacement(adUnit);

        Log.d(CLASS_TAG, bannerView.CurrentBid + "");

        if (bannerView.CurrentBid > 0.0) {
            customEventBannerListener.onBannerLoaded(bannerView);
        } else {
            customEventBannerListener.onBannerFailed(MoPubErrorCode.NO_FILL);
        }
    }

    @Override
    protected void onInvalidate() {
    }

    @Override
    public void onAdClicked() {
        Log.d(CLASS_TAG, "onAdClicked");

        if (mBannerListener != null) {
            mBannerListener.onBannerClicked();
        }
    }
}