package com.minimalist.code.Utils;

import com.google.gson.annotations.SerializedName;

public class ChatHadle {

	@SerializedName("result")
	private int result;

	@SerializedName("content")
	private String content;

	public int getResult(){
		return result;
	}

	public String getContent(){
		return content;
	}
}