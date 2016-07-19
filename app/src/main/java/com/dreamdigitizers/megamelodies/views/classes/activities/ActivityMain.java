package com.dreamdigitizers.megamelodies.views.classes.activities;

import android.support.design.widget.CoordinatorLayout;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.support.AdListener;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.views.classes.fragments.screens.ScreenMain;
import com.google.android.gms.ads.AdView;

public class ActivityMain extends ActivityBase {
    public static final String EXTRA__CURRENT_MEDIA_DESCRIPTION = "current_media_description";

    private AdView mAdView;

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected CoordinatorLayout getCoordinatorLayout() {
        return this.mCoordinatorLayout;
    }

    @Override
    protected void setLayout() {
        this.setContentView(R.layout.activity__main);
    }

    @Override
    protected void retrieveItems() {
        this.mCoordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.coordinatorLayout);
        this.mAdView = (AdView) this.findViewById(R.id.adView);
    }

    @Override
    protected void mapInformationToItems() {
        this.mAdView.setAdListener(new AdListener(this.mAdView));
    }

    @Override
    protected ScreenBase getStartScreen() {
        return new ScreenMain();
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.placeHolderScreen;
    }
}
