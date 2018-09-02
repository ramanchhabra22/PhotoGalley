package com.android.akanek.model;

import com.android.akanek.application.EkAnekApp;
import com.android.akanek.utils.AppUrlConstants;
import com.android.akanek.viewModel.PhotoSearchViewModel;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class PhotoSearchPagerHandler {

  private HandlerCallBack callBack;
  private RequestQueue requestQueue;
  private int pages;

  public PhotoSearchPagerHandler(HandlerCallBack callBack) {
    this.callBack = callBack;
    requestQueue = EkAnekApp.getInstance().getRequestQueue();
  }

  public void getSearchData(String query,int pageNo) {
    String url = String.format(AppUrlConstants.SEARCH_DATA, query,pageNo);
    if (pages == pageNo){
      return;
    }

    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            if (response != null) {
              if (!response.isNull("stat")){
                String status = response.optString("stat");
                if (status.equalsIgnoreCase("ok")){
                  if (!response.isNull("photos")){
                    JSONObject photos = response.optJSONObject("photos");
                    pages = photos.optInt("pages");
                    if (!photos.isNull("photo")){
                      ArrayList<PhotoModel> photoModels = new ArrayList<>();
                      JSONArray jsonArray = photos.optJSONArray("photo");
                      int len = jsonArray.length();
                      for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.optJSONObject(i);
                        photoModels.add(new PhotoModel(object));
                      }
                      callBack.onSuccess(photoModels);
                    }
                  }

                }
              }
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {
            callBack.onError(volleyError);
          }
        });
    int socketTimeout = 50000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    jsonObjectRequest.setRetryPolicy(policy);
    requestQueue.add(jsonObjectRequest);
  }
}
