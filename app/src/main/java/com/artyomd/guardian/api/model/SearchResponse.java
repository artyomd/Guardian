package com.artyomd.guardian.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

	@SerializedName("status")
	private String status;

	@SerializedName("total")
	private Integer total;

	@SerializedName("startIndex")
	private Integer startIndex;

	@SerializedName("pageSize")
	private Integer pageSize;

	@SerializedName("currentPage")
	private Integer currentPage;

	@SerializedName("pages")
	private Integer totalPages;

	@SerializedName("orderBy")
	private String orderBy;

	@SerializedName("results")
	private List<Item> results;

	public String getStatus() {
		return status;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public List<Item> getResults() {
		return results;
	}
}
