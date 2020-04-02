package com.moraspirit.moraspiritapp.points.universities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.Constants;
import com.moraspirit.moraspiritapp.points.HolderMenWomen;
import com.moraspirit.moraspiritapp.R;

import java.util.ArrayList;

public class AdapterMenWomenUniversity extends RecyclerView.Adapter<AdapterMenWomenUniversity.MyViewHolder> {
    private ArrayList<HolderMenWomen> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView sport, points;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            sport = (TextView) view.findViewById(R.id.sport);
            points = (TextView) view.findViewById(R.id.points);
            logo = view.findViewById(R.id.logo);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterMenWomenUniversity(ArrayList<HolderMenWomen> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterMenWomenUniversity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_holder, parent, false);
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
        holder.sport.setText(holderMenWomen.getSport().getName());
        holder.points.setText(points==Math.round(points)? String.valueOf(Math.round(points)):Float.toString(points));
        holder.logo.setImageResource(Constants.getSportLogo(holderMenWomen.getSport().getId()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}