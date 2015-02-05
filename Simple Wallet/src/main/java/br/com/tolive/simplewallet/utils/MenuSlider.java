package br.com.tolive.simplewallet.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;

public class MenuSlider {

    public static final int DURATION = 500;
    private final ActionBarActivity activity;
    private final View content;
    private final FrameLayout menuContainer;
    private final boolean removeAd;
    private View menuShadow;

    public MenuSlider(ActionBarActivity activity) {
        this.activity = activity;

        // We get the View of the Activity
        this.content = (View) activity.findViewById(android.R.id.content).getParent();

        // And its parent
        ViewGroup parent = (ViewGroup) this.content.getParent();

        // The container for the menu Fragment is added to the parent. We set an id so we can perform FragmentTransactions later on
        this.menuContainer = new FrameLayout(this.activity);
        this.menuContainer.setId(R.id.flMenuContainer);

        // We set visibility to GONE because the menu is initially hidden
        this.menuContainer.setVisibility(View.GONE);
        this.menuContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Create view to be a shadow
        this.menuShadow = new View(this.activity);
        this.menuShadow.setVisibility(View.GONE);
        this.menuShadow.setBackgroundColor(this.activity.getResources().getColor(R.color.transparent));
        this.menuShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });

        parent.addView(this.menuShadow);
        parent.addView(this.menuContainer);

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean(Constantes.SP_KEY_REMOVE_AD, true);
        //editor.commit();
        removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
    }

    public <T extends Fragment> void setMenuFragment(Class<T> cls) {
        Fragment fragment = Fragment.instantiate(this.activity, cls.getName());
        setMenuFragment(fragment);
    }

    public void setMenuFragment(Fragment fragment) {
        FragmentManager manager = this.activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.flMenuContainer, fragment);
        transaction.commit();
    }

    public boolean isMenuVisible() {
        return this.menuContainer.getVisibility() == View.VISIBLE;
    }

    // We pass the width of the menu in dip to showMenu()
    public void showMenu() {

        // We convert the width from dip into pixels
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

        float width = displayMetrics.widthPixels;
        final int menuWidth = (int) width - LayoutHelper.dpToPixel(this.activity, 56);

        // We move the Activity out of the way
        //slideTo(menuWidth, 0);

        // We have to take the height of the status bar at the top into account!
        Rect rectangle = new Rect();
        Window window = this.activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        final int statusBarHeight = rectangle.top;

        // These are the LayoutParams for the menu Fragment
        FrameLayout.LayoutParams fragmentParams;
        FrameLayout.LayoutParams shadowParams;
        if(removeAd) {
            fragmentParams = new FrameLayout.LayoutParams(menuWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            shadowParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        } else {
            float height = displayMetrics.heightPixels;
            fragmentParams = new FrameLayout.LayoutParams(menuWidth, (int) (height - LayoutHelper.dpToPixel(activity,50) - statusBarHeight));
            shadowParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (height - LayoutHelper.dpToPixel(activity,50) - statusBarHeight));
        }

        // We put a top margin on the menu Fragment container which is equal to the status bar height
        //fragmentParams.setMargins(0, 0, 0, 0);
        this.menuContainer.setLayoutParams(fragmentParams);
        this.menuShadow.setLayoutParams(shadowParams);

        // Perform the animation only if the menu is not visible
        if(!isMenuVisible()) {

            // Visibility of the menu container View is set to VISIBLE
            this.menuContainer.setVisibility(View.VISIBLE);
            this.menuShadow.setVisibility(View.VISIBLE);
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(this.menuShadow,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    this.activity.getResources().getColor(R.color.transparent),
                    this.activity.getResources().getColor(R.color.transparent_black));
            backgroundColorAnimator.setDuration(DURATION);
            backgroundColorAnimator.start();

            // The menu slides in from the right
            TranslateAnimation animation = new TranslateAnimation(-menuWidth, 0, 0, 0);
            animation.setDuration(DURATION);
            this.menuContainer.startAnimation(animation);
        }
    }

    public void hideMenu() {

        // We can only hide the menu if it is visible
        if(isMenuVisible()) {

            // We slide the Activity back to its original position
            //slideTo(0, 0);

            // We need the width of the menu to properly animate it
            final int menuWidth = this.menuContainer.getWidth();

            // Now we need an extra animation for the menu fragment container
            TranslateAnimation menuAnimation = new TranslateAnimation(0, -menuWidth, 0, 0);
            menuAnimation.setDuration(DURATION);

            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(menuShadow,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    activity.getResources().getColor(R.color.transparent_black),
                    activity.getResources().getColor(R.color.transparent));
            backgroundColorAnimator.setDuration(DURATION);
            backgroundColorAnimator.start();

            menuAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // As soon as the hide animation is finished we set the visibility of the fragment container back to GONE
                    menuContainer.setVisibility(View.GONE);
                    menuShadow.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.menuContainer.startAnimation(menuAnimation);
        }
    }

    public void slideTo(int x, int y) {

        // We get the current position of the Activity
        final int currentX = getActivityPositionX();
        final int currentY = getActivityPositionY();

        // The new position is set
        setActivityPosition(x, y);

        // We animate the Activity to slide from its previous position to its new position
        TranslateAnimation animation = new TranslateAnimation(currentX - x, 0, currentY - y, 0);
        animation.setDuration(DURATION);
        this.content.startAnimation(animation);
    }

    public void setActivityPosition(int x, int y) {
        // With this if statement we can check if the devices API level is above Honeycomb or below
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // On Honeycomb or above we set a margin
            FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) this.content.getLayoutParams();
            contentParams.setMargins(x, y, -x, -y);
            this.content.setLayoutParams(contentParams);
        } else {
            // And on devices below Honeycomb we set a padding
            this.content.setPadding(x, y, -x, -y);
        }
    }

    public int getActivityPositionX() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // On Honeycomb or above we return the left margin
            FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) this.content.getLayoutParams();
            return contentParams.leftMargin;
        } else {
            // On devices below Honeycomb we return the left padding
            return this.content.getPaddingLeft();
        }
    }

    public int getActivityPositionY() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // On Honeycomb or above we return the top margin
            FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) this.content.getLayoutParams();
            return contentParams.topMargin;
        } else {
            // On devices below Honeycomb we return the top padding
            return this.content.getPaddingTop();
        }
    }
}