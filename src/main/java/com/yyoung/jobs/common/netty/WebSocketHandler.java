package com.yyoung.jobs.common.netty;

import com.yyoung.jobs.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class WebSocketHandler {


    private final ValueOperations ops;

    //当前在线用户
    private Long id;

    private static final AtomicInteger onLineCount = new AtomicInteger(0);
    //当前在线用户的集合
    private static final ConcurrentHashMap<Long, Channel> websocketMap = new ConcurrentHashMap<>();
    //存放未发送的消息
    private static final ConcurrentHashMap<Long, List<String>> msgMap = new ConcurrentHashMap<>();

    public WebSocketHandler(){
        RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
        ops = redisTemplate.opsForValue();
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

    public void sendMsg(Long toId, String msg){
        msg = "{\"fromId\":" +id+ ",\"toId\":" +toId+ ",\"contentText\":" +msg+ "}" ;
        if (websocketMap.containsKey(toId)){
            NioSocketChannel ch = (NioSocketChannel) websocketMap.get(toId);
            ch.writeAndFlush(new TextWebSocketFrame(msg));
            log.info("成功发送消息给id为:{}的用户",toId);
        }else {
            //如果用户不在线则将消息缓存起来
            log.info("id为:{}的用户不在线",toId);

            if (msgMap.containsKey(toId)){
                ArrayList<String> list = (ArrayList<String>) msgMap.get(toId);
                list.add(msg);
            }else {
                ArrayList<String> list = new ArrayList<>();
                list.add(msg);
                msgMap.put(toId, list);
            }
//            ops.set(toId, msgMap.get(toId));
        }
    }


    public void receive(){
        if (websocketMap.containsKey(id)){
            Channel ch = websocketMap.get(id);
//            ArrayList<String> list = (ArrayList<String>) ops.getAndDelete(id);
            ArrayList<String> list = (ArrayList<String>) msgMap.get(id);
            if (list != null){
                for (String s : list) {
                    ch.write(new TextWebSocketFrame(s));
                }
                ch.flush();
            }
            msgMap.remove(id);
        }
    }


}
