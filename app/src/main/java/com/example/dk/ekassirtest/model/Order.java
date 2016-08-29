package com.example.dk.ekassirtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@SuppressWarnings("unused")
public class Order implements Parcelable {

	private int id;
	private Address startAddress;
	private Address endAddress;
	private Price price;
	private Date orderTime;
	private Vehicle vehicle;

	public Order(int id, Address startAddress, Address endAddress, Price price, Date orderTime, Vehicle vehicle) {
		this.id = id;
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.price = price;
		this.orderTime = orderTime;
		this.vehicle = vehicle;
	}

	private Order(Parcel parcel) {
		this.id = parcel.readInt();
		this.startAddress = parcel.readParcelable(Address.class.getClassLoader());
		this.endAddress = parcel.readParcelable(Address.class.getClassLoader());
		this.price = parcel.readParcelable(Price.class.getClassLoader());
		this.orderTime = (Date) parcel.readSerializable();
		this.vehicle = parcel.readParcelable(Vehicle.class.getClassLoader());
	}

	public Address getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(Address endAddress) {
		this.endAddress = endAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Address getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(Address startAddress) {
		this.startAddress = startAddress;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public static Parcelable.Creator<Order> CREATOR = new Creator<Order>() {
		@Override
		public Order createFromParcel(Parcel parcel) {
			return new Order(parcel);
		}

		@Override
		public Order[] newArray(int i) {
			return new Order[i];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeParcelable(startAddress, i);
		parcel.writeParcelable(endAddress, i);
		parcel.writeParcelable(price, i);
		parcel.writeSerializable(orderTime);
		parcel.writeParcelable(vehicle, i);
	}
}
