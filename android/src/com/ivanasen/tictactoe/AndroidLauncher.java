package com.ivanasen.tictactoe;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends FragmentActivity
        implements AndroidFragmentApplication.Callbacks, ActionResolver, Parcelable {

    public static final String LAUNCHER_KEY = "launcher_kay";

    public InterstitialAd mInterstitialAd;
    private AdView mAdBannerView;
    private double mTimeInterstitialStartedLoading;

    public AndroidLauncher() {
        super();
    }

    protected AndroidLauncher(Parcel in) {
        mTimeInterstitialStartedLoading = in.readDouble();
    }

    public static final Creator<AndroidLauncher> CREATOR = new Creator<AndroidLauncher>() {
        @Override
        public AndroidLauncher createFromParcel(Parcel in) {
            return new AndroidLauncher(in);
        }

        @Override
        public AndroidLauncher[] newArray(int size) {
            return new AndroidLauncher[size];
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mTimeInterstitialStartedLoading = System.currentTimeMillis() / 1000.0 - 120;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Bundle args = new Bundle();
        args.putParcelable(LAUNCHER_KEY, this);

        GameFragment mGameFragment = new GameFragment();
        mGameFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment, mGameFragment);
        transaction.commit();

        String appId = getString(R.string.app_id);
        MobileAds.initialize(getApplicationContext(), appId);

        onCreateBannerAd();
        onCreateInterstitialAd();
    }

    private void onCreateInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }

    private void onCreateBannerAd() {
        mAdBannerView = new AdView(this);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdBannerView.setAdSize(AdSize.SMART_BANNER);
        mAdBannerView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mAdBannerView.loadAd(adRequest);
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void exit() {
    }

    @Override
    public void checkIfShouldShowAd() {
        if (mInterstitialAd == null)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double currentTime = System.currentTimeMillis() / 1000.0;
                int minTime = getResources().getInteger(R.integer.min_elapsed_time_interstitial_ad);
                boolean isRequiredTimeMet = (currentTime - mTimeInterstitialStartedLoading) > minTime;

                if (mInterstitialAd.isLoaded() && isRequiredTimeMet) {
                    mInterstitialAd.show();
                    mTimeInterstitialStartedLoading = currentTime;
                }
            }
        });

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mTimeInterstitialStartedLoading);
    }

    public static class GameFragment extends AndroidFragmentApplication {
        private ActionResolver mActionResolver;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mActionResolver = (AndroidLauncher) getArguments().get(AndroidLauncher.LAUNCHER_KEY);
            return initializeForView(new TicTacToeMain(mActionResolver));
        }
    }

}
