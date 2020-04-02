package com.moraspirit.moraspiritapp.points.universities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.ItemClickListener;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.University;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AdaptorUniversity extends RecyclerView.Adapter<AdaptorUniversity.MyViewHolder> {
    private ArrayList<University> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView id, university;
        public ImageView logo;
        private ItemClickListener itemClickListener;


        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.idText);
            university = (TextView) view.findViewById(R.id.university);
            logo = (ImageView) view.findViewById(R.id.logo);
            view.setOnClickListener(this);
        }

        public void setItemClickListner(ItemClickListener itemClickListner) {
            this.itemClickListener = itemClickListner;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdaptorUniversity(ArrayList<University> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdaptorUniversity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.university_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final University university = mDataset.get(position);

        holder.id.setText(String.valueOf(university.getId()));
        holder.university.setText(university.getName());
        int logo = FragmentRanking.getLogo(university.getId());
        if (logo==0){
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(university.getImage(), holder.logo,options);
        }else {
            holder.logo.setImageResource(logo);
        }
        holder.setItemClickListner(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                FragmentRanking.setUniversity(university.getName());
                SubFragmentMenWomenUniversity.setCurrentUniversity(university);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}