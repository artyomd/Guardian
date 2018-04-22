package com.artyomd.guardian.api;

import com.artyomd.guardian.api.model.ItemResponse;
import com.artyomd.guardian.api.model.ResponseWrapper;
import com.artyomd.guardian.api.model.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GuardianService {
	@GET("search")
	Call<ResponseWrapper<SearchResponse>> search(@Query("api-key") String apiKey, @Query("show-fields") List<String> fields, @Query("page") int pageId, @Query("page-size") int pageSize, @Query("format") String format);

	@GET("{id}")
	Call<ResponseWrapper<ItemResponse>> getItem(@Path(value ="id", encoded = true) String itemId, @Query("api-key") String apiKey, @Query("show-fields") List<String> fields, @Query("format") String format);

}
