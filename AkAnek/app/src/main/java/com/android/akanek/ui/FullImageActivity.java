package com.android.akanek.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.akanek.R;
import com.android.akanek.application.EkAnekApp;
import com.android.akanek.model.PhotoModel;
import com.android.akanek.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;

public class FullImageActivity extends AppCompatActivity {

  private static final int STORAGE_PERMISSION = 10000;
  private String imageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_full_image);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    if (intent == null)
      return;

    PhotoModel photoModel = (PhotoModel) intent.getSerializableExtra("model");
    getSupportActionBar().setTitle("");
    imageUrl = photoModel.imageUrl;
    ImageView imageView = findViewById(R.id.image);
    AppUtils.setImage(imageView, this, imageUrl);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_full_image, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {

      case android.R.id.home:
        super.onBackPressed();
        break;

      case R.id.action_share:
        storagePermission();
        break;


    }


    return super.onOptionsItemSelected(item);
  }

  private void shareImage() {
    Glide.with(EkAnekApp.getInstance())
        .asBitmap()
        .load(imageUrl)
        .apply(
            new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        )
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            try {
              if (resource != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                byte[] bitmapData = bytes.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                if (path != null) {
                  Uri screenshotUri = Uri.parse(path);
                  sendIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                  sendIntent.setType("image/*");
                  sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                  startActivity(sendIntent);
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  private void storagePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      //check for permission
      int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
      if (storagePermission != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
      } else {
        shareImage();
      }
    } else {
      shareImage();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == STORAGE_PERMISSION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        shareImage();
      }
    }

  }


}
