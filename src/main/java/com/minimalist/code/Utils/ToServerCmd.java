package com.minimalist.code.Utils;

import com.google.gson.annotations.SerializedName;

public class ToServerCmd {

    @SerializedName("operate")
    private String operate;

    @SerializedName("passwd")
    private String passwd;

    @SerializedName("cmd")
    private String cmd;

    @SerializedName("msgid")
    private String msgid;

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getOperate() {
        return operate;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }
}