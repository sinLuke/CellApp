package com.cellgroup.cellapp.ui.today.document;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.cellgroup.cellapp.models.AnimationItem;

import static java.lang.StrictMath.abs;

@SuppressLint("AppCompatCustomView")
public class AnimationItemImageView extends ImageView {

    public AnimationItem animationitem;

    private float canvasWidth;
    private float canvasHeight;

    public boolean isPositionCorrect() {
        return abs(getCurrentX() - animationitem.END_POSITION_X) < AnimationItem.tolerant && abs(getCurrentY() - animationitem.END_POSITION_Y) < AnimationItem.tolerant;
    }

    private double getCurrentX() {
        return (double) (getX() / canvasWidth);
    }

    private double getCurrentY() {
        return (double) (getY() / canvasHeight);
    }

    public AnimationItemImageView(Context context, AnimationItem animationitem, float canvasWidth, float canvasHeight) {
        super(context);
    }
}
