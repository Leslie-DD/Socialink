package com.leslie.socialink.utils;

import static android.Manifest.permission.EXPAND_STATUS_BAR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.drawerlayout.widget.DrawerLayout;

import com.leslie.socialink.SocialinkApplication;

import java.lang.reflect.Method;

public final class BarUtils {

    ///////////////////////////////////////////////////////////////////////////
    // status bar
    ///////////////////////////////////////////////////////////////////////////

    private static final int DEFAULT_ALPHA = 112;
    private static final String TAG_COLOR = "TAG_COLOR";
    private static final String TAG_ALPHA = "TAG_ALPHA";
    private static final String TAG_OFFSET = "TAG_OFFSET";
    private static final int KEY_OFFSET = -123;

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the status bar's height.
     *
     * @return the status bar's height
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * Set the status bar's visibility.
     *
     * @param activity  The activity.
     * @param isVisible True to set status bar visible, false otherwise.
     */
    public static void setStatusBarVisibility(@NonNull final Activity activity,
                                              final boolean isVisible) {
        setStatusBarVisibility(activity.getWindow(), isVisible);
    }

    /**
     * Set the status bar's visibility.
     *
     * @param window    The window.
     * @param isVisible True to set status bar visible, false otherwise.
     */
    public static void setStatusBarVisibility(@NonNull final Window window,
                                              final boolean isVisible) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            showColorView(window);
            showAlphaView(window);
            addMarginTopEqualStatusBarHeight(window);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hideColorView(window);
            hideAlphaView(window);
            subtractMarginTopEqualStatusBarHeight(window);
        }
    }

    /**
     * Return whether the status bar is visible.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isStatusBarVisible(@NonNull final Activity activity) {
        int flags = activity.getWindow().getAttributes().flags;
        return (flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
    }

    /**
     * Set the status bar's light mode.
     *
     * @param activity    The activity.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    public static void setStatusBarLightMode(@NonNull final Activity activity,
                                             final boolean isLightMode) {
        setStatusBarLightMode(activity.getWindow(), isLightMode);
    }

    /**
     * Set the status bar's light mode.
     *
     * @param window      The window.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    public static void setStatusBarLightMode(@NonNull final Window window,
                                             final boolean isLightMode) {
        View decorView = window.getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        if (isLightMode) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            visibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(visibility);
    }

    /**
     * Add the top margin size equals status bar's height for view.
     *
     * @param view The view.
     */
    public static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        view.setTag(TAG_OFFSET);
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, true);
    }

    /**
     * Subtract the top margin size equals status bar's height for view.
     *
     * @param view The view.
     */
    public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin - getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, false);
    }

    private static void addMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        addMarginTopEqualStatusBarHeight(withTag);
    }

    private static void subtractMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        subtractMarginTopEqualStatusBarHeight(withTag);
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color) {
        setStatusBarColor(activity, color, DEFAULT_ALPHA, false);
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param alpha    The status bar's alpha which isn't the same as alpha in the color.
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarColor(activity, color, alpha, false);
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param alpha    The status bar's alpha which isn't the same as alpha in the color.
     * @param isDecor  True to add fake status bar in DecorView,
     *                 false to add fake status bar in ContentView.
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        hideAlphaView(activity);
        transparentStatusBar(activity);
        addStatusBarColor(activity, color, alpha, isDecor);
    }

    /**
     * Set the status bar's color.
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar,
                                         @ColorInt final int color) {
        setStatusBarColor(fakeStatusBar, color, DEFAULT_ALPHA);
    }

    /**
     * Set the status bar's color.
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param alpha         The status bar's alpha which isn't the same as alpha in the color.
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        fakeStatusBar.setVisibility(View.VISIBLE);
        transparentStatusBar((Activity) fakeStatusBar.getContext());
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight();
        fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
    }

    /**
     * Set the status bar's alpha.
     *
     * @param activity The activity.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity) {
        setStatusBarAlpha(activity, DEFAULT_ALPHA, false);
    }

    /**
     * Set the status bar's alpha.
     *
     * @param activity The activity.
     * @param alpha    The status bar's alpha.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarAlpha(activity, alpha, false);
    }

    /**
     * Set the status bar's alpha.
     *
     * @param activity The activity.
     * @param alpha    The status bar's alpha.
     * @param isDecor  True to add fake status bar in DecorView,
     *                 false to add fake status bar in ContentView.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        hideColorView(activity);
        transparentStatusBar(activity);
        addStatusBarAlpha(activity, alpha, isDecor);
    }

    /**
     * Set the status bar's alpha.
     *
     * @param fakeStatusBar The fake status bar view.
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar) {
        setStatusBarAlpha(fakeStatusBar, DEFAULT_ALPHA);
    }

    /**
     * Set the status bar's alpha.
     *
     * @param fakeStatusBar The fake status bar view.
     * @param alpha         The status bar's alpha.
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        fakeStatusBar.setVisibility(View.VISIBLE);
        transparentStatusBar((Activity) fakeStatusBar.getContext());
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight();
        fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    }

    /**
     * Set the status bar's color for DrawerLayout.
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                final boolean isTop) {
        setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, DEFAULT_ALPHA, isTop);
    }

    /**
     * Set the status bar's color for DrawerLayout.
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param alpha         The status bar's alpha which isn't the same as alpha in the color.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        drawer.setFitsSystemWindows(false);
        transparentStatusBar(activity);
        setStatusBarColor(fakeStatusBar, color, isTop ? alpha : 0);
        for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
            drawer.getChildAt(i).setFitsSystemWindows(false);
        }
        if (isTop) {
            hideAlphaView(activity);
        } else {
            addStatusBarAlpha(activity, alpha, false);
        }
    }

    /**
     * Set the status bar's alpha for DrawerLayout.
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        drawerLayout
     * @param fakeStatusBar The fake status bar view.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                final boolean isTop) {
        setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, DEFAULT_ALPHA, isTop);
    }

    /**
     * Set the status bar's alpha for DrawerLayout.
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        drawerLayout
     * @param fakeStatusBar The fake status bar view.
     * @param alpha         The status bar's alpha.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        drawer.setFitsSystemWindows(false);
        transparentStatusBar(activity);
        setStatusBarAlpha(fakeStatusBar, isTop ? alpha : 0);
        for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
            drawer.getChildAt(i).setFitsSystemWindows(false);
        }
        if (isTop) {
            hideAlphaView(activity);
        } else {
            addStatusBarAlpha(activity, alpha, false);
        }
    }

    private static void addStatusBarColor(final Activity activity,
                                          final int color,
                                          final int alpha,
                                          boolean isDecor) {
        ViewGroup parent = isDecor ?
                (ViewGroup) activity.getWindow().getDecorView() :
                (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        } else {
            parent.addView(createColorStatusBarView(activity, color, alpha));
        }
    }

    private static void addStatusBarAlpha(final Activity activity,
                                          final int alpha,
                                          boolean isDecor) {
        ViewGroup parent = isDecor ?
                (ViewGroup) activity.getWindow().getDecorView() :
                (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        } else {
            parent.addView(createAlphaStatusBarView(activity, alpha));
        }
    }

    private static void hideColorView(final Activity activity) {
        hideColorView(activity.getWindow());
    }

    private static void hideAlphaView(final Activity activity) {
        hideAlphaView(activity.getWindow());
    }

    private static void hideColorView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void hideAlphaView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void showColorView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static void showAlphaView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static int getStatusBarColor(final int color, final int alpha) {
        if (alpha == 0) return color;
        float a = 1 - alpha / 255f;
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return Color.argb(255, red, green, blue);
    }

    private static View createColorStatusBarView(final Context context,
                                                 final int color,
                                                 final int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        statusBarView.setTag(TAG_COLOR);
        return statusBarView;
    }

    private static View createAlphaStatusBarView(final Context context, final int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setTag(TAG_ALPHA);
        return statusBarView;
    }

    private static void transparentStatusBar(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // action bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the action bar's height.
     *
     * @return the action bar's height
     */
    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (SocialinkApplication.getInstance().getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(
                    tv.data, SocialinkApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics()
            );
        }
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // notification bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Set the notification bar's visibility.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />}</p>
     *
     * @param isVisible True to set notification bar visible, false otherwise.
     */
    @RequiresPermission(EXPAND_STATUS_BAR)
    public static void setNotificationBarVisibility(final boolean isVisible) {
        String methodName;
        if (isVisible) {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "expand" : "expandNotificationsPanel";
        } else {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
        }
        invokePanels(methodName);
    }

    private static void invokePanels(final String methodName) {
        try {
            @SuppressLint("WrongConstant")
            Object service = SocialinkApplication.getInstance().getApplicationContext().getSystemService("statusbar");
            @SuppressLint("PrivateApi")
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName);
            expand.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // navigation bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    public static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * Set the navigation bar's visibility.
     *
     * @param activity  The activity.
     * @param isVisible True to set navigation bar visible, false otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setNavBarVisibility(@NonNull final Activity activity, boolean isVisible) {
        setNavBarVisibility(activity.getWindow(), isVisible);
    }

    /**
     * Set the navigation bar's visibility.
     *
     * @param window    The window.
     * @param isVisible True to set navigation bar visible, false otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void setNavBarVisibility(@NonNull final Window window, boolean isVisible) {
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        final View decorView = window.getDecorView();
        if (isVisible) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~uiOptions);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
        }
    }

    /**
     * Return whether the navigation bar visible.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarVisible(@NonNull final Activity activity) {
        return isNavBarVisible(activity.getWindow());
    }

    /**
     * Return whether the navigation bar visible.
     *
     * @param window The window.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarVisible(@NonNull final Window window) {
        View decorView = window.getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        return (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
    }

    /**
     * Set the navigation bar's color.
     *
     * @param activity The activity.
     * @param color    The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Activity activity, @ColorInt final int color) {
        setNavBarColor(activity.getWindow(), color);
    }

    /**
     * Set the navigation bar's color.
     *
     * @param window The window.
     * @param color  The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Window window, @ColorInt final int color) {
        window.setNavigationBarColor(color);
    }

    /**
     * Return the color of navigation bar.
     *
     * @param activity The activity.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Activity activity) {
        return getNavBarColor(activity.getWindow());
    }

    /**
     * Return the color of navigation bar.
     *
     * @param window The window.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Window window) {
        return window.getNavigationBarColor();
    }

    /**
     * Return whether the navigation bar visible.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSupportNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) SocialinkApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //noinspection ConstantConditions
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y || realSize.x != size.x;
        }
        boolean menu = ViewConfiguration.get(SocialinkApplication.getInstance().getInstance().getApplicationContext()).hasPermanentMenuKey();
        boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !menu && !back;
    }
}
