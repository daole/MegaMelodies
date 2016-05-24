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

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterSearch;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewSearch;

public class ScreenSearch extends ScreenBase<IPresenterSearch> implements IViewSearch {
    private SearchView mSearchView;
    private Spinner mSpnServers;

    @Override
    protected void createOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        pInflater.inflate(R.menu.menu__search_screen, pMenu);
        this.setUpSearchViewMenuItem(pMenu);
        this.setUpServerSpinnersMenuItem(pMenu);
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
    protected void retrieveScreenItems(View pView) {

    }

    @Override
    protected void mapInformationToScreenItems(View pView) {

    }

    @Override
    protected int getTitle() {
        return R.string.title__search;
    }

    private void setUpSearchViewMenuItem(Menu pMenu) {
        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = pMenu.findItem(R.id.option_menu_item__search);
        this.mSearchView = (SearchView) searchMenuItem.getActionView();
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
        MenuItem serversMenuItem = pMenu.findItem(R.id.option_menu_item__servers);
        this.mSpnServers = (Spinner) serversMenuItem.getActionView();
        this.mSpnServers.setAdapter(listAdapter);
    }

    private boolean onSearchViewQueryTextSubmitted(String pQuery) {
        //MenuItemCompat.collapseActionView(this.mActionSearch);
        this.mSearchView.clearFocus();
        return true;
    }

    private boolean onSearchViewQueryTextChanged(String pNewText) {
        return false;
    }
}
