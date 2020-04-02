package com.moraspirit.moraspiritapp.points.sports;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.points.PointsFragment;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.Sport;
import com.moraspirit.moraspiritapp.points.overall.SubFragmentOverall;
import com.moraspirit.moraspiritapp.points.FragmentRanking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class SubFragmentSports extends PointsFragment {

    private ArrayList<Sport> sports;

    private Gson gson;

    private final String API = "http://api.moraspirit.com/api/v1/sports";

    private SubFragmentOverall.Overall current;
    private Boolean visibility = false;


    private Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.sports_sub_fragment, container, false);

        setView(view);

        visibility = true;

//        recyclerView.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.HORIZONTAL));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        updateCurrentFragment();


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        visibility = false;
    }

    @Override
    public RecyclerView.Adapter setAdapter(ArrayList arrayList, Context context) {
        return new AdaptorSports(arrayList,context);
    }

    private void reload() {
        new SubFragmentSports.RetrieveFeedTask().execute(API);
    }



    public void updateCurrentFragment() {
        if (visibility) reload();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            getOverlay().setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            if (MainActivity.checkDataAvailability(context)) {
                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        saveJsonData(stringBuilder.toString());
                        return stringBuilder.toString();
                    } finally {
                        httpURLConnection.disconnect();
                    }

                } catch (Exception e) {
                    MainActivity.makeSnackBar("Network Connection Error!", true);
                    return getJsondata();
                }
            } else {
                MainActivity.makeSnackBar("Connect to the internet", true);
                return getJsondata();
            }

        }

        @Override
        protected void onPostExecute(final String JSONString) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (JSONString != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(JSONString);
                            if (jsonObject.getInt("status") == 200) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray.length() > 0) {

                                    sports = new ArrayList<>();
                                    sports.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), Sport[].class)));
                                    updateRecyclerView(sports);
                                }
                            }
                        } catch (JSONException e) {
                            MainActivity.makeSnackBar("Data Error!", false);
                        }
                    }else{
                        updateRecyclerView(new ArrayList<>());
                    }
                    getOverlay().setVisibility(View.GONE);
                }
            }, 500);

        }

        public void saveJsonData(String jsonData){
            FragmentRanking.saveJsonData(context,"Sports",jsonData);
        }
        public String getJsondata(){
            return FragmentRanking.getJsonData(context,"Sports");
        }

    }


}

