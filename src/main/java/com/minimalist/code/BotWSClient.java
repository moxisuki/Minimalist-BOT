package com.minimalist.code;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class BotWSClient extends WebSocketClient {

    public BotWSClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public BotWSClient(URI serverURI) {
        super(serverURI);
    }

    public BotWSClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        BotTools.sendGroupMessage("服务器连接成功");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        BotHandleMessage.init(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        BotEvent.Clientstatus.remove("Status");
        BotEvent.bbs.complete();
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        BotTools.sendGroupMessage("服务器响应失败");
        try {
            BotTools.sendGroupMessage("正在尝试唤醒...");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

                BotEvent b = new BotEvent();
                //初始化事件类
                BotEvent ini = new BotEvent();
                ini.Load(b.OpenClient());
      }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }



}
