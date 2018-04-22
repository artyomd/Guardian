package com.artyomd.guardian.api;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class OkhttpFactory {
	public static final int CACHE_SIZE = 10 * 1024 * 1024; //10MB;

	private OkhttpFactory() {
	}

	public static OkHttpClient getOkhttpClient(Context context, File cacheDir) {
		Cache cache = new Cache(cacheDir, CACHE_SIZE);
		return new OkHttpClient.Builder()
				.cache(cache)
				.addInterceptor(new CachingInterceptor(context))
				.build();
	}
}
