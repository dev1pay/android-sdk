package com.m1pay.models;

public class ObjCard {
	private String type;
	private String serial;
	private String pin;
	private String message;
	private String amount;
	private String status;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public ObjCard(String type, String serial, String pin, String message,
			String amount, String status) {
		super();
		this.type = type;
		this.serial = serial;
		this.pin = pin;
		this.message = message;
		this.amount = amount;
		this.status = status;
	}
	
}
