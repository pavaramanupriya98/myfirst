package com.moraspirit.moraspiritapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;


import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moraspirit.moraspiritapp.about.AboutFragment;
import com.moraspirit.moraspiritapp.article.Article;
import com.moraspirit.moraspiritapp.article.ArticleFragment;
import com.moraspirit.moraspiritapp.article.ArticleViewFragment;
import com.moraspirit.moraspiritapp.notification.MyFirebaseMessagingService;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.moraspirit.moraspiritapp.sports.MatchFragment;
import com.moraspirit.moraspiritapp.year.FragmentYears;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.moraspirit.moraspiritapp.R.id.mainView;

public class MainActivity extends FragmentActivity {
    private static String TAG ="MainActivity";

    private static CustomBottomAppBar bottomAppBar;
    private static FloatingActionButton floatingActionButton;
    private static ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int prevFragmentIindex = 0;
    private TextView title;
    private static TextView year;
    private boolean firstTime;
    private static FragmentYears fragmentYears;

    private ArticleFragment articleFragment;
    private MatchFragment matchFragment;
    private FragmentRanking fragmentRanking;
    private AboutFragment aboutFragment;

    private static final int NUM_PAGES = 4;

    private static View mainView;

    private static Context context;
    private static MainActivity thisAct;

    private static boolean snackTime = true;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("userABC");

        firstTime = true;

        mainView = findViewById(R.id.mainView);

        context = getBaseContext();
        thisAct = this;

        articleFragment = new ArticleFragment();
        matchFragment = new MatchFragment();
        fragmentRanking = new FragmentRanking();
        aboutFragment = new AboutFragment();

        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        bottomAppBar = findViewById(R.id.bar);
        floatingActionButton = findViewById(R.id.fav);
        viewPager = findViewById(R.id.viewPager);

