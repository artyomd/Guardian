package com.artyomd.guardian.adapter.holder;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artyomd.guardian.activity.MainActivity;
import com.artyomd.guardian.R;
import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.utils.RoundCornersTransform;
import com.squareup.picasso.Picasso;

public class ItemViewHolder extends RecyclerView.ViewHolder {
	private final TextView titleView;
	private final TextView categoryView;
	private final ImageView imageView;
	private static RoundCornersTransform transform = new RoundCornersTransform(16);
	private MainActivity.OnItemClickListener listener;
	private Item item;
	private String transitionName;

	public ItemViewHolder(View view) {
		super(view);
		titleView = view.findViewById(R.id.title);
		categoryView = view.findViewById(R.id.category);
		imageView = view.findViewById(R.id.image);
		view.setOnClickListener(v -> {
			if (listener != null && item != null) {
				listener.onListFragmentInteraction(item, imageView, transitionName);
			}
		});
	}

	public void setListener(MainActivity.OnItemClickListener listener) {
		this.listener = listener;
	}

	public void setItem(Context context, Item item) {
		this.item = item;
		titleView.setText(item.getTitle());
		categoryView.setText(item.getSectionName());
		Picasso.with(context)
				.load(item.getFields().getImageUrl())
				.transform(transform)
				.placeholder(R.drawable.place_holder)
				.fit()
				.into(imageView);
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			this.imageView.setTransitionName(transitionName);
		}
	}
}
