package com.example.dk.ekassirtest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk.ekassirtest.model.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<AsyncResult<List<Order>>>, SwipeRefreshLayout.OnRefreshListener {

	// XXX not private?
	RecyclerView recyclerView;
	SwipeRefreshLayout swipeRefreshLayout;
	OrderListAdapter adapter;

	Callback callback;

	public static OrderListFragment newInstance() {
		return new OrderListFragment();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Callback) {
			callback = (Callback) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OrderListFragment.Callback");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_list, container, false);
		recyclerView = (RecyclerView) v.findViewById(R.id.order_list);
		swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
		swipeRefreshLayout.setOnRefreshListener(this);

		// XXX emptyList()? No-arg constructor?
		adapter = new OrderListAdapter(new ArrayList<Order>());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		swipeRefreshLayout.setRefreshing(true);
		// XXX magic number
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
	}

	@Override
	public Loader<AsyncResult<List<Order>>> onCreateLoader(int id, Bundle args) {
		return new OrderLoader(getActivity());
	}

	@Override
	public void onLoaderReset(Loader<AsyncResult<List<Order>>> loader) { }

	@Override
	public void onLoadFinished(Loader<AsyncResult<List<Order>>> loader, AsyncResult<List<Order>> data) {
		if(data.hasError()) {
			// XXX printing error to toast?
			Toast.makeText(getContext(), data.getError().toString(), Toast.LENGTH_LONG).show();
		} else {
			adapter.setData(data.getValue());
		}

		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		getLoaderManager().restartLoader(0, null, this);
	}

	private static class OrderLoader extends AsyncTaskLoader<AsyncResult<List<Order>>> {

		AsyncResult<List<Order>> result;

		OrderLoader(Context context) {
			super(context);
		}

		@Override
		protected void onStartLoading() {
			if (result != null) {
				deliverResult(result);
			}

			if (takeContentChanged() || result == null) {
				forceLoad();
			}
		}

		@Override
		public AsyncResult<List<Order>> loadInBackground() {

			try {
				result = new AsyncResult<>(RetrofitManager.getOrdersSync(getContext()), null);
			} catch (IOException e) {
				result = new AsyncResult<>(null, e);
			}

			return result;
		}

		@Override
		protected void onStopLoading() {
			cancelLoad();
		}

		@Override
		protected void onReset() {
			onStopLoading();
		}
	}

	private class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {

		private Order order;

		private TextView startAddress;
		private TextView endAddress;
		private TextView orderDate;
		private TextView price;

		ViewHolder(View itemView) {
			super(itemView);

			startAddress = (TextView) itemView.findViewById(R.id.start_address);
			endAddress = (TextView) itemView.findViewById(R.id.end_address);
			orderDate = (TextView) itemView.findViewById(R.id.order_date);
			price = (TextView) itemView.findViewById(R.id.price);

			itemView.setOnClickListener(this);
		}

		void bind(Order order) {
			this.order = order;

			startAddress.setText(order.getStartAddress().toString());
			endAddress.setText(order.getEndAddress().toString());
			// XXX date format not reused
			orderDate.setText(DateFormat.getDateFormat(getContext()).format(order.getOrderTime()));
			price.setText(order.getPrice().toString());
		}

		@Override
		public void onClick(View v) {
			callback.onOrderSelected(order);
		}
	}

	private class OrderListAdapter extends RecyclerView.Adapter<ViewHolder> {

		List<Order> data;

		OrderListAdapter(List<Order> data) {
			this.data = data;
		}

		@Override
		public int getItemCount() {
			return data.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			holder.bind(data.get(position));
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			return new ViewHolder(inflater.inflate(R.layout.order_item, parent, false));
		}

		public void setData(List<Order> data) {
			// XXX no defensive copy
			this.data = data;
			notifyDataSetChanged();
		}
	}

	public interface Callback {
		void onOrderSelected(Order order);
	}
}
