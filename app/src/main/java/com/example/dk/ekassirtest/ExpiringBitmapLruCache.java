package com.example.dk.ekassirtest;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExpiringBitmapLruCache<K> extends LruCache<K, Bitmap> {

	private static ExpiringBitmapLruCache<String> instance;

	private long expiringTime; //in ms
	private Map<K, Long> timeMap;

	public static ExpiringBitmapLruCache<String> getInstance() {
		if (instance == null) {
			int maxMemory = (int) (Runtime.getRuntime().maxMemory());
			int cacheSize = maxMemory / 8;
			instance = new ExpiringBitmapLruCache<>(cacheSize, TimeUnit.MINUTES.toMillis(10));
//			instance = new ExpiringBitmapLruCache<>(cacheSize, TimeUnit.SECONDS.toMillis(10));
		}
		return instance;
	}

	public ExpiringBitmapLruCache(int maxSize, long expiringTime) {
		super(maxSize);

		this.expiringTime = expiringTime;
		this.timeMap = new HashMap<>();
	}

	@Override
	protected int sizeOf(K key, Bitmap value) {
		return value.getByteCount();
	}

	@Override
	protected void entryRemoved(boolean evicted, K key, Bitmap oldValue, Bitmap newValue) {
		timeMap.remove(key);
	}

	public Bitmap getExpiringValue(K key) {
		Long creationTime = timeMap.get(key);
		if (creationTime != null && System.currentTimeMillis() - creationTime < expiringTime) {
			return super.get(key);
		} else {
			super.remove(key);
			return null;
		}
	}

	public void putExpiringValue(K key, Bitmap value) {
		super.put(key, value);
		timeMap.put(key, System.currentTimeMillis());
	}
}
