package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterSearch;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewSearch;

public class ScreenSearch extends ScreenTracks<IPresenterSearch> implements IViewSearch {
    private MenuItem mMenuItemSearch;
    private MenuItem mMenuItemServers;
    private SearchView mSearchView;
    private Spinner mSpnServers;

    private String mQuery;

    @Override
    protected void createOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        pInflater.inflate(R.menu.menu__search_screen, pMenu);
        this.setUpSearchViewMenuItem(pMenu);
        this.setUpServerSpinnersMenuItem(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.option_menu_item__search:
                this.mMenuItemServers.collapseActionView();
                return true;
            case R.id.option_menu_item__servers:
                this.mMenuItemSearch.collapseActionView();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onShow() {
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterSearch createPresenter() {
        return (IPresenterSearch) PresenterFactory.createPresenter(IPresenterSearch.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__search, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return R.string.title__search;
    }

    @Override
    public int getServerId() {
        return this.mSpnServers.getSelectedItemPosition();
    }

    @Override
    public String getQuery() {
        return this.mQuery;
    }

    private void setUpSearchViewMenuItem(Menu pMenu) {
        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        this.mMenuItemSearch = pMenu.findItem(R.id.option_menu_item__search);
        this.mSearchView = (SearchView) this.mMenuItemSearch.getActionView();
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                return ScreenSearch.this.onSearchViewQueryTextSubmitted(pQuery);
            }

            @Override
            public boolean onQueryTextChange(String pNewText) {
                return ScreenSearch.this.onSearchViewQueryTextChanged(pNewText);
            }
        });
    }

    private void setUpServerSpinnersMenuItem(Menu pMenu) {
        ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.servers, android.R.layout.simple_spinner_item);
        listAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.mMenuItemServers = pMenu.findItem(R.id.option_menu_item__servers);
        this.mSpnServers = (Spinner) this.mMenuItemServers.getActionView();
        this.mSpnServers.setAdapter(listAdapter);
    }

    private boolean onSearchViewQueryTextSubmitted(String pQuery) {
        this.mSearchView.clearFocus();
        this.handleSearch(pQuery);
        return true;
    }

    private boolean onSearchViewQueryTextChanged(String pNewText) {
        return false;
    }

    private void handleSearch(String pQuery) {
        this.mQuery = pQuery;
        this.mFragmentMediaItems.clearMediaItems();
        this.mPresenter.search();
    }
}
