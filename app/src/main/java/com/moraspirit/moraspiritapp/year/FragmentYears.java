package com.moraspirit.moraspiritapp.year;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.moraspirit.moraspiritapp.api.ApiData;
import com.moraspirit.moraspiritapp.api.GetData;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.api.RetrofitClient;
import com.moraspirit.moraspiritapp.points.FragmentRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentYears extends RoundedBottomSheetDialogFragment {
    private ArrayList<Year> years;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_content, container, false);

        if (years == null) years = new ArrayList<>();
        recyclerView = view.findViewById(R.id.yearsRecylerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        updateYears();
        return view;
    }

    public void updateYears() {
        adapter = new AdapterYear(years, getContext());
        recyclerView.setAdapter(adapter);
    }

    public void loadYears(Context context) {
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<ApiData<Year>> call = service.getYears();
        call.enqueue(new Callback<ApiData<Year>>() {

            @Override
            public void onResponse(Call<ApiData<Year>> call, Response<ApiData<Year>> response) {
                if(response.body()==null) return;
                years = new ArrayList<>();
                years.addAll(response.body().getData());
                Collections.reverse(years);
                setLastYear(years);
                saveData(context, years);

            }

            @Override
            public void onFailure(Call<ApiData<Year>> call, Throwable throwable) {
                years = new ArrayList<>();
                List arr = getData(context);
                if (arr == null || arr.size()==0) years.add(getNewYear());
                else years.addAll(arr);
                setLastYear(years);
            }
        });
    }

    public static void setLastYear(ArrayList<Year> years) {
        Year year = years.get(0);
        if(Integer.parseInt(year.getYear()) != FragmentRanking.getCurrentYear())
            FragmentRanking.setCurrentYear(Integer.parseInt(year.getYear()));
        MainActivity.setYear(year.getYear());
    }

    public static void saveData(Context context, ArrayList<Year> years) {
        Gson gson = new Gson();
        String json = gson.toJson(years);
        FragmentRanking.saveJsonData(context, "years", json);
    }

    public Year getNewYear(){
        Year year  = new Year();
        year.setYear("2019");
        year.setSlug("1");
        year.setActive("1");
        year.setId(0);
        return year;
    }

    public static List<Year> getData(Context context) {
        Gson gson = new Gson();
        String data = FragmentRanking.getJsonData(context, "years");
        if (data !=null)
            return Arrays.asList(gson.fromJson(data, Year[].class));
        else
            return null;
    }

}
