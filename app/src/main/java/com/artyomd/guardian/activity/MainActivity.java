package com.artyomd.guardian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.artyomd.guardian.R;
import com.artyomd.guardian.adapter.ItemRecyclerViewAdapter;
import com.artyomd.guardian.api.GuardianApi;
import com.artyomd.guardian.api.model.Item;
import com.artyomd.guardian.api.model.ResponseWrapper;
import com.artyomd.guardian.api.model.SearchResponse;
import com.artyomd.guardian.data.PinedItemController;
import com.artyomd.guardian.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.artyomd.guardian.GuardianService.FIRST_ITEM_ID;
import static com.artyomd.guardian.GuardianService.SHARED_PREF_NAME;

public class MainActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ItemRecyclerViewAdapter adapter;
	private GridLayoutManager layoutManager;
	private boolean loading;
	private boolean fetchingFistItems = false;
	private PinedItemController controller = new PinedItemController();
	private static final String KEY_LAYOUT_STATE = "layout_state";

	private List<Call> calls = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = findViewById(R.id.list);
		swipeRefreshLayout = findViewById(R.id.swiperefresh);
		layoutManager = new GridLayoutManager(getApplicationContext(), 2);
		if (savedInstanceState != null) {
			layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(KEY_LAYOUT_STATE));
		}
		updateLayout();
		recyclerView.addItemDecoration(new SpacesItemDecoration(16));
		adapter = new ItemRecyclerViewAdapter(getApplicationContext(), controller, (item, sharedElement, transitionName) -> {
			Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
			intent.putExtra(ItemDetailActivity.TRANSITION_NAME, transitionName);
			intent.putExtra(ItemDetailActivity.ITEM, item);

			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, sharedElement, transitionName);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				startActivity(intent, options.toBundle());
			} else {
				startActivity(intent);
			}
		});
		recyclerView.setAdapter(adapter);
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()

		{
			private int visibleItemCount, totalItemCount, pastVisibleItems;

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (dy > 0) {
					visibleItemCount = layoutManager.getChildCount();
					totalItemCount = layoutManager.getItemCount();
					pastVisibleItems = layoutManager.findFirstVisibleItemPosition();


					if (!loading) {
						if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
							fetchNextPage();
						}
					}
				}
			}
		});
		swipeRefreshLayout.setOnRefreshListener(this::fetchFirstPage);
		if (adapter.getItemCount() == 0) {
			fetchNextPage();
		}
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!fetchingFistItems) {
					fetchFirstPage();
				}
				handler.postDelayed(this, 30000);
			}
		}, 30000);
	}

	private void fetchNextPage() {
		if (loading) {
			return;
		}
		loading = true;
		adapter.addLoadingItem();
		int lastFetchedPage = (adapter.getItemCount() / 50);
		final Call<ResponseWrapper<SearchResponse>> call = GuardianApi.getInstance(getApplicationContext()).search(lastFetchedPage + 1);
		calls.add(call);
		call.enqueue(new Callback<ResponseWrapper<SearchResponse>>() {
			@Override
			public void onResponse(@NonNull Call<ResponseWrapper<SearchResponse>> call, @NonNull Response<ResponseWrapper<SearchResponse>> response) {
				if(lastFetchedPage ==0){
					updateFistItemId(response.body().getResponse().getResults().get(0).getId());
				}
				adapter.removeLoadingItem();
				adapter.addItems(response.body().getResponse().getResults());
				loading = false;
				calls.remove(call);
			}

			@Override
			public void onFailure(@NonNull Call<ResponseWrapper<SearchResponse>> call, @NonNull Throwable t) {
				loading = false;
				calls.remove(call);
			}
		});
	}

	public void updateFistItemId(String id){
		getApplication().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(FIRST_ITEM_ID, id);
	}

	private void updateLayout() {
		layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if (position == 0 && controller.getCount(getApplicationContext()) != 0) {
					return 2;
				} else {
					return 1;
				}
			}
		});
		recyclerView.setLayoutManager(layoutManager);
	}

	private void fetchFirstPage() {
		if (fetchingFistItems) {
			return;
		}
		fetchingFistItems = true;
		Call<ResponseWrapper<SearchResponse>> call = GuardianApi.getInstance(getApplicationContext()).search(1);
		calls.add(call);
		call.enqueue(new Callback<ResponseWrapper<SearchResponse>>() {
			@Override
			public void onResponse(@NonNull Call<ResponseWrapper<SearchResponse>> call, @NonNull Response<ResponseWrapper<SearchResponse>> response) {
				updateFistItemId(response.body().getResponse().getResults().get(0).getId());
				adapter.updateFirstItems(response.body().getResponse().getResults());
				swipeRefreshLayout.setRefreshing(false);
				fetchingFistItems = false;
				calls.remove(call);
			}

			@Override
			public void onFailure(@NonNull Call<ResponseWrapper<SearchResponse>> call, @NonNull Throwable t) {
				swipeRefreshLayout.setRefreshing(false);
				fetchingFistItems = false;
				calls.remove(call);
			}
		});
	}

	public interface OnItemClickListener {
		void onListFragmentInteraction(Item item, View sharedElement, String transitionName);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.updatePinnedData();
		updateLayout();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (Call call : calls) {
			call.cancel();
		}
	}
}
