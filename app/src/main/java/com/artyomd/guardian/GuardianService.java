package com.artyomd.guardian;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.artyomd.guardian.activity.MainActivity;
import com.artyomd.guardian.api.GuardianApi;
import com.artyomd.guardian.api.model.ResponseWrapper;
import com.artyomd.guardian.api.model.SearchResponse;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuardianService extends com.firebase.jobdispatcher.JobService {
	public static final String SHARED_PREF_NAME = "shared_pref";
	public static final String FIRST_ITEM_ID = "first_item_id";

	@Override
	public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
		GuardianApi.getInstance(getApplicationContext()).search(1).enqueue(new Callback<ResponseWrapper<SearchResponse>>() {
			@Override
			public void onResponse(Call<ResponseWrapper<SearchResponse>> call, Response<ResponseWrapper<SearchResponse>> response) {

				if (response.body() == null) {
					jobFinished(job, true);
					return;
				}
				String firstId = response.body().getResponse().getResults().get(0).getId();
				String firstItem = getApplication().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(FIRST_ITEM_ID, null);
				if (!firstId.equals(firstItem) && !isForground()) {
					createNotification();
					getApplication().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(FIRST_ITEM_ID, firstId);
				}
				jobFinished(job, false);
			}

			@Override
			public void onFailure(Call<ResponseWrapper<SearchResponse>> call, Throwable t) {
				jobFinished(job, true);
			}
		});
		return true;
	}

	@Override
	public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
		return true;
	}

	public boolean isForground() {
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		if (am == null) {
			return false;
		}
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		return getApplicationContext().getPackageName().equals(componentInfo.getPackageName());
	}

	public void createNotification() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_DEFAULT);
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.createNotificationChannel(channel);

		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "channel")
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle("Guradian")
				.setContentText("New content available")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

		notificationManager.notify((new Random()).nextInt(), mBuilder.build());
	}
}
