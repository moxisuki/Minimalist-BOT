package com.minimalist.code.Utils;

import com.google.gson.Gson;
import com.minimalist.code.BotWSClient;

public class PostDataToServer {

    public static String RunCmd(BotWSClient Client,String cmd,String id) {
        ToServerCmd code = new ToServerCmd();
        code.setOperate("runcmd");
        code.setPasswd("");
        code.setMsgid(id);
        code.setCmd(cmd);
        Gson gson = new Gson();
        String post = gson.toJson(code).toString();
        code.setPasswd(GetPassword.Password(post));
        Client.send(gson.toJson(code).toString());
        return null;
    }
}
