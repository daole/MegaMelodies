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
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
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
        this.mViewPager.setAdapter(new ViewPagerAdapter(this.getChildFragmentManager()));
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
        this.mTabLayout.getTabAt(0).setIcon(R.drawable.ic__search);
        this.mTabLayout.getTabAt(1).setIcon(R.drawable.ic__favorite);
        this.mTabLayout.getTabAt(2).setIcon(R.drawable.ic__playlist);
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    private void onPageSelected(int pPosition) {
        this.mToolbar.setTitle(this.mViewPager.getAdapter().getPageTitle(pPosition));
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<FragmentBase> mFragments;

        public ViewPagerAdapter(FragmentManager pFragmentManager) {
            super(pFragmentManager);
            this.mFragments = new ArrayList<>();

            ScreenSearch screenSearch = new ScreenSearch();
            this.mFragments.add(screenSearch);

            ScreenFavorites screenFavorites = new ScreenFavorites();
            this.mFragments.add(screenFavorites);

            ScreenPlaylists screenPlaylists = new ScreenPlaylists();
            this.mFragments.add(screenPlaylists);
        }

        @Override
        public Fragment getItem(int pPosition) {
            return this.mFragments.get(pPosition);
        }

        @Override
        public int getCount() {
            return this.mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int pPosition) {
            return null;
        }
    }
}
