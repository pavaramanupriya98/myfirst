package com.moraspirit.moraspiritapp.sports;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moraspirit.moraspiritapp.api.ApiData;
import com.moraspirit.moraspiritapp.database.Database;
import com.moraspirit.moraspiritapp.api.GetData;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.api.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpComingMatchFragment extends Fragment {
    private Context context;

    private RecyclerView recyclerView;
    private AdapterMatchUpComing adapter;



    private ArrayList<Match> matchesArray;

    private int LAST_PAGE = 1;
    private int CURRENT_PAGE = 1;

    private Activity activity;

    public UpComingMatchFragment(Context context){
        this.context = context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity =(Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.upcoming_match, container, false);
        matchesArray = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        refresh();
        return view;
    }
    public UpComingMatchFragment(){

    }

    public void refresh(){
        LAST_PAGE = 1;
        CURRENT_PAGE = 1;
        matchesArray = new ArrayList<>();
        if(adapter !=null) adapter.clear();
        loadMatchesFromDB();
    }

    public void setAdapter(ArrayList arr){
        adapter = new AdapterMatchUpComing(arr,context);
        recyclerView.setAdapter(adapter);
    }



    public void loadUpcoming(int page) {
        if (page > LAST_PAGE){
            return;
        }
        CURRENT_PAGE++;
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<ApiData<Match>> call = service.getMatches(page);
        call.enqueue(new Callback<ApiData<Match>>() {

            @Override
            public void onResponse(Call<ApiData<Match>> call, Response<ApiData<Match>> response)  {
                if(response.body()==null) return;
                setLAST_PAGE(response);
                ArrayList<Match> additionData = getAdditional(response.body().getData(), adapter.getmDataset());
                try {
                    ArrayList<Match> filteredArr = filterArray(additionData);
//                    matchesArray.addAll(filteredArr);
                    adapter.addAll(filteredArr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                saveToDB(additionData);
            }

            @Override
            public void onFailure(Call<ApiData<Match>> call, Throwable throwable) {
            }
        });
    }

    public ArrayList<Match> getAdditional(ArrayList<Match> arrOne, ArrayList<Match> arrTwo) {
        ArrayList<Match> arr = new ArrayList<>();
//        arrOne.removeAll(arrTwo);
        for(Match match1 : arrOne){
            boolean same = false;
            for(Match match2 : arrTwo){
                if(match1.getId()==match2.getId()){
                    same = true;
                    break;
                }
            }
            if(!same){
                arr.add(match1);
            }
        }
        return arr;
    }

    public void setLAST_PAGE(Response<ApiData<Match>> response) {
        LAST_PAGE = response.body().getPagination().getPageCount();
    }

    public void setUpdatedState() {

    }

    public void saveToDB(ArrayList<Match> arr) {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Database db = Database.getInstatnce(context);
                db.matchDao().insertAll(arr);
                loadUpcoming(CURRENT_PAGE);
            }
        });
    }

    public void loadMatchesFromDB(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Database db = Database.getInstatnce(context);
                try {
                    matchesArray.addAll(filterArray(db.matchDao().getAll()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    if(activity==null) return;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter(matchesArray);
                            CURRENT_PAGE = matchesArray.size() / 20 + 1;
                            LAST_PAGE = CURRENT_PAGE;
                            loadUpcoming(CURRENT_PAGE);
                        }
                    });
                } catch (NullPointerException e){
                    e.printStackTrace();
                }


            }
        });

    }

    public ArrayList<Match> filterArray(List<Match> arr) throws ParseException {
        ArrayList<Match> filteredArray = new ArrayList<>();
        for(Match match : arr){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date matchDate = format.parse(match.getDate());
            Date todayExact = new Date();
            Date today = format.parse(format.format(todayExact));
            assert matchDate != null;
            if (matchDate.after(today) || matchDate.equals(today)) {
                filteredArray.add(match);}
        }
        return filteredArray;
    }

}
