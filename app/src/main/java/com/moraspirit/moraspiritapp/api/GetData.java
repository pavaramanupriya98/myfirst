package com.moraspirit.moraspiritapp.api;

import com.moraspirit.moraspiritapp.sports.Match;
import com.moraspirit.moraspiritapp.sports.PastMatch;
import com.moraspirit.moraspiritapp.year.Year;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetData {

    @GET("years")
    Call<ApiData<Year>> getYears();

    @GET("schedule/page/{page}")
    Call<ApiData<Match>> getMatches(@Path("page") int page);

    @GET("latestmatches/page/{page}")
    Call<ApiData<PastMatch>> getMatchesPast(@Path("page") int page);


}