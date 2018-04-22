package com.artyomd.guardian.data;

import android.support.annotation.NonNull;

import com.artyomd.guardian.api.model.Item;

import java.util.List;

public interface StorageInterface {
	void addItem(@NonNull Item item);

	void deleteItem(@NonNull Item item);

	@NonNull
	List<Item> getAll();

	boolean itemExists(String itemId);
}
