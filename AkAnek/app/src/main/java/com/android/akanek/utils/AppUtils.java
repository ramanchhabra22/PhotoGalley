package com.android.akanek.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.android.akanek.R;
import com.android.akanek.application.EkAnekApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class AppUtils {

  public static void setImage(ImageView image, Context context, String imageUrl) {

    Glide.with(context)
        .load(imageUrl)
        .apply(
            new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(600,600)
                .centerCrop()
        )
        .into(image);
  }

  public static boolean isConnectedToInternet() {
    ConnectivityManager connectivity = (ConnectivityManager) EkAnekApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null)
        for (NetworkInfo anInfo : info)
          if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
    }
    return false;
  }
}
