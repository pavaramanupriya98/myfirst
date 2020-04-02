package com.moraspirit.moraspiritapp.article;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moraspirit.moraspiritapp.CustomBottomAppBar;
import com.moraspirit.moraspiritapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class ArticleViewFragment extends AppCompatActivity {
    private final String URL_Base = "https://www.moraspirit.com/news/";


    private ImageView imageView;
    private static Article article;
    private static Drawable drawable;
    private FloatingActionButton fab;
    private CustomBottomAppBar appBar;
    private HtmlTextView content;
    private TextView category, date, author, editor,title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view_fragment);

        category = findViewById(R.id.articleCategory);
        date = findViewById(R.id.articleDate);
        author = findViewById(R.id.articleAuthor);
        editor = findViewById(R.id.articleEditor);
        title = findViewById(R.id.articleTitle);

        imageView = findViewById(R.id.articleImage);
        content = findViewById(R.id.articleContent);
        fab = findViewById(R.id.articleFav);

        appBar = findViewById(R.id.articleBar);
        appBar.replaceMenu(R.menu.article_view_bottom_menu);
        appBar.setOnMenuItemClickListener(item -> {
            supportFinishAfterTransition();
            return false;
        });
        if(article!=null) {
            category.setText(ArticleFragment.getCategory(Integer.parseInt(article.getCategory()),category));
            date.setText(article.getDate());
            title.setText(article.getTitle());
            author.setText(ArticleFragment.getAuthor(Integer.parseInt(article.getAuthor()),author));
            editor.setText(ArticleFragment.getAuthor(Integer.parseInt(article.getEditor()),editor));
            if(article.getContent()!=null && !article.getContent().equals("")){
                content.setHtml(article.getContent());
            }
            else{

                content.setHtml(ArticleFragment.getContent(Integer.parseInt(article.getPosition()),content));
            }

            fab.setOnClickListener(v -> shareTextUrl(article.getTitle()+"\n #Moraspirit_News",
                    article.getTitle()+"- Moraspirit News\n\n"+URL_Base+article.getSlug()));
        }
        imageView =  findViewById(R.id.articleImage);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);



        if(drawable!= null){
            imageView.setImageDrawable(drawable);
        }

        if (imageView.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.default_image).getConstantState() && article!=null) {
            new Handler().postDelayed(() -> {
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(article.getImage(),imageView,options);
            }, 1000);
        }
    }


    public static void setImage(Drawable drawable) {
        ArticleViewFragment.drawable = drawable;
    }
    public static void setArticle(Article article){ArticleViewFragment.article = article;}

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
    private void shareTextUrl(String title,String url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link to the news via"));
    }

    @Override
    protected void onStop() {
        drawable = AppCompatResources.getDrawable(getBaseContext(),R.drawable.default_image);
        super.onStop();
    }
}
