package com.moraspirit.moraspiritapp.menuitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.R;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MenuItem> menuItems;
    private Context context;

    public AdapterMenu(List<MenuItem> menuItems, Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_holder,parent,false),menuItems);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MenuViewHolder){
            ((MenuViewHolder)holder).onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    private static class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private List<MenuItem> menuItems;
        private ConstraintLayout menuItemBg;

        public MenuViewHolder(@NonNull View itemView, List<MenuItem> menuItems) {
            super(itemView);
            this.menuItems = menuItems;
            title = itemView.findViewById(R.id.menu_title);
            menuItemBg = itemView.findViewById(R.id.menu_item_bg);
        }

        public void onBind(int position){
            title.setText(menuItems.get(position).getTitle());
            title.setTextColor(menuItems.get(position).getTextColor());
            menuItemBg.setBackgroundResource(menuItems.get(position).getBgRes());
        }
    }
}
