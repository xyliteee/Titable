package com.Xyliteee.titable;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

public class Animators {
    public static void CardViewFloat(CardView layout)
    {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout, "translationX", -5f, 5f);
        animatorX.setDuration(1000);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(layout, "translationY", -5f, 5f);
        animatorY.setDuration(2000);
        animatorY.setRepeatCount(ValueAnimator.INFINITE);
        animatorY.setRepeatMode(ValueAnimator.REVERSE);
        animatorX.start();
        animatorY.start();
    }

    public static void CircleMove(ImageView Image,int xTime,int yTime)
    {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(Image, "translationX", -5f, 5f);
        animatorX.setDuration(xTime);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(Image, "translationY", -5f, 5f);
        animatorY.setDuration(yTime);
        animatorY.setRepeatCount(ValueAnimator.INFINITE);
        animatorY.setRepeatMode(ValueAnimator.REVERSE);
        animatorX.start();
        animatorY.start();
    }
    public static void ControlBoxFloat(CardView cardView){
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 1000f, 0f);
        translationAnimator.setDuration(1000);
        TimeInterpolator interpolator = new OvershootInterpolator(0.5f);
        translationAnimator.setInterpolator(interpolator);
        translationAnimator.start();
    }

    public static void ControlBoxDown(CardView cardView){
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 0f, 1000f);
        translationAnimator.setDuration(1000);
        TimeInterpolator interpolator = new AccelerateInterpolator(0.5f);
        translationAnimator.setInterpolator(interpolator);
        translationAnimator.start();
    }

    public static void BackGroundMove(ImageView cardView){
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 50f, 0f);
        translationAnimator.setDuration(1000);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 0.1f);
        alphaAnimator.setDuration(2000);
        TimeInterpolator interpolator = new AccelerateInterpolator(0.5f);
        translationAnimator.setInterpolator(interpolator);
        alphaAnimator.setInterpolator(interpolator);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.start();

    }

    public static void ButtonPress(Button button){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.9f, 1f);
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }
}
