package com.minimalist.code;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.disposables.Disposable;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotTools {
    private static BotInitialization Main;
    private BotWSClient Client;
    public static Group bot = Bot.getBotInstances().get(0).getGroups().get(BotInitialization.GroupChat);
    public static Group bots = Bot.getBotInstances().get(0).getGroups().get(BotInitialization.GroupMain);

    /**
     * 检测一个字符串中是否含有中文
     *
     * @param countname 待检测字符串
     * @return
     */
    public static boolean checkcountname(String countname) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 发送带图片的消息模板模板
     *
     * @param event    事件类型
     * @param filename 位于项目资源文件夹下的图片名
     * @param msg      消息内容
     */
    public static void SendPictureMessage(GroupMessage event, String filename, String msg) {
        File file = new File(BotInitialization.class.getResource("/" + filename).getPath());
        event.getGroup().sendMessage(MessageUtils.newImage(event.getGroup().uploadImage(file).getImageId())
                .plus(new At(event.getSender()))
                .plus("\n" + msg)
        );
    }

    /**
     * 发送不带图片的群消息
     * 独立不依赖于Event
     *
     * @param Message 消息内容
     */
    public static void sendGroupMessage(String Message) {
        bot.sendMessage(Message);
    }

    /**
     * 发送不带图片的群消息到主群
     * 独立不依赖于Event
     *
     * @param Message 消息内容
     */
    public static void sendGroupMessageToNain(String Message) {
        bots.sendMessage(Message);
    }

    /**
     * 发起get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String get(String url) throws Exception {
        String content = null;
        URLConnection urlConnection = new URL(url).openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        connection.setRequestMethod("GET");
        //连接
        connection.connect();
        //得到响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder bs = new StringBuilder();
            String l;
            while ((l = bufferedReader.readLine()) != null) {
                bs.append(l).append("\n");
            }
            content = bs.toString();
        }
        return content;
    }
    /**
     * 截取两个字符串之间的字符
     * @param str 原字符串
     * @param strStart 字符A
     * @param strEnd 字符B
     * @return 截取后字符*/
    public static String splitData(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
        public static void Help(GroupMessage event){
        SendPictureMessage(event,"Good_job.png","游玩帮助已发送到私聊");
        event.getSender().sendMessage("欢迎来到Minimalist !");
        event.getSender().sendMessage("为了帮助您更好的游玩");
        event.getSender().sendMessage("请认真阅读以下帮助：");
        event.getSender().sendMessage("帮助文档：\n" +
                "1→服务器介绍\n" +
                "2→服务器玩法\n" +
                "3→进入服务器\n" +
                "4→支持服务器\n" +
                "Ps：发送序号即可");
        }

    //传入进程名称processName
    public static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName +'"');
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {}
            }
        }
    }

}

