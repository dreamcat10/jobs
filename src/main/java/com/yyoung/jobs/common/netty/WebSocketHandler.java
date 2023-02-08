package com.yyoung.jobs.common.netty;

import com.alibaba.fastjson.JSON;
import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.service.ChatLogService;
import com.yyoung.jobs.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class WebSocketHandler {


    private final RedisTemplate redisTemplate;
    private final ChatLogService chatLogService;

    //当前在线用户
    private Long id;

    private static final AtomicInteger onLineCount = new AtomicInteger(0);
    //当前在线用户的集合
    private static final ConcurrentHashMap<Long, Channel> websocketMap = new ConcurrentHashMap<>();
    //存放未发送的消息
    private static final ConcurrentHashMap<Long, List<String>> msgMap = new ConcurrentHashMap<>();

    public WebSocketHandler(){
         redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
         chatLogService = (ChatLogService) SpringUtil.getBean(ChatLogService.class);
    }

    public void addUser(Long id, Channel channel){
        this.id = id;
        if (websocketMap.containsKey(id)){
            websocketMap.remove(id);
            websocketMap.put(id,channel);
        }else {
            websocketMap.put(id,channel);
            onLineCount.getAndIncrement();
        }
           log.info("id为:{}的用户上线!,当前在线人数:{}",id,onLineCount);
    }

    public void remove(){
        if (websocketMap.containsKey(id)){
            websocketMap.remove(id);
            onLineCount.getAndDecrement();
            log.info("id为:{}的用户下线!",id);
        }
    }

    public void sendMsg(Long toId, String msg, Long roomId){
        ChatLog chatLog = new ChatLog();
        String logMsg = handleLog(chatLog,toId, msg, roomId);
        if (websocketMap.containsKey(toId)){
            NioSocketChannel ch = (NioSocketChannel) websocketMap.get(toId);
            ch.writeAndFlush(new TextWebSocketFrame(logMsg));
            chatLog.setState(1);
            log.info("成功发送消息给id为:{}的用户",toId);
        }else {
            //如果用户不在线则将消息缓存起来
            log.info("id为:{}的用户不在线",toId);
            chatLog.setState(0);
            if (msgMap.containsKey(toId)){
                ArrayList<String> list = (ArrayList<String>) msgMap.get(toId);
                list.add(logMsg);
            }else {
                ArrayList<String> list = new ArrayList<>();
                list.add(logMsg);
                msgMap.put(toId, list);
            }
            redisTemplate.opsForValue().set(toId+"",msgMap.get(toId));
        }
            chatLogService.save(chatLog);

    }


    public String handleLog(ChatLog chatLog,Long toId, String msg, Long roomId){
        chatLog.setChatRoomId(roomId);
        chatLog.setFromId(id);
        chatLog.setToId(toId);
        chatLog.setContext(msg);
        String logMsg = JSON.toJSONString(chatLog);

        return logMsg;
    }
    //接收离线时别人发送的消息
    public void receive(){
        if (websocketMap.containsKey(id)){
            Channel ch = websocketMap.get(id);
            ArrayList<String> list = (ArrayList<String>) redisTemplate.opsForValue().get(id+"");
//            ArrayList<String> list = (ArrayList<String>) msgMap.get(id);
            if (list != null){
                for (String s : list) {
                    ch.write(new TextWebSocketFrame(s));
                }
                ch.flush();
                chatLogService.receive(id);
            }
            redisTemplate.delete(id+"");
            msgMap.remove(id);

        }
    }


}
