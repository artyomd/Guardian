package com.artyomd.guardian.data;

import android.content.Context;

public class Storage {
	private static Storage instance;

	public static synchronized Storage getInstance(Context context) {
		if (instance == null) {
			instance = new Storage(context);
		}
		return instance;
	}

	private StorageInterface storageImpl;

	public Storage(Context context) {
		storageImpl = new SavedItemsDB(context);
	}

	public StorageInterface getStorageImpl() {
		return storageImpl;
	}
}
