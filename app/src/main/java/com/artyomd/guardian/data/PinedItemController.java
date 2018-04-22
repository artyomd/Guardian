package com.artyomd.guardian.data;

import android.content.Context;

import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.data.Storage;

import java.util.ArrayList;
import java.util.List;

public class PinedItemController {
	private List<Item> items = new ArrayList<>();

	public boolean updatePinedItems(Context context) {
		int prevSize = items.size();
		items = Storage.getInstance(context).getStorageImpl().getAll();
		return items.size() != prevSize;
	}

	public int getCount(Context context) {
		if (items == null || items.isEmpty()) {
			updatePinedItems(context);
		}
		return items.size();
	}

	public List<Item> getItems() {
		return items;
	}
}
