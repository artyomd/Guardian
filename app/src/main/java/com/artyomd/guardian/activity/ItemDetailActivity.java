package com.artyomd.guardian.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.artyomd.guardian.R;
import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.data.Storage;
import com.artyomd.guardian.data.StorageInterface;
import com.artyomd.guardian.utils.RoundCornersTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends Activity {
	public static final String ITEM = "item";
	public static final String TRANSITION_NAME = "transition_name";
	private TextView titleView;
	private TextView categoryView;
	private ImageView imageView;
	private TextView body;
	private Item item;
	private ImageButton pin;
	private boolean pinned;
	private StorageInterface storage;
	private String transitionName;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		item = getIntent().getParcelableExtra(ITEM);
		transitionName = getIntent().getStringExtra(TRANSITION_NAME);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			postponeEnterTransition();
		}
		titleView = findViewById(R.id.title);
		categoryView = findViewById(R.id.category);
		imageView = findViewById(R.id.image);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			imageView.setTransitionName(transitionName);
		}
		body = findViewById(R.id.body);
		pin = findViewById(R.id.pin);
		storage = Storage.getInstance(getApplicationContext()).getStorageImpl();
		pinned = storage.itemExists(item.getId());
		pin.setBackgroundResource(pinned ? R.drawable.pinned : R.drawable.pin);
		pin.setOnClickListener(v -> {
			if (!pinned) {
				storage.addItem(item);
				pin.setBackgroundResource(R.drawable.pinned);
			} else {
				storage.deleteItem(item);
				pin.setBackgroundResource(R.drawable.pin);
			}
			pinned = !pinned;
		});
		initView(item);
	}

	private void initView(Item item) {
		titleView.setText(item.getTitle());
		categoryView.setText(item.getSectionName());
		Picasso.with(getApplicationContext())
				.load(item.getFields().getImageUrl())
				.transform(new RoundCornersTransform(16))
				.placeholder(R.drawable.place_holder)
				.fit()
				.into(imageView, new Callback() {
					@Override
					public void onSuccess() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							startPostponedEnterTransition();
						}
					}

					@Override
					public void onError() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							startPostponedEnterTransition();
						}
					}
				});
		body.setText(item.getFields().getText());
	}
}
