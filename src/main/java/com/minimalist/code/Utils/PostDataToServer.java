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
    /**
     * Send Message to server
     * @param Client Client Object
     * @param nick User Nick
     * @param content Message Content
     * */
    public static void RunToPlayerMeg(BotWSClient Client,String nick,String content) {
        String demo = "tellraw @a {\"rawtext\":[{\"text\":\"{msg}\"}]}";
        String msgdemo = "[§3群聊§r][§e{nick}§r] --> {content}".replace("{nick}",nick).replace("{content}",content);
        String tmpmsg = demo.replace("{msg}",msgdemo);
        System.out.println(tmpmsg);
        RunCmd(Client,tmpmsg,"chat");
    }
}
