package com.moraspirit.moraspiritapp.points;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.R;

import java.util.ArrayList;

public abstract class PointsFragment extends Fragment {
    private View view;

    private ConstraintLayout offline,overlay;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public void setView(View view){
        this.view = view;

        setAll();
    }
    public void setAll(){
        setOffline((ConstraintLayout) view.findViewById(R.id.offline));
        setOverlay((ConstraintLayout) view.findViewById(R.id.overlay));
        setRecyclerView((RecyclerView)view.findViewById(R.id.recyclerView));

        recyclerView.setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

    }

    public ConstraintLayout getOffline() {
        return offline;
    }

    public void setOffline(ConstraintLayout offline) {
        this.offline = offline;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public abstract RecyclerView.Adapter setAdapter(ArrayList arrayList, Context context);

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public ConstraintLayout getOverlay() {
        return overlay;
    }

    public void setOverlay(ConstraintLayout overlay) {
        this.overlay = overlay;
    }

    public void updateRecyclerView(ArrayList arrayList) {
        if(arrayList==null){
            offline.setVisibility(View.VISIBLE);
            adapter = null;
        }else {
            offline.setVisibility(View.GONE);
            adapter = setAdapter(arrayList,getContext());
        }
        recyclerView.setAdapter(adapter);

    }

}
