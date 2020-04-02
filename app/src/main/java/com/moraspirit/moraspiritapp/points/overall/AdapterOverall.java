package com.moraspirit.moraspiritapp.points.overall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AdapterOverall extends RecyclerView.Adapter<AdapterOverall.MyViewHolder> {
    private ArrayList<HolderOverall> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView rank, points, university;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            rank = (TextView) view.findViewById(R.id.rank);
            university = (TextView) view.findViewById(R.id.university);
            points = (TextView) view.findViewById(R.id.points);
            logo = (ImageView) view.findViewById(R.id.logo);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterOverall(ArrayList<HolderOverall> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterOverall.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.overall_university_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HolderOverall university = mDataset.get(position);
        Float points =  university.getPoints();
        holder.rank.setText(Integer.toString(university.getRank()));
        holder.points.setText(points==Math.round(points)? String.valueOf(Math.round(points)):Float.toString(points));
        holder.university.setText(university.getUniversity().getName());
        int logo = FragmentRanking.getLogo(university.getUniversity().getId());
        if (logo==0){
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(university.getUniversity().getImage(), holder.logo,options);
        }else {
            holder.logo.setImageResource(logo);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}