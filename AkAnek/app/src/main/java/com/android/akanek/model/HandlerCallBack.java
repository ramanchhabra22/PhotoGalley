package com.android.akanek.model;

public interface HandlerCallBack {
  void onSuccess(Object object);
  void onFailure(String message);
  void onError(Object object);
}
