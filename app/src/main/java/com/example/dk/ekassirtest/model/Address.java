package com.example.dk.ekassirtest.model;

import android.os.Parcel;
import android.os.Parcelable;

// XXX suppress for class
@SuppressWarnings("unused")
public class Address implements Parcelable {
	private String city;
	private String address;

	public Address(String address, String city) {
		this.address = address;
		this.city = city;
	}

	private Address(Parcel parcel) {
		this.city = parcel.readString();
		this.address = parcel.readString();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

    // XXX used for formatting on UI layer
    // XXX not optimal - could use concat
	@Override
	public String toString() {
		return String.format("%s, %s", city, address);
	}

	public static Parcelable.Creator<Address> CREATOR = new Creator<Address>() {
		@Override
		public Address createFromParcel(Parcel parcel) {
			return new Address(parcel);
		}

		@Override
		public Address[] newArray(int i) {
			return new Address[i];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

    // XXX what is i?
	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(city);
		parcel.writeString(address);
	}
}
