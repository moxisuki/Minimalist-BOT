package com.minimalist.code;

import com.google.gson.Gson;
import com.minimalist.code.Utils.ChatHadle;
import com.minimalist.code.Utils.GetPassword;
import com.minimalist.code.Utils.PostDataToServer;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.ConfigSection;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.message.GroupMessage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class BotEvent {
    private BotWSClient Client;
    public static HashMap<String, String> Clientstatus = new HashMap<>();
    BotInitialization bot = new BotInitialization();
    public static Listener<GroupMessage> bbs;
    public Config Players = BotInitialization.Players;
    public ConfigSection UserStatus = BotInitialization.UserStatus;


    public void Load(BotWSClient Clients) {
        bbs = bot.getEventListener().subscribeAlways(GroupMessage.class, (GroupMessage event) -> {
            //定义事件返回内容及其初始化
            String Content = event.getMessage().contentToString();
            String Sender = String.valueOf(event.getSender().getId());
            Config Players = BotInitialization.Players;
            ConfigSection UserStatus = BotInitialization.UserStatus;
            UserStatus.setIfAbsent(Sender, false);
            Players.setIfAbsent(Sender, "");

            if(event.getGroup().getId() == BotInitialization.GroupChat){
                if(Content.contains("chat ")){
                    String chat = Content.substring(5);
                if(chat.length()>20){
                    event.getGroup().sendMessage("汝言甚多");
                }else if (chat.contains("§")){
                    event.getGroup().sendMessage("在？为什么要变色");
                }else{
                    PostDataToServer.RunToPlayerMeg(Clients,event.getSender().getNick(),chat);
                }
            }}
            if(event.getGroup().getId() == BotInitialization.GroupMain) {
                //某聊天API处理
                if (Content.contains("#* ")) {
                    String s = Content.substring(3);
                    BotTools tool = new BotTools();
                    try {
                        String ready = tool.get("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + s);
                        Gson gson = new Gson();
                        event.getGroup().sendMessage(gson.fromJson(ready, ChatHadle.class).getContent().replace("{br}", "\n"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //在线玩家
                else if (Content.equals("#Players")) {
                    try {
                        event.getGroup().sendMessage("正在查询...");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PostDataToServer.RunCmd(Clients, "list", "Players");
                }
                //1.绑定开始 ---Step 1
                if (Content.equals("#BindBE")) {
                    //判断是否绑定过
                    if (Players.getString(Sender).equals("")) {
                        //未绑定
                        if (!UserStatus.getBoolean(Sender)) {
                            //未进行绑定
                            BotTools.SendPictureMessage(event, "Good_job.png", "请在60s内发送您的XboxID");
                            UserStatus.set(Sender, true);//开始绑定判断
                            //新建计时器
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    //60s后执行
                                    if (UserStatus.getBoolean(Sender)) {
                                        //60s内没有进行绑定
                                        BotTools.SendPictureMessage(event, "Cry.png", "当前请求已超时");
                                        UserStatus.set(Sender, false);
                                    }
                                }
                            }, 60000);
                        } else {
                            //上一个绑定程序正在运行
                            BotTools.SendPictureMessage(event, "Surprise.png", "当前正在绑定哦");
                        }
                    } else {
                        //已绑定
                        BotTools.SendPictureMessage(event, "Question.png", "已经绑定过了哦");
                    }//判断结束
                }//绑定结束
                //1.处理绑定结果 ---Step 2
                else if (UserStatus.getBoolean(Sender)) {
                    //开始处理ID
                    if (Content.length() > 20 || BotTools.checkcountname(Content)) {
                        BotTools.SendPictureMessage(event, "Eat.png", "您的XboxID好像不对哦");
                    } else if (Players.toString().contains(Content)) {
                        BotTools.SendPictureMessage(event, "Appreciate.png", "这个XboxID已经被抢先啦");
                    } else {
                        //绑定用户的ID
                        Players.set(Sender, Content);
                        UserStatus.set(Sender, false);
                        BotTools.SendPictureMessage(event, "Star.png", "绑定XboxID成功啦");
                        Players.save();
                    }
                }//处理结束
                //2.个人信息
                else if (Content.equals("#About")) {
                    //信息集合
                    String BEID = Players.getString(Sender);

                    String Demo = "√QQID：{XBID}\n√XboxID：{BEID}";
                    //判断是否绑定
                    if (isBindBe(Sender)) {
                        //已绑定
                        String msg = Demo.replace("{BEID}", BEID).replace("{XBID}", Sender);
                        BotTools.SendPictureMessage(event, "Star.png", msg);
                    } else {
                        //未绑定
                        String msg = Demo.replace("{BEID}", "未绑定").replace("{XBID}", Sender);
                        BotTools.SendPictureMessage(event, "Star.png", msg);
                    }
                }//个人信息结束
                //3.解绑BE
                else if (Content.equals("#UnBindBE")) {

                    //判断是否绑定
                    if (isBindBe(Sender)) {
                        //已绑定
                        Players.set(Sender, "");
                        BotTools.SendPictureMessage(event, "Surprise.png", "解绑XboxID成功啦");
                    } else {
                        //未绑定
                        BotTools.SendPictureMessage(event, "Question.png", "您都还没绑定呢");
                    }
                }//解绑结束
                //申请白名单
                else if(Content.equals("#GetWhiteList")){
                    if(isBindBe(Sender)){
                        //已绑定
                        String whitelist = "whitelist add \""+Players.getString(Sender)+"\"";
                         try {
                             BotTools.SendPictureMessage(event,"Eat.png","正在提交您的申请...");
                             Thread.sleep(2000);
                             PostDataToServer.RunCmd(Clients,whitelist,"whitelist");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            BotTools.sendGroupMessageToNain("申请失败，错误日志已发送到私聊");
                            event.getSender().sendMessage(e.toString());
                        }
                    }else{
                        //未绑定
                        BotTools.SendPictureMessage(event, "Question.png", "您都还没绑定呢");
                    }

                }//申请白名单结束
                //服务器状态查询
                else if(Content.equals("#Status")){
                    if(BotTools.findProcess("bedrock_server.exe")){
                        BotTools.SendPictureMessage(event,"Star.png","服务器状态正常");
                    }else{
                        BotTools.SendPictureMessage(event,"Cry.png","服务器状态异常");
                    }
                }
                //帮助
                else if(Content.equals("#Help")){
                BotTools.Help(event);
                }
                else if(Content.equals("#CB")){
                    event.getGroup().sendMessage("正在为您查询...");
                    PostDataToServer.RunCmd(Clients,"money query \""+Players.getString(Sender)+"\"","money query");
                }
            }});
    }

    private boolean isBindBe(String sender) {
        if (Players.exist(sender)){
            return !Players.getString(sender).equals("");
        }else{
            return false;
        }
    }

    /**
     * 链接到服务器
     * Q: 为什么放在这个类
     * A: 方便上面的事件使用
     */
    public BotWSClient OpenClient() {
        if (Clientstatus.get("Status") == null) {
            try {
                Client = new BotWSClient(new URI(BotInitialization.ClientHost));
                Client.connectBlocking();
                Clientstatus.put("Status", "True");
            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Client;
    }
}
