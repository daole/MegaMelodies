package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterMain;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewMain;

import java.util.ArrayList;
import java.util.List;

public class ScreenMain extends ScreenBase<IPresenterMain> implements IViewMain {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private int mLastTabPosition;

    @Override
    public boolean onBackPressed() {
        ScreenBase screenBase = (ScreenBase) this.mViewPagerAdapter.getItem(this.mViewPager.getCurrentItem());
        boolean result = screenBase.onBackPressed();
        if (result) {
            return true;
        }
        if (this.mViewPager.getCurrentItem() > 0) {
            this.mViewPager.setCurrentItem(0, true);
            return true;
        }
        return false;
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterMain createPresenter() {
        return (IPresenterMain) PresenterFactory.createPresenter(IPresenterMain.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__main, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mToolbar = (Toolbar) pView.findViewById(R.id.toolbar);
        this.mTabLayout = (TabLayout) pView.findViewById(R.id.tabLayout);
        this.mViewPager = (ViewPager) pView.findViewById(R.id.viewPager);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        ((ActivityBase)this.getActivity()).setSupportActionBar(this.mToolbar);
        this.mViewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());
        this.mViewPager.setAdapter(this.mViewPagerAdapter);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pPosition, float pPositionOffset, int pPositionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pPosition) {
                ScreenMain.this.onPageSelected(pPosition);
            }

            @Override
            public void onPageScrollStateChanged(int pState) {
            }
        });
        this.mTabLayout.setupWithViewPager(this.mViewPager);
        this.mTabLayout.getTabAt(0).setIcon(R.drawable.ic__search_white);
        this.mTabLayout.getTabAt(1).setIcon(R.drawable.ic__favorite_white);
        this.mTabLayout.getTabAt(2).setIcon(R.drawable.ic__playlist_white);
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    private void onPageSelected(int pPosition) {
        this.mToolbar.setTitle(this.mViewPager.getAdapter().getPageTitle(pPosition));

        Fragment fragment = this.mViewPagerAdapter.getItem(this.mLastTabPosition);
        if (fragment instanceof ScreenMediaItems) {
            ((ScreenMediaItems) fragment).onHide();
        }

        fragment = this.mViewPagerAdapter.getItem(pPosition);
        if (fragment instanceof ScreenMediaItems) {
            ((ScreenMediaItems) fragment).onShow();
        }

        this.mLastTabPosition = pPosition;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<ScreenBase> mScreens;

        public ViewPagerAdapter(FragmentManager pFragmentManager) {
            super(pFragmentManager);
            this.mScreens = new ArrayList<>();

            ScreenSearch screenSearch = new ScreenSearch();
            this.mScreens.add(screenSearch);

            ScreenFavorites screenFavorites = new ScreenFavorites();
            this.mScreens.add(screenFavorites);

            ScreenPlaylists screenPlaylists = new ScreenPlaylists();
            this.mScreens.add(screenPlaylists);
        }

        @Override
        public CharSequence getPageTitle(int pPosition) {
            return null;
        }

        @Override
        public Fragment getItem(int pPosition) {
            return this.mScreens.get(pPosition);
        }

        @Override
        public int getCount() {
            return this.mScreens.size();
        }
    }
}
