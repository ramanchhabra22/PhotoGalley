package com.android.akanek.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.akanek.R;
import com.android.akanek.model.PhotoModel;
import com.android.akanek.utils.AppUtils;

import java.util.ArrayList;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

  private Context mContext;
  private ArrayList<PhotoModel> photoModelList;

  public PhotoRecyclerViewAdapter(Context context, ArrayList<PhotoModel> photoModelList) {
    mContext = context;
    this.photoModelList = photoModelList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_tile, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
    final PhotoModel photoModel = photoModelList.get(i);
    AppUtils.setImage(viewHolder.imageView, mContext, photoModel.imageUrl);

    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mContext, FullImageActivity.class);
        intent.putExtra("model", photoModel);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, viewHolder.imageView, "robot");
        mContext.startActivity(intent, options.toBundle());
      }
    });
  }

  @Override
  public int getItemCount() {
    return photoModelList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.image);
    }
  }
}
