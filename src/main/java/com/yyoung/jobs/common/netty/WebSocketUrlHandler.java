package com.yyoung.jobs.common.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
public class WebSocketUrlHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{

//    @Resource
//    WebSocketHandler webSocketHandler;
    private WebSocketHandler webSocketHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<WebSocketHandler> map = AttributeKey.valueOf("map");
        webSocketHandler = ctx.channel().attr(map).get();
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null&&msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Long id = Long.valueOf(uri.substring(uri.lastIndexOf("/")+1));
            log.info(String.valueOf(id));
            webSocketHandler.addUser(id,  ctx.channel());

            request.setUri("/imServer");
        }
        ctx.fireChannelRead(msg);
    }
}
