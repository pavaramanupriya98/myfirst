package com.moraspirit.moraspiritapp.sports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.BaseViewHolder;
import com.moraspirit.moraspiritapp.Constants;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.moraspirit.moraspiritapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

//import com.github.ybq.android.spinkit.sprite.Sprite;
//import com.github.ybq.android.spinkit.style.DoubleBounce;

public class AdapterMatchUpComing extends RecyclerView.Adapter<BaseViewHolder> {
    private ArrayList<Match> mDataset;
    private static Context context;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterMatchUpComing(ArrayList<Match> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {

        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_match_card, parent, false), mDataset);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public void add(Match response) {
        mDataset.add(response);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void addAll(List<Match> postItems) {
        for (Match response : postItems) {
            add(response);
        }
    }


    public void remove(Match postItems) {
        int position = mDataset.indexOf(postItems);
        if (position > -1) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new Match());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mDataset.size() - 1;
        Match item = getItem(position);
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

    public boolean isFill() {
        if (mDataset.size() > 0) return true;
        else return false;
    }

    Match getItem(int position) {
        if (mDataset.size() == 0) return null;
        return mDataset.get(position);
    }

    public ArrayList<Match> getmDataset() {
        return mDataset;
    }

    public static class MyViewHolder extends BaseViewHolder {
        private TextView sport, type, title, uniNameA, uniNameB, time, date, venue;
        private ImageView teamA,teamB;
        private LinearLayout teamAContainer,teamBContainer;
        private ArrayList<Match> mDataset;


        private MyViewHolder(View view, ArrayList data) {
            super(view);
            ButterKnife.bind(this, view);
            mDataset = data;
            sport = view.findViewById(R.id.sport);
            type = view.findViewById(R.id.type);
            title = view.findViewById(R.id.title);
            uniNameA = view.findViewById(R.id.uni_name_a);
            uniNameB = view.findViewById(R.id.uni_name_b);
            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
            venue = view.findViewById(R.id.venue);

            teamAContainer = view.findViewById(R.id.team_a_container);
            teamBContainer = view.findViewById(R.id.team_b_container);

            teamA = view.findViewById(R.id.team_a);
            teamB = view.findViewById(R.id.team_b);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            Match match = mDataset.get(position);
            sport.setText(match.getSport().getName());
            type.setText(match.getType());
            title.setText(match.getTitle());
            uniNameA.setText(
                    Constants.getUniShortName(match.getTeamA().getId())
            );
            uniNameB.setText(Constants.getUniShortName(match.getTeamB().getId()));
            time.setText(match.getTime());
            date.setText(match.getDate());
            venue.setText(match.getVenue());

//            if(UpComingMatchFragment.currentMatchType == UpComingMatchFragment.MatchType.PAST){
//                if(match instanceof PastMatch){
//                    String textA = Constants.getUniShortName(match.getTeamA().getId())+" - "+ match.getTeamA().getMarks();
//                    String textB = Constants.getUniShortName(match.getTeamB().getId()) +" - "+ match.getTeamB().getMarks();
//                    uniNameA.setText(textA);
//                    uniNameB.setText(textB);
//
//                    if(((PastMatch) match).getWon().equals("team_a")){
//                        teamAContainer.setBackgroundResource(R.drawable.winner_bg);
//                    }else if(((PastMatch) match).getWon().equals("team_b")){
//                        teamBContainer.setBackgroundResource(R.drawable.winner_bg);
//                    }
//                }
//            }

            int logoA = FragmentRanking.getLogo(match.getTeamA().getId());
            if (logoA==0){
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(match.getTeamA().getImage(), teamA,options);
            }else {
                teamA.setImageResource(logoA);
            }

            int logoB =FragmentRanking.getLogo(match.getTeamB().getId());
            if (logoB==0){
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(match.getTeamB().getImage(), teamB,options);
            }else {
                teamB.setImageResource(logoB);
            }

        }

    }

}