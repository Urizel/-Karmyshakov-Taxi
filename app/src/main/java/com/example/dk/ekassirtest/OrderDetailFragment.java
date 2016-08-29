package com.example.dk.ekassirtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk.ekassirtest.model.Order;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends Fragment {

	private static final String ARG_ORDER = "order";

	private Order order;

	private ImageView photo;
	private ProgressBar photoProgressBar;
	private TextView loadErrorTitle;

	private TextView startAddress;
	private TextView endAddress;
	private TextView orderTime;
	private TextView price;

	private TextView driverName;
	private TextView modelName;
	private TextView regNumber;

	public static OrderDetailFragment newInstance(Order order) {
		OrderDetailFragment fragment = new OrderDetailFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_ORDER, order);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		order = getArguments().getParcelable(ARG_ORDER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_order_detail, container, false);
		photo = (ImageView) v.findViewById(R.id.photo);
		photoProgressBar = (ProgressBar) v.findViewById(R.id.photo_progress_bar);
		loadErrorTitle = (TextView) v.findViewById(R.id.load_error_title);

		startAddress = (TextView) v.findViewById(R.id.start_address);
		endAddress = (TextView) v.findViewById(R.id.end_address);
		orderTime = (TextView) v.findViewById(R.id.order_time);
		price = (TextView) v.findViewById(R.id.price);

		driverName = (TextView) v.findViewById(R.id.driver_name);
		modelName = (TextView) v.findViewById(R.id.model_name);
		regNumber = (TextView) v.findViewById(R.id.reg_number);

		startAddress.setText(order.getStartAddress().toString());
		endAddress.setText(order.getEndAddress().toString());
		orderTime.setText(DateFormat.getTimeFormat(getContext()).format(order.getOrderTime()));
		price.setText(order.getPrice().toString());

		driverName.setText(order.getVehicle().getDriverName());
		modelName.setText(order.getVehicle().getModelName());
		regNumber.setText(order.getVehicle().getRegNumber());

		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadPhoto();
	}

	private void loadPhoto() {

		Bitmap bitmap = ExpiringBitmapLruCache.getInstance()
				.getExpiringValue(order.getVehicle().getPhoto());
		if(bitmap == null) {
			photoProgressBar.setVisibility(View.VISIBLE);
			photo.setVisibility(View.GONE);
			loadErrorTitle.setVisibility(View.GONE);

			RetrofitManager.getService(getActivity())
					.getPhoto(order.getVehicle().getPhoto())
					.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call,
						                       Response<ResponseBody> response) {
							new DecodeBitmapAsyncTask().execute(response.body().byteStream());
						}

						@Override
						public void onFailure(Call<ResponseBody> call, Throwable t) {
							Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();

							photoProgressBar.setVisibility(View.GONE);
							photo.setVisibility(View.GONE);
							loadErrorTitle.setVisibility(View.VISIBLE);
						}
					});
		} else {
			photo.setImageBitmap(bitmap);

			photoProgressBar.setVisibility(View.GONE);
			photo.setVisibility(View.VISIBLE);
			loadErrorTitle.setVisibility(View.GONE);
		}
	}

	private class DecodeBitmapAsyncTask extends AsyncTask<InputStream, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(InputStream... params) {
			return BitmapFactory.decodeStream(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			ExpiringBitmapLruCache.getInstance()
					.putExpiringValue(order.getVehicle().getPhoto(), bitmap);
			photo.setImageBitmap(bitmap);

			photoProgressBar.setVisibility(View.GONE);
			photo.setVisibility(View.VISIBLE);
			loadErrorTitle.setVisibility(View.GONE);
		}
	}
}
