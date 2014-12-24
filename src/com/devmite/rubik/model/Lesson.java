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
	
	public void setContentFromString(String contentString) {
		if (contentString != null && contentString.length()>0){
			String[] split = contentString.split(",");
			int[] contentInt = new int[split.length];
			
			for (int i=0; i<split.length;i++){
				contentInt[i] = Integer.parseInt(split[i]);
			}
			content = contentInt;
		}
	}
	
	public String getContentStringActual() {
		// 0-based
		StringBuilder sb = new StringBuilder();
		if (content != null){
			for (int i=0; i< content.length; i++){
				sb.append(content[i]);
				sb.append(",");
			}
		}
		
		String result = sb.toString();
		
		if (result.length() > 0){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
	public String getContentStringForShow() {
		// This is for show only. It is 1-based
		StringBuilder sb = new StringBuilder();
		if (content != null){
			for (int i=0; i< content.length; i++){
				int show = content[i] + 1;
				sb.append(show);
				sb.append(",");
			}
		}
		
		String result = sb.toString();
		
		if (result.length() > 0){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
}
