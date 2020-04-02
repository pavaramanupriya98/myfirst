package com.moraspirit.moraspiritapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.moraspirit.moraspiritapp.article.ArticleFragment;
import com.moraspirit.moraspiritapp.menuitem.AdapterMenu;
import com.moraspirit.moraspiritapp.menuitem.MenuItem;
import com.moraspirit.moraspiritapp.points.PointsFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout sliderLayout;
    private RecyclerView menuView;
    private AdapterMenu adapterMenu;
    private Context context;
    ImageView bgapp;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       /* context = this;
        menuView = findViewById(R.id.menu);
        sliderLayout = findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Mountains during sunset. Beautiful natural landscape in the summer time", "https://image.shutterstock.com/image-photo/mountains-during-sunset-beautiful-natural-600w-407021107.jpg");
        url_maps.put("Big Bang Theory", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823_960_720.jpg");
        url_maps.put("DogWolfYelpMoon", "https://cdn.pixabay.com/photo/2015/02/24/15/41/dog-647528_960_720.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
            menuView.setLayoutManager(gridLayoutManager);

            List<MenuItem> menuItems = Arrays.asList(
                    new MenuItem("Articles", ArticleFragment.class,R.drawable.article_lgjpg, ResourcesCompat.getColor(getResources(),R.color.black,null)),
                    new MenuItem("Points Tables", PointsFragment.class,R.drawable.scoreboard,ResourcesCompat.getColor(getResources(),R.color.white,null))
                    ,new MenuItem("Matches", PointsFragment.class,R.drawable.matches,ResourcesCompat.getColor(getResources(),R.color.white,null))
            );
            adapterMenu = new AdapterMenu(menuItems,context);
            menuView.setAdapter(adapterMenu);
        }*/

        frombottom = AnimationUtils.loadAnimation(this,R.animator.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(1500);


        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(1300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);



    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
