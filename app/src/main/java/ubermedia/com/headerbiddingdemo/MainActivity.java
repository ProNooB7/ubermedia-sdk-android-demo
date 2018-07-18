package ubermedia.com.headerbiddingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ubermedia.com.ubermedia.CBBannerView;
import ubermedia.com.ubermedia.ClearBid;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClearBid.initializeClearBidSDK(this, "test-1-api-key", "test-1-secret-key");
        ClearBid.requestLocationPermission(this);

        // Change the refresh rate to 15 seconds
        CBBannerView adView = (CBBannerView) findViewById(R.id.adview);
        adView.setAdRefreshRate(15);

        // Button logic to go test interstitials
        final Intent intent = new Intent(this, InterstitialActivity.class);

        Button interstitialActivityButton = (Button) findViewById(R.id.interstitialActivityButton);
        interstitialActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                startActivity(intent);
            }
        });
    }
}