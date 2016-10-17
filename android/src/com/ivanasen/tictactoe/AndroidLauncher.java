package com.ivanasen.tictactoe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks {

    private GameFragment mGameFragment;
    private AdView mAdBannerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mGameFragment = new GameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment, mGameFragment);
        transaction.commit();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        createBannerAd();
    }

    private void createBannerAd() {
        mAdBannerView = new AdView(this);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("9A34A68053A117A4E1BE1AE21CD64C50").build();
        mAdBannerView.setAdSize(AdSize.SMART_BANNER);
        mAdBannerView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mAdBannerView.loadAd(adRequest);
        mAdBannerView.setForegroundGravity(Gravity.BOTTOM);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mAdBannerView.setLayoutParams(params);

        mAdBannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                FrameLayout layout = (FrameLayout) findViewById(R.id.main_layout);
                layout.removeView(mAdBannerView);
                layout.addView(mAdBannerView);
            }
        });
    }

    @Override
    public void exit() {}

    public static class GameFragment extends AndroidFragmentApplication {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return initializeForView(new TicTacToeMain());
        }
    }

}
