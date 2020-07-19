package com.minimalist.code;

import cn.leancloud.AVObject;
import cn.leancloud.core.AVOSCloud;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.console.command.BlockingCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.JCommandManager;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.ConfigSection;
import net.mamoe.mirai.console.plugins.ConfigSectionFactory;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.GroupSettings;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    This is main.
*/
public class BotInitialization extends PluginBase {
    public static String ClientHost;
    public static String Password;
    public static Config Players;
    public static Long GroupMain = 476575391L;
    public static Long GroupChat = 476575391L;
    public static ConfigSection UserStatus;
    public void onLoad() {
        super.onLoad();
        //This is logger;
        getLogger().info("Loaded!");
        //存储配置信息
        Config setting = this.loadConfig("Setting.json");
        setting.setIfAbsent("Host", "127.0.0.1");
        setting.setIfAbsent("Port", "8880");
        setting.setIfAbsent("Pass", "123456789");
        setting.setIfAbsent("EndPoint", "/mc");
        ClientHost = "ws://" + setting.getString("Host") + ":" + setting.getString("Port") + setting.getString("EndPoint");
        Password = setting.getString("Pass");
        setting.save();


        //初始化存储器
        UserStatus = ConfigSectionFactory.create();
        Players = loadConfig("Players.json");
    }


    public void onEnable() {
        //This is logger.
        getLogger().info("Enabled!");
        //注册指令
        JCommandManager.getInstance().register(this, new BlockingCommand(
                "Server", new ArrayList<>(), "启动一个Websocket实例", "/Server Client InstanceName"
        ) {
            @Override
            public boolean onCommandBlocking(@NotNull CommandSender commandSender, @NotNull List<String> list) {
                if (list.size() < 1) {
                    return false;
                }
                if (list.get(0).equals("Client")) {
                    if(list.size()>1) {
                        if (list.get(1).equals("Bedrock")) {
                            commandSender.sendMessageBlocking("正在启动实例Bedrock");
                            BotEvent b = new BotEvent();
                            //初始化事件类
                            BotEvent ini = new BotEvent();
                            ini.Load(b.OpenClient());
                        } else {
                            commandSender.sendMessageBlocking("该实例不不存在");
                        }
                    }else{return false;}
                }else{
                    return false;
                }
                return true;
            }
        });
    }
}