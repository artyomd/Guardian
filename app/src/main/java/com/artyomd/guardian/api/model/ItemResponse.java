package com.artyomd.guardian.api.model;

import com.google.gson.annotations.SerializedName;

public class ItemResponse {
	@SerializedName("status")
	private String status;
	@SerializedName("total")
	private Integer total;
	@SerializedName("isHosted")
	private Boolean hosted;
	@SerializedName("pillarId")
	private String pillarId;
	@SerializedName("pillarName")
	private String pillarName;
	@SerializedName("content")
	private Item item;

	public String getStatus() {
		return status;
	}

	public Integer getTotal() {
		return total;
	}

	public Boolean getHosted() {
		return hosted;
	}

	public String getPillarId() {
		return pillarId;
	}

	public String getPillarName() {
		return pillarName;
	}

	public Item getItem() {
		return item;
	}
}
