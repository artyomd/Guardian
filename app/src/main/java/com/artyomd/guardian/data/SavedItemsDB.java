package com.artyomd.guardian.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.artyomd.guardian.api.model.Item;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SavedItemsDB extends SQLiteOpenHelper implements StorageInterface {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ItemsDB";

	private static final String TABLE_ITEMS = "items";
	private static final String KEY_ID = "id";
	private static final String KEY_ITEM_ID = "item_id";
	private static final String KEY_ITEM_DATA = "item_data";

	private SQLiteDatabase db;
	private Gson gson;

	public SavedItemsDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.gson = new Gson();
		this.db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_ID + " TEXT," + KEY_ITEM_DATA + " TEXT" + ")";
		db.execSQL(CREATE_EVENTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}

	@Override
	public void addItem(@NonNull Item item) {
		ContentValues values = new ContentValues();
		values.put(KEY_ITEM_ID, item.getId());
		values.put(KEY_ITEM_DATA, gson.toJson(item));
		db.insert(TABLE_ITEMS, null, values);
	}

	public void deleteItem(@NonNull String itemId) {
		db.delete(TABLE_ITEMS, KEY_ITEM_ID + " = \"" + itemId+"\"", null);
	}

	@Override
	public void deleteItem(@NonNull Item item) {
		deleteItem(item.getId());
	}

	@Override
	@NonNull
	public List<Item> getAll() {
		Cursor cursor = db.rawQuery("select * from " + TABLE_ITEMS, null);
		List<Item> items = new ArrayList<>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				items.add(gson.fromJson(cursor.getString(2), Item.class));
			}
			cursor.close();
		}
		return items;
	}

	@Override
	public boolean itemExists(String itemId) {
		Cursor cursor = db.rawQuery("select * from " + TABLE_ITEMS + " where " + KEY_ITEM_ID + " = \"" + itemId+"\"", null);
		boolean status = cursor.getCount() > 0;
		cursor.close();
		return status;
	}
}
