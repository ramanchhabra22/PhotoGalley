package com.android.akanek.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.akanek.application.EkAnekApp;
import com.android.akanek.model.HandlerCallBack;
import com.android.akanek.model.PhotoModel;
import com.android.akanek.model.PhotoSearchPagerHandler;
import com.android.akanek.model.SharedPreferenceHelper;
import com.android.akanek.utils.AppUtils;

import java.util.ArrayList;

public class PhotoSearchViewModel extends ViewModel implements HandlerCallBack {

  private PhotoSearchPagerHandler searchPagerHandler;
  private MutableLiveData<ArrayList<PhotoModel>> photoModelData;
  private SharedPreferenceHelper sharedPreferenceHelper;

  public PhotoSearchViewModel() {
    searchPagerHandler = new PhotoSearchPagerHandler(this);
    sharedPreferenceHelper = new SharedPreferenceHelper(EkAnekApp.getInstance(), this);
  }

  public MutableLiveData<ArrayList<PhotoModel>> searchData(String query, int pageNo) {
    if (photoModelData == null) {
      photoModelData = new MutableLiveData<>();
    }

    if (AppUtils.isConnectedToInternet()) {
      searchPagerHandler.getSearchData(query, pageNo);
    } else {
      sharedPreferenceHelper.getPhotoData(query);
    }
    return photoModelData;
  }


  public void saveData(String searchTerm, ArrayList<PhotoModel> photoModels) {
    sharedPreferenceHelper.savePhotoData(searchTerm, photoModels);
  }


  @Override
  public void onSuccess(Object object) {
    if (object != null) {
      photoModelData.setValue((ArrayList<PhotoModel>) object);
    }
  }

  @Override
  public void onFailure(String message) {

  }

  @Override
  public void onError(Object object) {

  }
}
