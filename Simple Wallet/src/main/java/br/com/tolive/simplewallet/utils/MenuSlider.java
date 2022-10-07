package br.com.tolive.simplewallet.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;

public class MenuSlider {

    public static final int DURATION = 500;
    // Query Scroller every 16 miliseconds
    private static final int QUERY_INTERVAL = 16;

    private final AppCompatActivity activity;
    private final View content;
    private final FrameLayout menuContainer;
    private final boolean removeAd;
    private View menuShadow;

    private boolean isDragging = false;
    private int prevX = 0;
    private int menuPosition = 0;
    private int lastDiffX = 0;
    private Scroller menuScroller;
    private Runnable menuRunnable = new MenuRunnable();
    private Handler menuHandler = new Handler();
    private final float width;
    private final float height;
    private final int menuSize;
    // The state of menu
    private enum MenuState {
        HIDING,
        HIDDEN,
        SHOWING,
        SHOWN,
    };

    private MenuState currentMenuState = MenuState.HIDDEN;

    public MenuSlider(final AppCompatActivity activity) {
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

        // Scroller is used to facilitate animation
        menuScroller = new Scroller(activity,
                new EaseInInterpolator());


        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.d("TAG", "width" + width);
        menuSize = ((int) (width - LayoutHelper.dpToPixel(activity, 56)));

        this.content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Do nothing if sliding is in progress
                if(currentMenuState == MenuState.HIDING || currentMenuState == MenuState.SHOWING)
                    return false;

                // getRawX returns X touch point corresponding to screen
                // getX sometimes returns screen X, sometimes returns content View X
                int curX = (int)event.getRawX();
                Log.d("TAG", "curX " + curX);
                if(!isMenuVisible()) {
                    if (curX > LayoutHelper.dpToPixel(activity, 40) && !isDragging) {
                        Log.d("TAG", "curX > " + LayoutHelper.dpToPixel(activity, 40));
                        return false;
                    }
                } else {
                    if (curX < menuSize && !isDragging) {
                        Log.d("TAG", "curX < " + menuSize);
                        return false;
                    }
                }

                int diffX = 0;

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("TAG", "Down x " + curX);
                        //menuContainer.offsetLeftAndRight(-menuSize);

                        prevX = curX;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        Log.d("TAG", "Move x " + curX);

                        // Set menu to Visible when user start dragging the content View
                        if(!isDragging) {
                            isDragging = true;
                            if(!isMenuVisible()) {
                                FrameLayout.LayoutParams fragmentParams;
                                Rect rectangle = new Rect();
                                Window window = activity.getWindow();
                                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                                final int statusBarHeight = rectangle.top;
                                if (removeAd) {
                                    fragmentParams = new FrameLayout.LayoutParams(menuSize, ViewGroup.LayoutParams.MATCH_PARENT);
                                    //shadowParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                                } else {
                                    fragmentParams = new FrameLayout.LayoutParams(menuSize, (int) (height - LayoutHelper.dpToPixel(activity, 50) - statusBarHeight));
                                    //shadowParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (height - LayoutHelper.dpToPixel(activity,50) - statusBarHeight));
                                }
                                menuContainer.setLayoutParams(fragmentParams);
                                menuContainer.setTranslationX(-menuSize);
                                menuContainer.setVisibility(View.VISIBLE);
                            }
                        }

                        // How far we have moved since the last position
                        diffX = curX - prevX ;
                        Log.d("TAG", "diffX " + diffX);

                        // Prevent user from dragging beyond border
                        if(menuPosition + diffX <= 0) {
                            // Don't allow dragging beyond left border
                            // Use diffX will make content cross the border, so only translate by -menuPosition
                            diffX = 0;
                            Log.d("TAG", "menuPosition + diffX <= 0");
                        } else if(menuPosition + diffX > menuSize) {
                            // Don't allow dragging beyond menu width
                            diffX = 0;
                            Log.d("TAG", "menuPosition + diffX (" + (menuPosition + diffX) + ") > menuSize" +
                                    " (" + menuSize + ")");
                        }

                        // Translate content View accordingly
                        menuContainer.offsetLeftAndRight(diffX);

                        menuPosition += diffX;
                        Log.d("TAG", "menuPosition " + menuPosition);

                        // Invalite this whole MainLayout, causing onLayout() to be called
                        //content.invalidate();

                        prevX = curX;
                        lastDiffX = diffX;
                        return true;

                    case MotionEvent.ACTION_UP:
                        Log.d("TAG", "lastDiffX " + lastDiffX);

                        // Start scrolling
                        // Remember that when content has a chance to cross left border, lastDiffX is set to 0
                        if(lastDiffX >= 0 && menuPosition > menuSize/2) {
                            // User wants to show menu
                            currentMenuState = MenuState.SHOWING;

                            // No need to set to Visible, because we have set to Visible in ACTION_MOVE
                            //menu.setVisibility(View.VISIBLE);

                            Log.d("TAG", "Up menuPosition " + menuPosition);

                            // Start scrolling from menuPosition
                            menuScroller.startScroll(menuPosition, 0, menuSize - menuPosition,
                                    0, DURATION);
                        } else{
                            // User wants to hide menu
                            currentMenuState = MenuState.HIDING;
                            menuScroller.startScroll(menuPosition, 0, -menuPosition,
                                    0, DURATION);
                        }


                        // Begin querying
                        menuHandler.postDelayed(menuRunnable, QUERY_INTERVAL);

                        // Done dragging
                        isDragging = false;
                        prevX = 0;
                        lastDiffX = 0;
                        //menuPosition = 0;
                        return true;

                    default:
                        break;
                }

                return false;
            }
        });
    }

    // Make scrolling more natural. Move more quickly at the end
    // See the formula here http://cyrilmottier.com/2012/05/22/the-making-of-prixing-fly-in-app-menu-part-1/
    protected class EaseInInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float t) {
            return (float)Math.pow(t-1, 5) + 1;
        }

    }


    // Query Scroller
    protected class MenuRunnable implements Runnable {
        @Override
        public void run() {
            boolean isScrolling = menuScroller.computeScrollOffset();
            adjustContentPosition(isScrolling);
        }
    }

    // Adjust content View position to match sliding animation
    private void adjustContentPosition(boolean isScrolling) {
        int scrollerXOffset = menuScroller.getCurrX();

        Log.d("TAG", "scrollerOffset " + scrollerXOffset);
        Log.d("TAG", "menuPosition " + menuPosition);

        // Translate content View accordingly
        menuContainer.offsetLeftAndRight(scrollerXOffset - menuPosition);

        menuPosition = scrollerXOffset;

        // Check if animation is in progress
        if (isScrolling)
            menuHandler.postDelayed(menuRunnable, QUERY_INTERVAL);
        else
            this.onMenuSlidingComplete();
    }

    // Called when sliding is complete
    private void onMenuSlidingComplete() {
        //menuPosition = 0;
        switch (currentMenuState) {
            case SHOWING:
                currentMenuState = MenuState.SHOWN;
                menuPosition = (int) (this.width - LayoutHelper.dpToPixel(activity, 56));
                break;
            case HIDING:
                currentMenuState = MenuState.HIDDEN;
                menuContainer.setVisibility(View.GONE);
                menuPosition = 0;
                break;
            default:
                return;
        }
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
        menuContainer.setTranslationX(-menuSize);

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
            menuContainer.setTranslationX(0);
            TranslateAnimation animation = new TranslateAnimation(-menuWidth, 0, 0, 0);
            animation.setDuration(DURATION);
            this.menuContainer.startAnimation(animation);
            this.menuPosition = menuSize;
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
            this.menuPosition = 0;
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