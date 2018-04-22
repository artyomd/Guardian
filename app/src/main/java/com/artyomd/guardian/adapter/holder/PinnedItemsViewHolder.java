package com.artyomd.guardian.adapter.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.artyomd.guardian.activity.MainActivity;
import com.artyomd.guardian.adapter.PinnedItemAdapter;
import com.artyomd.guardian.data.PinedItemController;
import com.artyomd.guardian.utils.SpacesItemDecoration;

public class PinnedItemsViewHolder extends RecyclerView.ViewHolder {
	private PinnedItemAdapter adapter;

	public PinnedItemsViewHolder(RecyclerView recyclerView, PinedItemController controller, MainActivity.OnItemClickListener listener) {
		super(recyclerView);
		Context context = recyclerView.getContext();
		this.adapter = new PinnedItemAdapter(context, controller, listener);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.addItemDecoration(new SpacesItemDecoration(16));
		recyclerView.setAdapter(adapter);
	}

	public void updateData() {
		adapter.updateItems();
	}
}