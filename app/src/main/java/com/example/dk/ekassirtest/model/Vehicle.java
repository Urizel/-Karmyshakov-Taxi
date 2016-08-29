package com.example.dk.ekassirtest.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Vehicle implements Parcelable {
	private String regNumber;
	private String modelName;
	private String photo;
	private String driverName;

	public Vehicle(String driverName, String modelName, String photo, String regNumber) {
		this.driverName = driverName;
		this.modelName = modelName;
		this.photo = photo;
		this.regNumber = regNumber;
	}

	private Vehicle(Parcel parcel) {
		this.regNumber = parcel.readString();
		this.modelName = parcel.readString();
		this.photo = parcel.readString();
		this.driverName = parcel.readString();
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public static Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
		@Override
		public Vehicle createFromParcel(Parcel parcel) {
			return new Vehicle(parcel);
		}

		@Override
		public Vehicle[] newArray(int i) {
			return new Vehicle[i];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(regNumber);
		parcel.writeString(modelName);
		parcel.writeString(photo);
		parcel.writeString(driverName);
	}
}
