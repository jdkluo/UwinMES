package com.redlichee.uwinmes.model;

public class KeyValue {
	private String key;// key
	private String value;// value

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
