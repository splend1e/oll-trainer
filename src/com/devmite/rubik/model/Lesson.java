package com.devmite.rubik.model;

public class Lesson {

	private int id;
	private String name;
	private int[] content;
	
	public Lesson() {
		// TODO Auto-generated constructor stub
	}
	
	public Lesson(String name, int[] content) {
		super();
		this.name = name;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getContent() {
		return content;
	}

	public void setContent(int[] content) {
		this.content = content;
	}
	
	public int[] setContentFromString(String contentString) {
		String[] split = contentString.split(",");
		int[] contentInt = new int[split.length];
		
		for (int i=0; i<split.length;i++){
			contentInt[i] = Integer.parseInt(split[i]);
		}
		return contentInt;
	}
	
	public String getContentString() {
		StringBuilder sb = new StringBuilder();
		String result ="";
		for (int i=0; i< content.length; i++){
			sb.append(content);
			sb.append(",");
		}
		if (sb.length() > 0){
			result = sb.substring(0, sb.length());
		}
		return result;
	}
	
}
