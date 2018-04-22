package com.artyomd.guardian.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DetailsTransition extends TransitionSet {

	public DetailsTransition() {
		setOrdering(ORDERING_TOGETHER);
		addTransition(new ChangeTransform());
		addTransition(new ChangeBounds());
		addTransition(new ChangeImageTransform());
	}
}