        openFABTimeout();
        openFromNotification();
        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);
        pagerAdapter = new SlidePagerAdaptor(getSupportFragmentManager(),FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);
        //Initial floating button animation


        //Save fragment state up to change 3 different fragments
        viewPager.setOffscreenPageLimit(3);

        fragmentYears = new FragmentYears();
        year.setOnClickListener(v -> fragmentYears.show(getSupportFragmentManager(),"select_year_dialog_fragment"));

        //Title changing listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        if (!firstTime) {
                            title.setText("Articles");
                        } else {
                            title.setText("MoraSpirit");
                            firstTime = false;
                        }

                        floatingActionButton.setImageResource(R.drawable.ic_folded_newspaper);
                        year.setVisibility(View.GONE);
                        break;
                    case 1:
                        title.setText("Sports");
                        floatingActionButton.setImageResource(R.drawable.ic_soccer_ball_variant);
                        year.setVisibility(View.GONE);
                        break;
                    case 2:
                        title.setText("Points in ");
                        floatingActionButton.setImageResource(R.drawable.ic_medal);
                        year.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        title.setText("About");
                        floatingActionButton.setImageResource(R.drawable.ic_info_outline_black_24dp);
                        year.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        floatingActionButton.setOnLongClickListener(v -> {
            Constants.openFacebookIntent(this);
//            bottomAppBar.hide();
            return true;
        });
        floatingActionButton.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == NUM_PAGES - 1) {
                setFragment(0);
            } else if (viewPager.getCurrentItem() == NUM_PAGES - 2) {
                setFragment(0);
            } else {
                setFragment(viewPager.getCurrentItem() + 1);
            }

        });




        bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            floatingActionButton.show();

            if (menuItem.getItemId() == R.id.about) {
                if (viewPager.getCurrentItem() != NUM_PAGES - 1) {
                    setFragment(NUM_PAGES - 1);
                } else {
                    setFragment(prevFragmentIindex);
                }
            }
            return false;
        });



    }
    public static void showShowCase(){
        String showCaseKey = "SHOW_CASE_KEY";
        boolean firstTime = thisAct.getPreferences(MODE_PRIVATE).getBoolean(showCaseKey,true);
        if (!firstTime) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TapTargetView.showFor(thisAct,                 // `this` is an Activity
                        TapTarget.forView(floatingActionButton, "Tap to find more!", "> Upcoming matches \n> Match results \n> Points table")
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)              // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(60),                  // Specify the target radius (in dp)
                        new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                            @Override
                            public void onTargetClick(TapTargetView view) {
                                super.onTargetClick(view);      // This call is optional
                                thisAct.getPreferences(MODE_PRIVATE).edit().putBoolean(showCaseKey, false).apply();
                            }
                        });
            }
        },1000);

    }



    public void openFABTimeout(){
        new Handler().postDelayed(() -> {
            floatingActionButton.show();
        }, 300);
    }


    public static void loadFragmentYear(){
        fragmentYears.loadYears(context);
    }

    public void openFromNotification(){
        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().getString("id")!= null && getIntent().getExtras().getString("id").equals("article")){
                Map map = new HashMap();
                Set<String> ks = getIntent().getExtras().keySet();
                for (String key : ks) {
                    if (getIntent().getExtras().get(key)  instanceof String)
                        map.put(key, getIntent().getExtras().getString(key));
                }
                Article article = MyFirebaseMessagingService.getArticle(map);
                Intent intent = new Intent(MainActivity.this, ArticleViewFragment.class);
                ArticleViewFragment.setArticle(article);
                startActivity(intent);
            }
        }
    }

    public static View getMainView() {
        return mainView;
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 0) {
            floatingActionButton.hide();
            super.onBackPressed();
        } else if (viewPager.getCurrentItem() == NUM_PAGES - 1) {
            setFragment(0);
        } else {
            setFragment(viewPager.getCurrentItem() - 1);
        }
    }

    public void setFragment(int position) {
        prevFragmentIindex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(position);
    }
    public static CustomBottomAppBar getAppBar(){
        return MainActivity.bottomAppBar;
    }
    public static FloatingActionButton getFav(){
        return MainActivity.floatingActionButton;
    }

    public static void setYearFromFragment(String year){
        setYear(year);
        fragmentYears.dismiss();
    }
    public static void setYear(String year){
        MainActivity.year.setText(year);
    }

    public class SlidePagerAdaptor extends FragmentStatePagerAdapter {


        public SlidePagerAdaptor(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    title.setText("Articles");
                    return articleFragment;
                case 1:
//                    title.setText("Gallery");
                    return matchFragment;
                case 2:
//                    title.setText("Rankings");
                    return fragmentRanking;
                case 3:
//                    title.setText("About");
                    return aboutFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    public static void makeToast(final Context context,
                                 final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }
    public static void makeSnackBar(String text,Boolean action){
        if(!snackTime) return;
        Snackbar snackbar = Snackbar.make(getMainView(),text,Snackbar.LENGTH_LONG).setAnchorView(floatingActionButton);

        if(action){
            snackbar.setAction("Retry", v -> updateCurrentFragment());
        }
        snackbar.show();
        setFalseSnackTime();

    }
    public static void setFalseSnackTime(){
        snackTime = false;
        new Handler(Looper.getMainLooper()).postDelayed(() -> snackTime = true,3000);

    }

//    public void animate(){
//        final ObjectAnimator animator = ObjectAnimator.ofInt(floatingActionButton, "backgroundTint", Color.rgb(0, 121, 107), Color.rgb(226, 143, 34));
//        animator.setDuration(2000L);
//        animator.setEvaluator(new ArgbEvaluator());
//        animator.setInterpolator(new DecelerateInterpolator(2));
//        animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatedValue = (int) animation.getAnimatedValue();
//                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
//            }
//        });
//        animator.start();
//    }
    public static void updateCurrentFragment(){
        switch (viewPager.getCurrentItem()){
            case 0:
                ((ArticleFragment)((SlidePagerAdaptor)viewPager.getAdapter()).getItem(0)).onRefresh();
            case 1:
            case 2:
                FragmentRanking.updateCurrentFragment();
            case 3:
        }
    }
    public static boolean checkDataAvailability(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            if(connectivityManager==null) return false;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnected();
        }

        assert connectivityManager != null;

        Network[] networks = connectivityManager.getAllNetworks();
        boolean hasInternet = false;
        if(networks.length>0){
            for(Network network :networks){
                NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
                if(nc!=null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true;
            }
        }
        return hasInternet;

    }
}
