package com.example.dk.ekassirtest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.dk.ekassirtest.model.Order;

public class OrderDetailActivity extends SingleFragmentActivity {

	private static String EXTRA_ORDER = "order";

	private Order order;

	public static Intent getIntent(Context context, Order order) {
		Intent i = new Intent(context, OrderDetailActivity.class);
		i.putExtra(EXTRA_ORDER, order);
		return i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		order = getIntent().getParcelableExtra(EXTRA_ORDER);
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected Fragment getFragment() {
		return OrderDetailFragment.newInstance(order);
	}
}
