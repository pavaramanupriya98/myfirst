
package com.moraspirit.moraspiritapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_screen);
//        splashInit();
        openMain();
        finish();
    }

//    private void splashInit() {
//        ProgressBar mProgressBar = findViewById(R.id.progress);
//        Sprite doubleBounce = new ChasingDots();
//        doubleBounce.setColor(getResources().getColor(R.color.white));
//        mProgressBar.setIndeterminateDrawable(doubleBounce);
//    }

    public void setExtras(Intent intent) {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("id") != null && getIntent().getExtras().getString("id").equals("article")) {

                Map map = new HashMap();
                Set<String> ks = getIntent().getExtras().keySet();
                for (String key : ks) {
                    if (getIntent().getExtras().get(key) instanceof String)
                        intent.putExtra(key,getIntent().getExtras().get(key).toString());
//                        map.put(key, getIntent().getExtras().getString(key));
                }
//                article article = MyFirebaseMessagingService.getArticle(map);
//                Intent intent = new Intent(Sp.this,ArticleViewFragment.class);
//                ArticleViewFragment.setArticle(article);
//                startActivity(intent);
            }
        }
    }

    public void openMain() {
        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
        setExtras(intent);
        startActivity(intent);
    }
}

