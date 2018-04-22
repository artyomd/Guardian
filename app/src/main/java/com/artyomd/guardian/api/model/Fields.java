package com.artyomd.guardian.api.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Fields implements Parcelable {
	@SerializedName("thumbnail")
	private String imageUrl;
	@SerializedName("bodyText")
	private String text;

	public String getText() {
		return text;
	}

	public String getImageUrl() {
		return imageUrl;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.imageUrl);
		dest.writeString(this.text);
	}

	protected Fields(Parcel in) {
		this.imageUrl = in.readString();
		this.text = in.readString();
	}

	public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
		@Override
		public Fields createFromParcel(Parcel source) {
			return new Fields(source);
		}

		@Override
		public Fields[] newArray(int size) {
			return new Fields[size];
		}
	};
}