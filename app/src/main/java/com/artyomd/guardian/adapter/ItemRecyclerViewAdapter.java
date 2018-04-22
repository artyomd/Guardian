package com.artyomd.guardian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.artyomd.guardian.activity.MainActivity;
import com.artyomd.guardian.R;
import com.artyomd.guardian.adapter.holder.ItemViewHolder;
import com.artyomd.guardian.adapter.holder.PinnedItemsViewHolder;
import com.artyomd.guardian.adapter.holder.ProgressViewHolder;
import com.artyomd.guardian.data.PinedItemController;
import com.artyomd.guardian.api.model.Item;

import java.util.ArrayList;
import java.util.List;


public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<Item> items = new ArrayList<>();
	private Context context;
	private final MainActivity.OnItemClickListener listener;
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;
	private final int VIEW_PINNED = 2;
	private PinedItemController controller;
	private PinnedItemsViewHolder pinnedItemsViewHolder;

	public ItemRecyclerViewAdapter(Context context, PinedItemController controller, MainActivity.OnItemClickListener listener) {
		this.listener = listener;
		this.controller = controller;
		this.context = context.getApplicationContext();
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder vh;
		if (viewType == VIEW_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
			vh = new ItemViewHolder(view);
		} else if (viewType == VIEW_PINNED) {
			RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pinned_list, parent, false);
			pinnedItemsViewHolder = new PinnedItemsViewHolder(recyclerView, controller, listener);
			vh = pinnedItemsViewHolder;
		} else {
			ProgressBar v = (ProgressBar) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
			vh = new ProgressViewHolder(v);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			((ItemViewHolder) holder).setItem(context, items.get(position));
			((ItemViewHolder) holder).setListener(listener);
			((ItemViewHolder) holder).setTransitionName("transitionList" + position);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (items.get(position) != null) {
			return VIEW_ITEM;
		} else if (position == 0 && items.get(position) == null && items.size() > 1) {
			return VIEW_PINNED;
		} else {
			return VIEW_PROG;
		}
	}

	public void setItems(List<Item> items) {
		if (items == null) {
			return;
		}
		this.items = items;
		refreshPinnedItems();
		notifyDataSetChanged();
	}

	public void addLoadingItem() {
		this.items.add(null);
		notifyItemInserted(items.size() - 1);
	}

	public void removeLoadingItem() {
		this.items.remove(items.size() - 1);
		notifyItemRemoved(items.size());
	}

	public void addItems(List<Item> items) {
		if (items == null) {
			return;
		}
		if (this.items.isEmpty()) {
			refreshPinnedItems();
		}
		this.items.addAll(items);
		notifyDataSetChanged();
	}

	public void updateFirstItems(List<Item> items) {
		if (items == null) {
			return;
		}
		if (this.items.isEmpty() || (this.items.get(0) == null && this.items.size() == 1)) {
			this.items = new ArrayList<>(items);
		}
		List<Item> itemsToAdd = new ArrayList<>();
		String firstItemId;
		if (this.items.get(0) == null) {
			firstItemId = this.items.get(1).getId();
		} else {
			firstItemId = this.items.get(0).getId();
		}
		boolean isFisrtItems = false;
		for (Item item : items) {
			if (item.getId().equals(firstItemId)) {
				isFisrtItems = true;
				break;
			}
			itemsToAdd.add(item);
		}
		if (isFisrtItems) {
			List<Item> finalData = new ArrayList<>(itemsToAdd);
			if (this.items.size() > 1 && this.items.get(0) == null) {
				this.items.remove(0);
			}
			finalData.addAll(this.items);
			this.items = finalData;
		} else {
			this.items = new ArrayList<>(items);
		}
		refreshPinnedItems();
		notifyDataSetChanged();
	}

	public void refreshPinnedItems() {
		if (controller.getCount(context) != 0) {
			if (!(items.size() > 1 && items.get(0) == null)) {
				this.items.add(0, null);
			}
		} else if (items.size() > 1 && items.get(0) == null) {
			items.remove(0);
		}
	}

	@Override
	public int getItemCount() {
		if (items == null) {
			return 0;
		}
		return items.size();
	}

	public void updatePinnedData() {
		if (pinnedItemsViewHolder == null) {
			return;
		}
		pinnedItemsViewHolder.updateData();
		notifyDataSetChanged();
		refreshPinnedItems();
	}
}
