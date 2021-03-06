package ubermedia.com.headerbiddingdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import ubermedia.com.ubermedia.ClearBid;

public class InterstitialActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener {

    private final String CLASS_TAG = "ClearBid";

    private final Handler mHandler = new Handler();
    private Runnable mAdRefreshRunnable;

    private final int mAdRefreshRate = 45000;
    private final int mTimeBeforeFirstAdIsShown = 7000;

    /*     Insert your own ClearBid and MoPub ad units here    */
    private final String adUnit = "";
    private final String moPubAdUnit = "";

    private MoPubInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        // You only need to call this method once in the app, usually from the MainActivity
        ClearBid.initializeClearBidSDK(this, "insert api key", "insert secret key");
        ClearBid.requestLocationPermission(this);

        ClearBid.preCacheInterstitialPlacement(getApplicationContext(), adUnit, new int[]{320, 480});

        startRefreshTimer();

        Button button = (Button) findViewById(R.id.showInterstitialButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                mInterstitial.show();
            }
        });
    }

    private void startRefreshTimer() {
        stopRefreshTimer();

        Log.d(CLASS_TAG, "Starting Ad Refresh Timer");

        mAdRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(CLASS_TAG, "Refreshing Interstitial Ad");

                String targetKeywords = ClearBid.getTargetParamsAsString(adUnit);
                Log.d(CLASS_TAG, targetKeywords);

                mInterstitial = new MoPubInterstitial(InterstitialActivity.this, moPubAdUnit);
                mInterstitial.setKeywords(targetKeywords);
                mInterstitial.setInterstitialAdListener(InterstitialActivity.this);
                mInterstitial.load();

                mHandler.postDelayed(this, mAdRefreshRate);
            }
        };

        // Change postDelayed to post if you don't want to wait before first ad is shown (waiting allows HB SDK to cache ad)
        mHandler.postDelayed(mAdRefreshRunnable, mTimeBeforeFirstAdIsShown);
    }

    private void stopRefreshTimer() {
        Log.d(CLASS_TAG, "Stopping Ad Refresh Timer");

        // Clear existing timer if exists
        mHandler.removeCallbacks(mAdRefreshRunnable);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        Log.d(CLASS_TAG, "onWindowFocusChanged " + Boolean.toString(hasWindowFocus));

        if (hasWindowFocus) {
            startRefreshTimer();
            return;
        }

        stopRefreshTimer();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.d(CLASS_TAG, "onDetachedFromWindow");
        stopRefreshTimer();
    }

    @Override
    protected void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        Log.d(CLASS_TAG, "onInterstitialLoaded");
        Log.d(CLASS_TAG, "Is interstitial ready: " + mInterstitial.isReady());

        if (mInterstitial.isReady()) {
            setTextViewText("Interstitial Loaded. Press show to launch it.");

            // Show button
            Button button = (Button) findViewById(R.id.showInterstitialButton);
            button.setVisibility(View.VISIBLE);

            //mInterstitial.show();
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        Log.d(CLASS_TAG, "onInterstitialFailed");
        setTextViewText("onInterstitialFailed");

        ClearBid.preCacheInterstitialPlacement(getApplicationContext(), adUnit, new int[]{320, 480});
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        Log.d(CLASS_TAG, "onInterstitialShown");
        setTextViewText("onInterstitialShown");

        // Hide button
        Button button = (Button) findViewById(R.id.showInterstitialButton);
        button.setVisibility(View.INVISIBLE);

        ClearBid.preCacheInterstitialPlacement(getApplicationContext(), adUnit, new int[]{320, 480});
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        Log.d(CLASS_TAG, "onInterstitialClicked");
        setTextViewText("onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        Log.d(CLASS_TAG, "onInterstitialDismissed");
        setTextViewText("onInterstitialDismissed");
    }

    private void setTextViewText(String message) {
        TextView loadingTextView = (TextView) findViewById(R.id.interstitialLoadingTextView);
        loadingTextView.setText(message);
    }
}