package com.example.dk.ekassirtest;

import android.support.annotation.Nullable;

public class AsyncResult<T> {

	private T value;
	private Throwable error;

	public AsyncResult(@Nullable T value, @Nullable Throwable error) {
		this.value = value;
		this.error = error;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean hasError() {
		return error != null;
	}
}
