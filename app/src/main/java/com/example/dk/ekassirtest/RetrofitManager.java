package com.example.dk.ekassirtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dk.ekassirtest.model.Order;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public class RetrofitManager {

	private static Retrofit retrofit;

	public static TestService getService(Context context) {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(context.getString(R.string.base_url))
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		return retrofit.create(TestService.class);
	}

	public static List<Order> getOrdersSync(Context context) throws IOException {
		return getService(context).getOrders().execute().body();
	}

	interface TestService {
		@GET("orders.json")
		Call<List<Order>> getOrders();

		@GET("images/{photo}")
		Call<ResponseBody> getPhoto(@Path("photo") String photo);
	}
}
