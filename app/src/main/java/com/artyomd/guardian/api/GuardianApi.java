package com.artyomd.guardian.api;

import android.content.Context;

import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.api.model.ItemResponse;
import com.artyomd.guardian.api.model.ResponseWrapper;
import com.artyomd.guardian.api.model.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuardianApi {
	private static final String API_KEY = "ad3eac0d-2e19-4b55-a410-4f216391fb96";
	private static final String API_URL = "https://content.guardianapis.com";
	private static GuardianApi instance = null;

	public static synchronized GuardianApi getInstance(Context context) {
		if (instance == null) {
			instance = new GuardianApi(context);
		}
		return instance;
	}

	private GuardianService guardianService;

	private GuardianApi(Context context) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addConverterFactory(GsonConverterFactory.create(getGson()))
				.client(getOkhttpClient(context))
				.build();
		guardianService = retrofit.create(GuardianService.class);
	}

	private OkHttpClient getOkhttpClient(Context context) {
		File cacheFolder = new File(context.getCacheDir(), "api");
		return OkhttpFactory.getOkhttpClient(context, cacheFolder);
	}

	private Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		return builder.create();
	}

	public Call<ResponseWrapper<SearchResponse>> search(int pageId) {
		return guardianService.search(API_KEY, Collections.singletonList("all"), pageId, 50, "json");
	}

	public Call<ResponseWrapper<ItemResponse>> getItem(String itemId) {
		return guardianService.getItem(itemId, API_KEY, Collections.singletonList("all"), "json");
	}
}
