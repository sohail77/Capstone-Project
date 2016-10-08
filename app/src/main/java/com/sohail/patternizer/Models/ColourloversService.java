package com.sohail.patternizer.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SOHAIL on 05/10/16.
 */
public interface ColourloversService {

    @GET("patterns/top?format=json")
    Call<List<PatternsModel>> getTopRatedPatterns(@Query("resultOffset") int resultOffset, @Query("numResults") int numResults);

}
