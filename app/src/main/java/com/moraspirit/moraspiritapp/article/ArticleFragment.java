package com.moraspirit.moraspiritapp.article;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moraspirit.moraspiritapp.points.FragmentRanking;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static Gson gson;
    private static final String API_Articles = "http://api.moraspirit.com/api/v1/latestarticles/page/";
    private static final String API_Authors = "http://api.moraspirit.com/api/v1/articles-authors";
    private static final String API_Categories = "http://api.moraspirit.com/api/v1/articles-categories";


    private static ArrayList<Article> articlesArray;
    private static ArrayList<Author> authorsArray;
    private static ArrayList<Category> categoryArray;
    public static ConcurrentHashMap<Integer,TextView> toLoadCategoryArray,toLoadAuthorArray,toLoadContent;

    private RecyclerView articleList;
    private static AdapterArticle adapter;

    private static SwipeRefreshLayout swipe;

    private static Context context;

    public static final int PAGE_START = 1;
    private static int currentPage = 1;
    private static boolean isLastPage = false;
    private static int totalPage = Integer.MAX_VALUE;
    private static boolean isLoading = false;
    static int itemCount = 0;

    private static boolean fromExternal = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        isLastPage = false;

        currentPage = PAGE_START;
        context = getContext();

        toLoadCategoryArray = new ConcurrentHashMap <>();
        toLoadAuthorArray =  new ConcurrentHashMap <>();
        toLoadContent = new ConcurrentHashMap<>();

        articleList = view.findViewById(R.id.articleList);
        LinearLayoutManager layoutManager = (new LinearLayoutManager(getContext()));
        articleList.setLayoutManager(layoutManager);
        articleList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadPage(currentPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        swipe = view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);

        articlesArray = new ArrayList<>();
        setAdapter(articlesArray);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        loadAuthors();
        return view;
    }


    public Gson getGson() {
        return gson;
    }


    public Article getArticleFromDB(int pos) {
        ArrayList<Article> arrayList = adapter.getmDataset();
        return arrayList.get(pos);
    }


    public void setAdapter(ArrayList arrayList) {
        adapter = new AdapterArticle(arrayList, context);
        articleList.setAdapter(adapter);
    }

    public static void loadPage(int page) {
        new RetrieveFeedTaskArticles().execute(API_Articles + page);
    }


    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        loadAuthors();
    }

    private static void loadAuthors() {
        new RetrieveFeedTaskAuthors().execute(API_Authors);
    }

    public static String getAuthor(int id,TextView textView) {
        if (authorsArray == null) {
            if (toLoadAuthorArray == null) {
                toLoadAuthorArray = new ConcurrentHashMap <>();
            }
            toLoadAuthorArray.put(id, textView);
            return "";
        }
        return getAuthor(id);
    }
    public static String getAuthor(int id) {

        for (Author author : authorsArray) {
            if (author.getId() == id) {
                return author.getName();
            }
        }
        return "Moraspirit";
    }

    public static String getCategory(int id, TextView textView) {
        if (categoryArray == null) {
            if (toLoadCategoryArray == null) {
                toLoadCategoryArray = new ConcurrentHashMap <>();
            }
            toLoadCategoryArray.put(id, textView);
            fromExternal = true;
            loadAuthors();
            return "";
        }
        return getCategory(id);

    }

    public static String getCategory(int id) {
        for (Category category : categoryArray) {
            if (category.getId() == id) {
                return category.getCategory();
            }
        }
        return "";
    }
    public static String getContent(int pos, TextView textView){
        if (adapter.getItemCount()<5) {

            if (toLoadContent == null) {
                toLoadContent = new ConcurrentHashMap <>();
            }
            toLoadContent.put(pos, textView);
            return "";
        }
        return getContent(pos);

    }
    public static String getContent(int pos){
        return adapter.getItem(pos).getContent();
    }

    public static boolean isAdapterFill(){
        return adapter.isFill();
    }


    static class RetrieveFeedTaskArticles extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
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
                    e.printStackTrace();
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


            if (JSONString != null && !JSONString.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(JSONString);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject pagination = jsonObject.getJSONObject("pagination");
                        if (jsonArray.length() > 0) {
                            articlesArray = new ArrayList<>();
                            articlesArray.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), Article[].class)));

                            if (articlesArray.size() < 5)
                                totalPage = pagination.getInt("page_number");

                            itemCount += articlesArray.size();
                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addAll(articlesArray);
                            swipe.setRefreshing(false);
                            if (currentPage < totalPage) adapter.addLoading();
                            else isLastPage = true;
                            isLoading = false;
                            setLoadTo();

                            MainActivity.showShowCase();

                        }

                    }

                } catch (JSONException e) {
                    MainActivity.makeSnackBar("Data Error!", false);
//                    updateRecyclerView(new ArrayList());
                }
            } else {
//                updateRecyclerView(new ArrayList());
            }
            swipe.setRefreshing(false);


        }
        public static synchronized void setLoadTo(){
            Iterator<Integer> set = toLoadContent.keySet().iterator();
            System.out.println(toLoadContent.size());
            while (set.hasNext()){
                int pos = set.next();
                ((HtmlTextView) Objects.requireNonNull(toLoadContent.get(pos))).setHtml(getContent(pos));
                toLoadContent.remove(pos);
            }
        }

        public void saveJsonData(String jsonData) {

            FragmentRanking.saveJsonData(context, "article-page-" + currentPage, jsonData);

        }

        public String getJsonData() {

            return FragmentRanking.getJsonData(context, "article-page-" + currentPage);

        }
    }

    static class RetrieveFeedTaskAuthors extends AsyncTask<String, Void, String> {

        boolean success = false;

        @Override
        protected void onPreExecute() {
            if(!isAdapterFill()){

            }
            swipe.setRefreshing(true);
        }


        @Override
        protected String doInBackground(String... urls) {
            if (MainActivity.checkDataAvailability(context)) {
                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    URL url2 = new URL(API_Categories);
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();

                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        saveJsonDataAuthor(stringBuilder.toString()); //Save data for offline


                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(httpURLConnection2.getInputStream()));
                        StringBuilder stringBuilder2 = new StringBuilder();

                        String line2;
                        while ((line2 = bufferedReader2.readLine()) != null) {
                            stringBuilder2.append(line2).append("\n");
                        }
                        bufferedReader2.close();
                        saveJsonDataCategory(stringBuilder2.toString()); //Save data for offline
                        postExecute(stringBuilder2.toString());


                        return stringBuilder.toString();
                    } finally {
                        httpURLConnection.disconnect();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.makeSnackBar("Network Connection Error!", true);
                    postExecute(getJsonDataCategory());
                    return getJsonDataAuthor();
//                MainActivity.makeToast(getContext(),"Network Connection Error!");
                }
            } else {
                MainActivity.makeSnackBar("Connect to the internet", true);
                postExecute(getJsonDataCategory());
                return getJsonDataAuthor();

            }
        }

        protected void postExecute(final String JSONString) {


            if (JSONString != null && !JSONString.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(JSONString);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            categoryArray = new ArrayList<>();
                            categoryArray.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), Category[].class)));
                            success = true;
                        }

                    }

                } catch (JSONException e) {
                    MainActivity.makeSnackBar("Data Error!", false);
                }
            } else {
                MainActivity.makeSnackBar("Offline Data not available", true);
            }
        }

        @Override
        protected void onPostExecute(final String JSONString) {


            if (JSONString != null && !JSONString.equals("") && success) {
                try {
                    JSONObject jsonObject = new JSONObject(JSONString);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            authorsArray = new ArrayList<>();
                            authorsArray.addAll(Arrays.asList(gson.fromJson(jsonArray.toString(), Author[].class)));
                            setLoadTo();
                            if(!fromExternal)loadPage(1);
                            fromExternal = false;
                        }

                    }

                } catch (JSONException e) {
                    MainActivity.makeSnackBar("Data Error!", false);
                }
            } else {
                swipe.setRefreshing(false);
                MainActivity.makeSnackBar("Offline Data not available", true);
            }

        }

        public static synchronized void setLoadTo(){
            Iterator<Integer> set = toLoadCategoryArray.keySet().iterator();
            while (set.hasNext()){
                int id = set.next();
                toLoadCategoryArray.get(id).setText(getCategory(id));
                toLoadCategoryArray.remove(id);
            }
            Iterator<Integer> set2 = toLoadAuthorArray.keySet().iterator();
            while (set2.hasNext()){
                int id = set2.next();
                toLoadAuthorArray.get(id).setText(getAuthor(id));
                toLoadAuthorArray.remove(id);
            }
        }

        public void saveJsonDataAuthor(String jsonData) {

            FragmentRanking.saveJsonData(context, "Authors", jsonData);

        }

        public void saveJsonDataCategory(String jsonData) {

            FragmentRanking.saveJsonData(context, "Category", jsonData);

        }

        public String getJsonDataAuthor() {
            return FragmentRanking.getJsonData(context, "Authors");
        }

        public String getJsonDataCategory() {
            return FragmentRanking.getJsonData(context, "Category");
        }

    }
}

