package com.yyoung.jobs.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class WebSocketServer {

    //在线人数
    private static AtomicInteger onLineCount = new AtomicInteger(0);

    //存放每个客户端对应的websocket对象
    private static ConcurrentHashMap<Long ,WebSocketServer> websocketMap = new ConcurrentHashMap<>();

    //与客户端的会话，通过这个向客户端发送信息
    private Session session;

    //接收到的用户id
    private Long id;

    //与服务端建立连接成功
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id){
        this.session =  session;
        this.id = id;
        //判断当前用户是否存在连接
        if (websocketMap.containsKey(id)){
            //存在，将当前websocket覆盖之前的
            websocketMap.remove(id);
            websocketMap.put(id, this);
        }else {
            websocketMap.put(id, this);
            onLineCount.getAndIncrement();
        }
        log.info("一个用户连接成功，当前连接数:{}",onLineCount);
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //关闭连接
    @OnClose
    public void onClose(){
        if (websocketMap.containsKey(id)){
            websocketMap.remove(id);
            onLineCount.decrementAndGet();
        }
        log.info("用户:{},断开连接!", id);
    }

    //接收消息，发送给指定用户
    @OnMessage
    public void OnMessage(String message, Session session) {
        log.info("用户:{},发送消息:{}",id,message);
        try {
        if (!ObjectUtils.isEmpty(message)){
            JSONObject jsonObject = JSON.parseObject(message);
            log.info("json:{}",jsonObject);
            jsonObject.put("fromId",this.id);
            Long toId = Long.valueOf(jsonObject.getString("toId"));

            if (toId!=null&&websocketMap.containsKey(toId)){
                    websocketMap.get(toId).sendMessage(jsonObject.toJSONString());
            }else {
                log.info("消息发送失败,用户:{}未在线",toId);
            }
        }
             }catch (IOException e) {
                e.printStackTrace();
            }
        }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.id+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    //实现服务器主动推送
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        log.info("发送消息成功:{}",message);
    }

    //发送自定义消息
    public static void sendInfo(String message, @PathParam("id") Long id) throws IOException {
          if (!ObjectUtils.isEmpty(message)&&websocketMap.containsKey("id"))
             {
                 websocketMap.get("id").sendMessage(message);
             }else {
              log.info("消息为空或用户:{}不在线",id);
          }

    }



}
