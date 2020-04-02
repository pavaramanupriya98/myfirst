package com.moraspirit.moraspiritapp.points.sports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.Constants;
import com.moraspirit.moraspiritapp.ItemClickListener;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.Sport;
import com.moraspirit.moraspiritapp.points.FragmentRanking;

import java.util.ArrayList;

public class AdaptorSports extends RecyclerView.Adapter<AdaptorSports.MyViewHolder> {
    private ArrayList<Sport> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView sport,points;
        public ImageView logo;
        private ItemClickListener itemClickListener;


        public MyViewHolder(View view) {
            super(view);
            sport = (TextView) view.findViewById(R.id.sport);
            logo = (ImageView) view.findViewById(R.id.logo);
            points = (TextView) view.findViewById(R.id.points);

            points.setVisibility(View.GONE);

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
    public AdaptorSports(ArrayList<Sport> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdaptorSports.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Sport sport = mDataset.get(position);

        holder.sport.setText(sport.getName());
        holder.logo.setImageResource(Constants.getSportLogo(sport.getId()));

        holder.setItemClickListner(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                FragmentRanking.setSport(sport.getName());
                SubFragmentMenWomenSports.setCurrentSport(sport);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}