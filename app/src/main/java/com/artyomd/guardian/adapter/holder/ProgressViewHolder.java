package com.artyomd.guardian.adapter.holder;


import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

public class ProgressViewHolder extends RecyclerView.ViewHolder

{
	public ProgressBar progressBar;

	public ProgressViewHolder(ProgressBar progressBar) {
		super(progressBar);
		this.progressBar = progressBar;
	}
}