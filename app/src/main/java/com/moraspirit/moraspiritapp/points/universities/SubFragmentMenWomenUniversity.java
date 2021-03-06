package com.moraspirit.moraspiritapp.points.universities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moraspirit.moraspiritapp.points.HolderMenWomen;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.points.PointsFragment;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.University;
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

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class SubFragmentMenWomenUniversity extends PointsFragment {

    private ArrayList<HolderMenWomen> men,women;

    private Gson gson;

    private final String API = "http://api.moraspirit.com/api/v1/points/";


    private ToggleSwitch toggleSwitch;

    private Gender current;

    private Context context;


    private static University currentUniversity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.men_women_sub_fragment, container, false);

        setView(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;

        toggleSwitch = view.findViewById(R.id.toggleSwitch);
        toggleSwitch.setToggleWidth(width/3);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        toggleSwitch.active(0);
                        current = Gender.MEN;
                        updateCurrentFragment();
                        break;
                    case 1:
                        toggleSwitch.active(1);
                        current = Gender.WOMEN;
                        updateCurrentFragment();
                        break;

                }
            }
        });
        toggleSwitch.setCheckedTogglePosition(0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        return view;
    }

    @Override
    public RecyclerView.Adapter setAdapter(ArrayList arrayList, Context context) {
        return new AdapterMenWomenUniversity(arrayList,context);
    }

    public static University getCurrentUniversity() {
        return SubFragmentMenWomenUniversity.currentUniversity;
    }

    public static void setCurrentUniversity(University currentUniversity) {
        SubFragmentMenWomenUniversity.currentUniversity = currentUniversity;
    }


    public void updateCurrentFragment(){
        if(getCurrentUniversity()==null) return;
            new RetrieveFeedTask().execute(API+String.valueOf(FragmentRanking.getCurrentYear())+"/university/"+getCurrentUniversity().getId());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            getOverlay().setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            if(MainActivity.checkDataAvailability(context)) {
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
                    return getJsonData();
                }
            } else{
                MainActivity.makeSnackBar("Connect to the internet", true);
                return getJsonData();
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
                                    ArrayList<HolderMenWomen> menWomen = new ArrayList<>();
                                    menWomen.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), HolderMenWomen[].class)));
                                    men = new ArrayList<>();
                                    women = new ArrayList<>();
                                    for(HolderMenWomen holderMenWomen : menWomen){
                                        if(holderMenWomen.getType().equals("Male")){
                                            men.add(holderMenWomen);
                                        }else{
                                            women.add(holderMenWomen);
                                        }
                                    }
                                    switch (current){
                                        case MEN:
                                            updateRecyclerView(men);
                                            break;
                                        case WOMEN:
                                            updateRecyclerView(women);
                                            break;
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            MainActivity.makeSnackBar("Data Error!",false);
                            updateRecyclerView(null);
                        }
                    }else{
                        updateRecyclerView(null);
                    }
                    getOverlay().setVisibility(View.GONE);
                }
            },500);

        }

        public void saveJsonData(String jsonData){
            switch (current){
                case MEN:
                    FragmentRanking.saveJsonData(context,currentUniversity.getName()+"-Men-"+FragmentRanking.getCurrentYear(),jsonData);
                    break;
                case WOMEN:
                    FragmentRanking.saveJsonData(context,currentUniversity.getName()+"-Women-"+FragmentRanking.getCurrentYear(),jsonData);
            }
        }
        public String getJsonData(){
            switch (current){
                case MEN:
                    return FragmentRanking.getJsonData(context,currentUniversity.getName()+"-Men-"+FragmentRanking.getCurrentYear());
                case WOMEN:
                    return FragmentRanking.getJsonData(context,currentUniversity.getName()+"-Women-"+FragmentRanking.getCurrentYear());
            }
            return null;
        }

    }
    enum Gender {
        MEN,
        WOMEN
    }

}

