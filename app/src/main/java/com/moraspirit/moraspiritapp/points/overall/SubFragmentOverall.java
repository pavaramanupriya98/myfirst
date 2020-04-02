package com.moraspirit.moraspiritapp.points.overall;

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
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.points.PointsFragment;
import com.moraspirit.moraspiritapp.R;
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

public class SubFragmentOverall extends PointsFragment {

    private ArrayList<HolderOverall> totalUniversities, menUniversities, womenUniversities;

    private Gson gson;


    private final String API = "http://api.moraspirit.com/api/v1/points/";


    private ToggleSwitch toggleSwitch;

    private Overall current;

    private View view;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.overall_sub_fragment, container, false);

        setView(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        System.out.println(width);
        int innerWidth = (int) (width - 6 * width / 100);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Overall Men");
        labels.add("Total");
        labels.add("Overall Women");

        toggleSwitch = view.findViewById(R.id.toggleSwitch);
        toggleSwitch.setToggleWidth(innerWidth / 3);
        toggleSwitch.setLabels(labels);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        toggleSwitch.active(0);
                        current = Overall.MEN;
                        loadMale();
                        break;
                    case 1:
                        toggleSwitch.active(1);
                        current = Overall.TOTAL;
                        loadTotal();
                        break;
                    case 2:
                        toggleSwitch.active(2);
                        current = Overall.WOMEN;
                        loadFemale();
                        break;
                }
            }
        });
        toggleSwitch.setCheckedTogglePosition(1);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        return view;
    }

    @Override
    public RecyclerView.Adapter setAdapter(ArrayList arrayList, Context context) {
        return new AdapterOverall(arrayList,context);
    }

    public void loadTotal() {
        new RetrieveFeedTask().execute(API + Integer.toString(FragmentRanking.getCurrentYear()));
    }

    public void loadMale() {
        new RetrieveFeedTask().execute(API + Integer.toString(FragmentRanking.getCurrentYear()) + "/male");
    }

    public void loadFemale() {
        new RetrieveFeedTask().execute(API + Integer.toString(FragmentRanking.getCurrentYear()) + "/female");
    }


    public void updateCurrentFragment() {
        switch (current) {
            case MEN:
                loadMale();
                break;
            case TOTAL:
                loadTotal();
                break;
            case WOMEN:
                loadFemale();
                break;
        }
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
                        saveJsonData(stringBuilder.toString()); //Save data for offline
                        return stringBuilder.toString();
                    } finally {
                        httpURLConnection.disconnect();
                    }

                } catch (Exception e) {
                    MainActivity.makeSnackBar("Network Connection Error!", true);
                    return getJsonData();
//                MainActivity.makeToast(getContext(),"Network Connection Error!");
                }
            } else {
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
                                    switch (current) {
                                        case MEN:
                                            menUniversities = new ArrayList<>();
                                            menUniversities.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), HolderOverall[].class)));
                                            updateRecyclerView(menUniversities);
                                            break;
                                        case TOTAL:
                                            totalUniversities = new ArrayList<>();
                                            totalUniversities.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), HolderOverall[].class)));
                                            updateRecyclerView(totalUniversities);
                                            break;
                                        case WOMEN:
                                            womenUniversities = new ArrayList<>();
                                            womenUniversities.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), HolderOverall[].class)));
                                            updateRecyclerView(womenUniversities);
                                            break;
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            MainActivity.makeSnackBar("Data Error!", false);
                            updateRecyclerView(null);
//                            Toast.makeText(getContext(), "Data Error", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        updateRecyclerView(null);
                    }
                    getOverlay().setVisibility(View.GONE);
                }
            }, 500);

        }
        public void saveJsonData(String jsonData){
            switch (current){
                case MEN:
                    FragmentRanking.saveJsonData(context,"Overall-Men"+FragmentRanking.getCurrentYear(),jsonData);
                    break;
                case WOMEN:
                    FragmentRanking.saveJsonData(context,"Overall-Women"+FragmentRanking.getCurrentYear(),jsonData);
                    break;
                case TOTAL:
                    FragmentRanking.saveJsonData(context,"Overall-Total"+FragmentRanking.getCurrentYear(),jsonData);
                    break;

            }
        }
        public String getJsonData(){
            switch (current){
                case MEN:
                    return FragmentRanking.getJsonData(context,"Overall-Men"+FragmentRanking.getCurrentYear());
                case WOMEN:
                    return FragmentRanking.getJsonData(context,"Overall-Women"+FragmentRanking.getCurrentYear());
                case TOTAL:
                    return FragmentRanking.getJsonData(context,"Overall-Total"+FragmentRanking.getCurrentYear());

            }
            return null;
        }

    }

    public enum Overall {
        MEN,
        TOTAL,
        WOMEN
    }

}

