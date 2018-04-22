package com.artyomd.guardian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artyomd.guardian.activity.MainActivity;
import com.artyomd.guardian.R;
import com.artyomd.guardian.adapter.holder.ItemViewHolder;
import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.data.PinedItemController;

import java.util.ArrayList;
import java.util.List;

public class PinnedItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {


	private List<Item> items = new ArrayList<>();
	private Context context;
	private final MainActivity.OnItemClickListener listener;
	private PinedItemController controller;

	public PinnedItemAdapter(Context context, PinedItemController controller, MainActivity.OnItemClickListener listener) {
		this.listener = listener;
		this.controller = controller;
		this.context = context.getApplicationContext();
		updateItems();
	}

	@NonNull
	@Override
	public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
		holder.setItem(context, items.get(position));
		holder.setListener(listener);
		holder.setTransitionName("transitionPinned" + position);

	}

	public void updateItems() {
		controller.updatePinedItems(context);
		items = controller.getItems();
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}
