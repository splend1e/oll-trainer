package com.devmite.rubik.model;

import android.graphics.Bitmap;

public class ItemAlgorithm {
	Bitmap img;
	String title;
	String desc;
	private boolean cb1;
	
	public ItemAlgorithm(Bitmap img, String title, String desc, boolean cb1) {
		super();
		this.img = img;
		this.title = title;
		this.desc = desc;
		this.cb1 = cb1;
	}

	public boolean isChecked() {
		return cb1;
	}

	public void setCb1(boolean cb1) {
		this.cb1 = cb1;
	}

	public ItemAlgorithm(Bitmap  img, String title, String desc) {
		super();
		this.img = img;
		this.title = title;
		this.desc = desc;
	}
	
	public Bitmap  getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
