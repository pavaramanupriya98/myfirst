package com.moraspirit.moraspiritapp.points.sports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.points.HolderMenWomen;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AdapterMenWomenSports extends RecyclerView.Adapter<AdapterMenWomenSports.MyViewHolder> {
    private ArrayList<HolderMenWomen> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView university, points;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            university = (TextView) view.findViewById(R.id.university);
            points = (TextView) view.findViewById(R.id.points);
            logo = (ImageView) view.findViewById(R.id.logo);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterMenWomenSports(ArrayList<HolderMenWomen> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterMenWomenSports.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sports_university_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HolderMenWomen holderMenWomen = mDataset.get(position);
        Float points =  holderMenWomen.getPoints();
        holder.university.setText(holderMenWomen.getUniversity().getName());
        holder.points.setText(points==Math.round(points)? String.valueOf(Math.round(points)):Float.toString(points));
        int logo = FragmentRanking.getLogo(holderMenWomen.getUniversity().getId());
        if (logo==0){
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(holderMenWomen.getUniversity().getImage(), holder.logo,options);
            Toast.makeText(context,"run",Toast.LENGTH_LONG).show();
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