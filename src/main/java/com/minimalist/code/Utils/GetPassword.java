package com.minimalist.code.Utils;

import com.minimalist.code.BotInitialization;
import net.mamoe.mirai.console.plugins.Config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class GetPassword {


    /**
     * 获取Websocket传输密码
     *
     * @return
     */
    public static String Password(String msg) {
        Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        String date = formatter.format(calendar.getTime());
        String passw = BotInitialization.Password + date + "@" + msg;
        return ToMd5(passw);
    }

    private static String ToMd5(String src) {
        // 需要加密的字符串
        try {
            // 加密对象，指定加密方式
            MessageDigest md5 = MessageDigest.getInstance("md5");
            // 准备要加密的数据
            byte[] b = src.getBytes();
            // 加密
            byte[] digest = md5.digest(b);
            // 十六进制的字符
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuffer sb = new StringBuffer();
            // 处理成十六进制的字符串(通常)
            for (byte bb : digest) {
                sb.append(chars[(bb >> 4) & 15]);
                sb.append(chars[bb & 15]);
            }
            // 打印加密后的字符串
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
