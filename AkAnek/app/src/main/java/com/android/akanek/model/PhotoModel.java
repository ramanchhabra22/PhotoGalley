package com.android.akanek.model;

import org.json.JSONObject;

import java.io.Serializable;

public class PhotoModel implements Serializable{

  public String id;
  public String imageUrl;
  public String title;

  public PhotoModel(JSONObject object){
    id = object.optString("id");
    imageUrl = object.optString("url_m");
    title = object.optString("title");
  }
}
