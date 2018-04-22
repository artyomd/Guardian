package com.artyomd.guardian.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

	@SerializedName("id")
	private String id;
	@SerializedName("sectionId")
	private String sectionId;
	@SerializedName("sectionName")
	private String sectionName;
	@SerializedName("type")
	private String type;
	@SerializedName("webPublicationDate")
	private String publicationDate;
	@SerializedName("webTitle")
	private String title;
	@SerializedName("apiUrl")
	private String url;
	@SerializedName("fields")
	private Fields fields;

	public String getId() {
		return id;
	}

	public String getSectionId() {
		return sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public Fields getFields() {
		return fields;
	}

	public String getType() {
		return type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.sectionId);
		dest.writeString(this.sectionName);
		dest.writeString(this.type);
		dest.writeString(this.publicationDate);
		dest.writeString(this.title);
		dest.writeString(this.url);
		dest.writeParcelable(this.fields, flags);
	}

	protected Item(Parcel in) {
		this.id = in.readString();
		this.sectionId = in.readString();
		this.sectionName = in.readString();
		this.type = in.readString();
		this.publicationDate = in.readString();
		this.title = in.readString();
		this.url = in.readString();
		this.fields = in.readParcelable(Fields.class.getClassLoader());
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel source) {
			return new Item(source);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
