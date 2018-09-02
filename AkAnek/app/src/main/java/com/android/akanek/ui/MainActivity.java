package com.android.akanek.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.android.akanek.R;
import com.android.akanek.model.PhotoModel;
import com.android.akanek.utils.AppUtils;
import com.android.akanek.utils.SpaceItemDecoration;
import com.android.akanek.viewModel.PhotoSearchViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

  private RecyclerView recyclerView;
  private PhotoRecyclerViewAdapter adapter;
  private ArrayList<PhotoModel> photoModelList;
  private int pastVisiblesItems, visibleItemCount, totalItemCount;
  private boolean loading;
  private PhotoSearchViewModel mModel;
  private String query;
  private int pageNo;
  private Observer<ArrayList<PhotoModel>> observer;
  private GridLayoutManager layoutManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    mModel = ViewModelProviders.of(this).get(PhotoSearchViewModel.class);
    setRecyclerViewAndAdapter();
  }

  private void searchDataObserver() {
    if (observer == null) {
      observer = new Observer<ArrayList<PhotoModel>>() {
        @Override
        public void onChanged(@Nullable final ArrayList<PhotoModel> photoModels) {
          if (pageNo == 1) {
            photoModelList.clear();
          }
          loading = false;
          photoModelList.addAll(photoModels);
          adapter.notifyDataSetChanged();
          mModel.saveData(query, photoModelList);
        }
      };
    }
    mModel.searchData(query, pageNo).observe(this, observer);
  }


  private void setRecyclerViewAndAdapter() {
    photoModelList = new ArrayList<>();
    recyclerView = findViewById(R.id.cm_recycler_view);
//    recyclerView.addItemDecoration(new SpaceItemDecoration(5));
    layoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new PhotoRecyclerViewAdapter(this, photoModelList);
    recyclerView.setAdapter(adapter);


    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!AppUtils.isConnectedToInternet()) {
          return;
        }

        if (dy > 0) {
          visibleItemCount = layoutManager.getChildCount();
          totalItemCount = layoutManager.getItemCount();
          pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

          if (!loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
              loading = true;
              pageNo = pageNo + 1;
              searchDataObserver();
            }
          }
        }
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    MenuItem searchMenuItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchMenuItem.getActionView();
    searchView.setQueryHint("enter Text");
    searchView.setOnQueryTextListener(this);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {

      case R.id.two_column:
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        break;

      case R.id.three_column:
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        break;

      case R.id.four_column:
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        break;
    }


    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onQueryTextSubmit(String s) {
    pageNo = 1;
    query = s;
    searchDataObserver();
    return false;
  }

  @Override
  public boolean onQueryTextChange(String s) {
//    pageNo = 1;
//    query = s;
//    searchDataObserver();
    return false;
  }

}
