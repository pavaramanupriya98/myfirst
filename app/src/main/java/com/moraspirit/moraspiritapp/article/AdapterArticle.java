package com.moraspirit.moraspiritapp.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.card.MaterialCardView;
import com.moraspirit.moraspiritapp.BaseViewHolder;
import com.moraspirit.moraspiritapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.github.ybq.android.spinkit.sprite.Sprite;
//import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterArticle extends RecyclerView.Adapter<BaseViewHolder> {
    private ArrayList<Article> mDataset;
    private static Context context;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;



    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterArticle(ArrayList<Article> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.article_card, parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false),mDataset);
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;

        }
    }

// Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }




    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mDataset.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public void add(Article response) {
        mDataset.add(response);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void addAll(List<Article> postItems) {
        for (Article response : postItems) {
            add(response);
        }
    }


    public void remove(Article postItems) {
        int position = mDataset.indexOf(postItems);
        if (position > -1) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new Article());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mDataset.size() - 1;
        Article item = getItem(position);
        if (item != null) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isFill(){
        if(mDataset.size()>0) return true;
        else return false;
    }

    public Article getItem(int position) {
        if (mDataset.size()==0) return null;
        return mDataset.get(position);
    }

    public ArrayList<Article> getmDataset() {
        return mDataset;
    }

    public static class MyViewHolder extends BaseViewHolder implements View.OnClickListener {
        private final String URL_Base = "https://www.moraspirit.com/news/";

        private TextView category, date, author, caption;
        private LinearLayout authorContainer;
        private ImageView image;
        private Button open, share;
        private Article article;
        private MaterialCardView card;
        final Drawable[] drawables = new Drawable[1];
        private ArrayList<Article> mDataset;


        private MyViewHolder(View view,ArrayList data) {
            super(view);
            ButterKnife.bind(this, view);
            mDataset = data;
            caption =  view.findViewById(R.id.caption);
            category =  view.findViewById(R.id.category);
            date = view.findViewById(R.id.date);
            author =  view.findViewById(R.id.author);
            author.setSelected(true);
            card = view.findViewById(R.id.materialCardView);

            authorContainer = view.findViewById(R.id.authorContainer);

            image =  view.findViewById(R.id.image);
            open = view.findViewById(R.id.open);
            share = view.findViewById(R.id.share);

        }

        protected void clear() {

        }
        public void onBind(int position){
            article = mDataset.get(position);
            if (article.getCategory()==null) return;
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.default_image));
            caption.setText(article.getTitle());
            category.setText(ArticleFragment.getCategory(Integer.parseInt(article.getCategory())));
            date.setText(article.getDate());
            author.setText(ArticleFragment.getAuthor(Integer.parseInt(article.getAuthor())));


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(article.getImage(),image,options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    drawables[0] = ((ImageView) view).getDrawable();

                }
            });

//            if (MainActivity.checkDataAvailability(context)) {
//                Glide.with(context).
//                        load(article.getImage())
//                        .placeholder(R.drawable.default_image)
//                        .into(new CustomTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                image.setImageDrawable(resource);
//                                drawables[0] = resource;
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });
//            }

            open.setOnClickListener(v -> openArticle());
            card.setOnClickListener(v->openArticle());
            share.setOnClickListener(v->shareTextUrl(article.getTitle()+"\n #Moraspirit_News",article.getTitle()+"- Moraspirit News\n\n"+URL_Base+article.getSlug()));
        }
        private void shareTextUrl(String title,String url) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, title);
            share.putExtra(Intent.EXTRA_TEXT, url);

            context.startActivity(Intent.createChooser(share, "Share link to the news via"));
        }

        private void openArticle(){

            Pair<View, String> p1 = Pair.create(image, "image");
            Pair<View, String> p2 = Pair.create(authorContainer, "authorContainer");
            Pair<View, String> p3 = Pair.create(author, "author");

            ActivityOptionsCompat option = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, p1, p2, p3);
            Intent intent = new Intent(context, ArticleViewFragment.class);
            ArticleViewFragment.setImage(drawables[0]);
            ArticleViewFragment.setArticle(article);
            context.startActivity(intent, option.toBundle());
        }

        @Override
        public void onClick(View view) {
            openArticle();
        }
    }

    public class FooterHolder extends BaseViewHolder {

        @BindView(R.id.progress)
        ProgressBar mProgressBar;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Sprite doubleBounce = new DoubleBounce();
            doubleBounce.setColor(context.getResources().getColor(R.color.colorPrimary));
            mProgressBar.setIndeterminateDrawable(doubleBounce);
        }

        protected void clear() {

        }

        public void onBind(int position) {
        }
    }
}