package com.devmite.rubik.database;

public class Record {
	private int id;
	private int algoType;
	private int algoNum;
	private int timeValue;
	private String timeLabel;
	private long date;

	public Record() {
	}

	public Record(int algoType, int algoNum, int timeValue, String timeLabel) {
		super();
		this.algoType = algoType;
		this.algoNum = algoNum;
		this.timeValue = timeValue;
		this.timeLabel = timeLabel;
		date = System.currentTimeMillis();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAlgoType() {
		return algoType;
	}

	public void setAlgoType(int algoType) {
		this.algoType = algoType;
	}

	public int getAlgoNum() {
		return algoNum;
	}

	public void setAlgoNum(int algoNum) {
		this.algoNum = algoNum;
	}

	public int getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(int timeValue) {
		this.timeValue = timeValue;
	}

	public String getTimeLabel() {
		return timeLabel;
	}

	public void setTimeLabel(String time) {
		this.timeLabel = time;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
