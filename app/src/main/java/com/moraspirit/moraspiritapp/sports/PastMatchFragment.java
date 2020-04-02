package com.moraspirit.moraspiritapp.sports;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moraspirit.moraspiritapp.api.ApiData;
import com.moraspirit.moraspiritapp.database.Database;
import com.moraspirit.moraspiritapp.api.GetData;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.api.RetrofitClient;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastMatchFragment extends Fragment {
    private Context context;

    private RecyclerView recyclerView;
    private AdapterMatchPast adapter;



    private ArrayList<PastMatch> matchesArray;

    private int LAST_PAGE = 1;
    private int CURRENT_PAGE = 1;

    public PastMatchFragment(Context context){
        this.context = context;
    }
    public PastMatchFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.past_match, container, false);
        matchesArray = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        refresh();
        return view;
    }

    public void refresh(){
        LAST_PAGE = 1;
        CURRENT_PAGE = 1;
        matchesArray = new ArrayList<>();
        if(adapter !=null) adapter.clear();
        loadMatchesFromDB();
    }

    public void setAdapter(ArrayList arr){
        adapter = new AdapterMatchPast(arr,context);
        recyclerView.setAdapter(adapter);
    }



    public void loadUpcoming(int page) {
        if (page > LAST_PAGE){
            return;
        }
        CURRENT_PAGE++;
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<ApiData<PastMatch>> call = service.getMatchesPast(page);
        call.enqueue(new Callback<ApiData<PastMatch>>() {

            @Override
            public void onResponse(Call<ApiData<PastMatch>> call, Response<ApiData<PastMatch>> response)  {
                setLAST_PAGE(response);
                ArrayList<PastMatch> additionData = getAdditional(response.body().getData(), adapter.getmDataset());
//                try {
                    ArrayList<PastMatch> filteredArr =additionData;
                    adapter.addAll(filteredArr);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                saveToDB(additionData);
            }

            @Override
            public void onFailure(Call<ApiData<PastMatch>> call, Throwable throwable) {
            }
        });
    }

    public ArrayList<PastMatch> getAdditional(ArrayList<PastMatch> arrOne, ArrayList<PastMatch> arrTwo) {
        ArrayList<PastMatch> arr = new ArrayList<>();
//        arrOne.removeAll(arrTwo);
        for(PastMatch match1 : arrOne){
            boolean same = false;
            for(PastMatch match2 : arrTwo){
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

    public void setLAST_PAGE(Response<ApiData<PastMatch>> response) {
        LAST_PAGE = response.body().getPagination().getPageCount();
    }

    public void setUpdatedState() {

    }

    public void saveToDB(ArrayList<PastMatch> arr) {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Database db = Database.getInstatnce(context);
                db.matchPastDao().insertAll(arr);
                loadUpcoming(CURRENT_PAGE);
            }
        });
    }

    public void loadMatchesFromDB() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Database db = Database.getInstatnce(context);
//                try {
                    matchesArray.addAll(db.matchPastDao().getAll());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                try {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter(matchesArray);
                            CURRENT_PAGE = matchesArray.size()/20+1;
                            LAST_PAGE = CURRENT_PAGE;
                            loadUpcoming(CURRENT_PAGE);
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }



            }
        });

    }

//    public ArrayList<PastMatch> filterArray(List<PastMatch> arr) throws ParseException {
//        ArrayList<PastMatch> filteredArray = new ArrayList<>();
//        for(PastMatch match : arr){
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//            Date matchDate = format.parse(match.getDate());
//            Date today = new Date();
//            assert matchDate != null;
//            if (matchDate.compareTo(today) <= 0) {
//                filteredArray.add(match);}
//        }
//
//        return filteredArray;
//    }

}
