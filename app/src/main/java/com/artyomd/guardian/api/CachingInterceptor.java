package com.artyomd.guardian.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.StreamResetException;

public class CachingInterceptor implements Interceptor {

	private Context context;

	public CachingInterceptor(Context context) {
		this.context = context.getApplicationContext();
	}

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request request = chain.request();
		CacheControl cacheControl = request.cacheControl();
		if (!isConnected(context) && "GET".equals(request.method()) && (!cacheControl.noStore() || !cacheControl.onlyIfCached())) {
			request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
		}
		try {
			return chain.proceed(request);
		} catch (Exception e) {
			cacheControl = request.cacheControl();
			if (e instanceof StreamResetException || (!"GET".equals(request.method()) || cacheControl.noStore() || cacheControl.onlyIfCached())) {
				throw e;
			}
		}
		Request newRequest = request.newBuilder()
				.cacheControl(CacheControl.FORCE_CACHE)
				.build();
		return chain.proceed(newRequest);
	}

	public static boolean isConnected(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo nInfo = cm.getActiveNetworkInfo();
				return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
			}
		}
		return false;
	}
}
