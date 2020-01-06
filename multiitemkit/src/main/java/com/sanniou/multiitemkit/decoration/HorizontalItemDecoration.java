package com.sanniou.multiitemkit.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorizontalItemDecoration extends RecyclerView.ItemDecoration {

  private static final int[] ATTRS = {android.R.attr.listDivider};

  private final Map<Integer, Drawable> mDividerViewTypeMap;
  private final Drawable mFirstDrawable;
  private final Drawable mLastDrawable;
  private final Drawable mCommonDrawable;
  private final boolean mDrawOnTop;

  public HorizontalItemDecoration(Map<Integer, Drawable> dividerViewTypeMap,
                                  Drawable firstDrawable, Drawable lastDrawable, Drawable commonDrawable, boolean drawOnTop) {
    mDividerViewTypeMap = dividerViewTypeMap;
    mFirstDrawable = firstDrawable;
    mLastDrawable = lastDrawable;
    mCommonDrawable = commonDrawable;
    mDrawOnTop = drawOnTop;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                             RecyclerView.State state) {
    // specific view type
    int childType = parent.getLayoutManager().getItemViewType(view);

    // last position
    if (isLastPosition(view, parent)) {
      if (mLastDrawable != null) {
        outRect.right = mLastDrawable.getIntrinsicWidth();
      }
      if (mDrawOnTop) {
        Drawable drawableEnd = mDividerViewTypeMap.get(childType);
        if (drawableEnd == null) {
          drawableEnd = mCommonDrawable;
        }
        outRect.left = drawableEnd.getIntrinsicWidth();
      }
      return;
    }

    Drawable drawable = mDividerViewTypeMap.get(childType);
    if (drawable == null) {
      drawable = mCommonDrawable;
    }
    if (drawable != null) {
      if (mDrawOnTop) {
        outRect.left = drawable.getIntrinsicWidth();
      } else {
        outRect.right = drawable.getIntrinsicWidth();
      }
    }

    // first position
    if (isFirstPosition(view, parent)) {
      if (mFirstDrawable != null) {
        outRect.left = mFirstDrawable.getIntrinsicWidth();
      } else if (mDrawOnTop) {
        Drawable drawableStart = mDividerViewTypeMap.get(childType);
        if (drawableStart == null) {
          drawableStart = mCommonDrawable;
        }
        outRect.left = drawableStart.getIntrinsicWidth();
      }
    }
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int top = parent.getPaddingTop();
    int bottom = parent.getHeight() - parent.getPaddingBottom();

    int childCount = parent.getChildCount();
    for (int i = 0; i <= childCount - 1; i++) {
      View child = parent.getChildAt(i);
      int childViewType = parent.getLayoutManager().getItemViewType(child);
      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      // last position
      if (isLastPosition(child, parent)) {
        if (mDrawOnTop) {
          Drawable drawableEnd = mDividerViewTypeMap.get(childViewType);
          if (drawableEnd == null) {
            drawableEnd = mCommonDrawable;
          }
          if (drawableEnd != null) {
            int left, right;
            right = child.getLeft() - params.leftMargin;
            left = right - drawableEnd.getIntrinsicWidth();
            drawableEnd.setBounds(left, top, right, bottom);
            drawableEnd.draw(c);
          }
        }
        if (mLastDrawable != null) {
          int left, right;
          left = child.getRight() + params.rightMargin;
          right = left + mLastDrawable.getIntrinsicWidth();
          mLastDrawable.setBounds(left, top, right, bottom);
          mLastDrawable.draw(c);
        }
        return;
      }

      // specific view type
      Drawable drawable = mDividerViewTypeMap.get(childViewType);
      if (drawable == null) {
        drawable = mCommonDrawable;
      }
      if (drawable != null) {
        int left, right;
        if (mDrawOnTop) {
          right = child.getLeft() - params.leftMargin;
          left = right - drawable.getIntrinsicWidth();
        } else {
          left = child.getRight() + params.rightMargin;
          right = left + drawable.getIntrinsicWidth();
        }
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(c);
      }

      // first position
      if (isFirstPosition(child, parent) && mFirstDrawable != null) {

        int left, right;
        if (mDrawOnTop) {
          Drawable drawableStart = mDividerViewTypeMap.get(childViewType);
          if (drawableStart == null) {
            drawableStart = mCommonDrawable;
          }
          left = child.getRight() + params.rightMargin;
          right = left + drawableStart.getIntrinsicWidth();
        } else {
          right = child.getLeft() - params.leftMargin;
          left = right - mFirstDrawable.getIntrinsicWidth();
        }
        mFirstDrawable.setBounds(left, top, right, bottom);
        mFirstDrawable.draw(c);
      }
    }
  }

  private boolean isFirstPosition(View view, RecyclerView parent) {
    return parent.getChildAdapterPosition(view) == 0;
  }

  private boolean isLastPosition(View view, RecyclerView parent) {
    return parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1;
  }

  public static class Builder {

    private final Context mContext;
    private final Map<Integer, Drawable> mDividerViewTypeMap = new HashMap<>();
    private Drawable mFirstDrawable;
    private Drawable mLastDrawable;
    private Drawable mCommonDrawable;
    private boolean mDrawOnTop;

    public Builder(Context context) {
      mContext = context;
    }

    public Builder drawOnTop(boolean drawOnTop) {
      mDrawOnTop = drawOnTop;
      return this;
    }

    public Builder common(Drawable drawable) {
      mCommonDrawable = drawable;
      return this;
    }

    public Builder type(List<Integer> types, Drawable drawable) {
      if (types == null || drawable == null) {
        return this;
      }
      for (Integer type : types) {
        type(type, drawable);
      }
      return this;
    }

    public Builder type(int viewType) {
      final TypedArray a = mContext.obtainStyledAttributes(ATTRS);
      Drawable divider = a.getDrawable(0);
      type(viewType, divider);
      a.recycle();
      return this;
    }

    public Builder type(int viewType, @DrawableRes int drawableResId) {
      mDividerViewTypeMap.put(viewType, ContextCompat.getDrawable(mContext, drawableResId));
      return this;
    }

    public Builder type(int viewType, Drawable drawable) {
      mDividerViewTypeMap.put(viewType, drawable);
      return this;
    }

    public Builder first(@DrawableRes int drawableResId) {
      first(ContextCompat.getDrawable(mContext, drawableResId));
      return this;
    }

    public Builder first(Drawable drawable) {
      mFirstDrawable = drawable;
      return this;
    }

    public Builder last(@DrawableRes int drawableResId) {
      last(ContextCompat.getDrawable(mContext, drawableResId));
      return this;
    }

    public Builder last(Drawable drawable) {
      mLastDrawable = drawable;
      return this;
    }

    public HorizontalItemDecoration create() {
      return new HorizontalItemDecoration(mDividerViewTypeMap, mFirstDrawable,
          mLastDrawable, mCommonDrawable,
          mDrawOnTop);
    }
  }
}