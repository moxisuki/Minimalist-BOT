package com.minimalist.code;

import cn.leancloud.utils.StringUtil;
import com.google.gson.Gson;
import com.minimalist.code.Utils.FormServerCmd;
import com.minimalist.code.Utils.FormServerData;
import net.mamoe.mirai.Bot;

import java.util.Objects;

public class BotHandleMessage {
    /**
     * 处理来自于服务端的消息消息
     *
     * @param message 消息内容
     */
    public static void init(String message) {
        Gson gson = new Gson();
        FormServerData mactn = gson.fromJson(message, FormServerData.class);
        switch (mactn.getOperate()) {
            case "onmsg":
                HandleMessage(mactn);
                break;
            case "onjoin":
                HandleJoin(mactn);
                break;
            case "onleft":
                HandleLeft(mactn);
                break;
            case "runcmd":
                HandleCmd(message);
        }

    }

    //技术有限 指令回调只能写在这
    private static void HandleCmd(String mactn) {
        //依赖项
        Gson gson = new Gson();
        FormServerCmd cmd = gson.fromJson(mactn, FormServerCmd.class);
        //初始化数据
        String Auth = cmd.getAuth();//执行状态
        String Text = cmd.getText();//回调内容
        //是否成功
        if (Auth.equals("Success")) {
            //功能区
            switch (cmd.getMsgid()) {
                case "Players":
                    String Message = "当前在线玩家: {playersize}\n{players}";
                    String str = "players online:";
                    String Players = Text.substring(Text.indexOf(str)).substring(str.length());
                    System.out.println(Players);
                    if (Objects.equals(Players, "")) {
                        Players = "\n当前没有玩家在线";
                    }
                    String Playersize = BotTools.splitData(Text, "There are ", " players");
                    BotTools.sendGroupMessageToNain(Message.replace("{playersize}", Playersize).replace("{players}", Players).replaceFirst("\n", ""));
                    break;
                case "whitelist":
                    if (Text.contains("Player added to whitelist")) {
                        BotTools.sendGroupMessageToNain("白名单申请成功");
                    } else if (Text.contains("Player already in whitelist")) {
                        BotTools.sendGroupMessageToNain("您已经拥有白名单啦");
                    }
                    break;
                case "money query":
                    if (Text.contains("找不到目标")) {
                        BotTools.sendGroupMessageToNain("服务器没有您的数据");
                    } else {
                        String CB = BotTools.splitData(Text, "Money: ", "命令已执行").replace("\n","");
                        BotTools.sendGroupMessageToNain("您的CB剩余：" + CB);
                    }
                    break;

            }
        } else {
            BotTools.sendGroupMessage("发生错误：\n请检查传输协议");
        }

    }

    private static void HandleLeft(FormServerData mactn) {
        String message = "玩家" + mactn.getTarget() + "离开服务器";
        BotTools.sendGroupMessage(message);
    }

    private static void HandleJoin(FormServerData mactn) {
        String message = "玩家" + mactn.getTarget() + "进入服务器";
        BotTools.sendGroupMessage(message);
    }

    private static void HandleMessage(FormServerData mactn) {
        String message = "玩家" + mactn.getTarget() + "说：\n" + "→ " + mactn.getText();
        BotTools.sendGroupMessage(message);
    }
}
