package com.m1pay.models;

public class ObjSms {
	private String phoneNumber;
	private String contentMessage;
	private String price;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContentMessage() {
		return contentMessage;
	}
	public void setContentMessage(String contentMessage) {
		this.contentMessage = contentMessage;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public ObjSms(String phoneNumber, String contentMessage, String price) {
		super();
		this.phoneNumber = phoneNumber;
		this.contentMessage = contentMessage;
		this.price = price;
	}

}
