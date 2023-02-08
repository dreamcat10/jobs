package com.yyoung.jobs.common.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yyoung.jobs.entity.ChatLog;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyServerInboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketHandler webSocketHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<WebSocketHandler> map = AttributeKey.valueOf("map");
        webSocketHandler = ctx.channel().attr(map).get();
        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        webSocketHandler.remove();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TextWebSocketFrame webSocketFrame = (TextWebSocketFrame) msg;
        if (webSocketFrame.text().equals("接收消息")){
            webSocketHandler.receive();
        }else{
            super.channelRead(ctx,msg);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        JSONObject json = JSON.parseObject(text);
        Long toId =Long.valueOf((String) json.get("toId")) ;
        String contentText = (String) json.get("contentText");
        Long roomId = Long.valueOf((String) json.get("roomId"));

        webSocketHandler.sendMsg(toId, contentText, roomId);
        ChatLog chatLog = new ChatLog();
        String logMsg = webSocketHandler.handleLog(chatLog, toId, contentText, roomId);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(logMsg));
    }

}
