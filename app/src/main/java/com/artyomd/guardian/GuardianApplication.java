package com.artyomd.guardian;

import android.app.Application;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class GuardianApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

		Job myJob = dispatcher.newJobBuilder()
				.setService(GuardianService.class)
				.setTag("GuardianJob")
				.setRecurring(true)
				.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
				.setTrigger(Trigger.executionWindow(25*60,  30*60))
				.setReplaceCurrent(false)
				.setConstraints(Constraint.ON_ANY_NETWORK)
				.build();

		dispatcher.mustSchedule(myJob);

	}


}
