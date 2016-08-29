package com.example.dk.ekassirtest;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.dk.ekassirtest.model.Order;

public class OrderListActivity extends SingleFragmentActivity implements OrderListFragment.Callback {

	@Override
	protected Fragment getFragment() {
		return OrderListFragment.newInstance();
	}

	// Why not handle in fragment
	@Override
	public void onOrderSelected(Order order) {
		startActivity(OrderDetailActivity.getIntent(this, order));
	}
}
