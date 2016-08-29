package com.example.dk.ekassirtest.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Price implements Parcelable {
	private int amount;
	private String currency;

	public Price(int amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	private Price(Parcel parcel) {
		this.amount = parcel.readInt();
		this.currency = parcel.readString();
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return String.format("%s %s", (float) amount / 100, currency);
	}

	public static Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
		@Override
		public Price createFromParcel(Parcel parcel) {
			return new Price(parcel);
		}

		@Override
		public Price[] newArray(int i) {
			return new Price[i];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(amount);
		parcel.writeString(currency);
	}
}
