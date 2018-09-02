package com.android.akanek.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
  private int space;
  private int spanCount;

  public SpaceItemDecoration(int space) {
    this.space = space;
  }

  private void setSpanCount(int spanCount){
    this.spanCount = spanCount;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//    outRect.left = space;
//    outRect.right = space;
    outRect.bottom = space;

    // Add top margin only for the first item to avoid double space between items
    int position = parent.getChildAdapterPosition(view);
    if (position % 2 == 0)
      outRect.right = space;
  }
}
