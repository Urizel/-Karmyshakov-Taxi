package com.example.dk.ekassirtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class SingleFragmentActivity extends AppCompatActivity {

	private Toolbar toolbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, getFragment())
				.commit();
	}

	protected abstract Fragment getFragment();
}
