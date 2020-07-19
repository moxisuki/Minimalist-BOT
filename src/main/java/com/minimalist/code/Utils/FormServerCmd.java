package com.minimalist.code.Utils;

import com.google.gson.annotations.SerializedName;

public class FormServerCmd{

	@SerializedName("operate")
	private String operate;

	@SerializedName("Auth")
	private String auth;

	@SerializedName("msgid")
	private String msgid;

	@SerializedName("text")
	private String text;

	public void setOperate(String operate){
		this.operate = operate;
	}

	public String getOperate(){
		return operate;
	}

	public void setAuth(String auth){
		this.auth = auth;
	}

	public String getAuth(){
		return auth;
	}

	public void setMsgid(String msgid){
		this.msgid = msgid;
	}

	public String getMsgid(){
		return msgid;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}
}