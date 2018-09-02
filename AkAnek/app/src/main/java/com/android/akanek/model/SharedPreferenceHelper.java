package com.android.akanek.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceHelper {

  private SharedPreferences mSharedPreferences;
  private SharedPreferences.Editor e;
  private HandlerCallBack callBack;


  public SharedPreferenceHelper(Context context, HandlerCallBack callBack) {
    mSharedPreferences = context.getSharedPreferences("EKANEK_PREFERENCE", MODE_PRIVATE);
    e = mSharedPreferences.edit();
    e.apply();
    this.callBack = callBack;
  }

  public void savePhotoData(String searchTerm, ArrayList<PhotoModel> photoModels) {
    String data = new Gson().toJson(photoModels);
    e.putString(searchTerm, data);
    e.apply();
  }

  public void getPhotoData(String searchTerm) {
    if (searchTerm != null && !searchTerm.equals("")) {
      String data = mSharedPreferences.getString(searchTerm, "");
      if (!data.equals("")) {
        ArrayList<PhotoModel> list = new Gson().fromJson(data, new TypeToken<ArrayList<PhotoModel>>() {
        }.getType());
        callBack.onSuccess(list);
      }
    }
  }
}
