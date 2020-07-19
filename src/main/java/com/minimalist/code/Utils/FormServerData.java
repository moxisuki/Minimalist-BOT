package com.minimalist.code.Utils;

import com.google.gson.annotations.SerializedName;

public class FormServerData {

	@SerializedName("operate")
	private String operate;

	@SerializedName("text")
	private String text;

	@SerializedName("target")
	private String target;

	public String getOperate(){
		return operate;
	}

	public String getText(){
		return text;
	}

	public String getTarget(){
		return target;
	}
}