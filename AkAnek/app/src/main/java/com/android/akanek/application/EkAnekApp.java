package com.android.akanek.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EkAnekApp extends Application {

  private static EkAnekApp mInstance;

  private RequestQueue mRequestQueue;


  public static synchronized EkAnekApp getInstance() {
    return mInstance;
  }


  @Override
  public void onCreate() {
    super.onCreate();
    mInstance = this;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return mRequestQueue;
  }
}
