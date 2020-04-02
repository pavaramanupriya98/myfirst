package com.moraspirit.moraspiritapp.year;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.moraspirit.moraspiritapp.ItemClickListener;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.R;

import java.util.ArrayList;

public class AdapterYear extends RecyclerView.Adapter<AdapterYear.MyViewHolder> {
    private ArrayList<Year> years;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView yearTextView;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            yearTextView = view.findViewById(R.id.yearTextView);
            view.setOnClickListener(this);
        }
        public void setItemClickListner(ItemClickListener itemClickListner){
            this.itemClickListener = itemClickListner;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterYear(ArrayList<Year> myDataset, Context context) {
        years = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterYear.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.year_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Year year = years.get(position);
        String yearText = year.getYear()+(year.getSlug().equals("1")?" - SLUG":"");
        holder.yearTextView.setText(yearText);
        if(year.getActive().equals("1")) holder.yearTextView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryDark, null));
        holder.setItemClickListner((view, position1) -> {
            FragmentRanking.setCurrentYear(Integer.parseInt(year.getYear()));
            MainActivity.setYearFromFragment(year.getYear());
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return years.size();
    }

}