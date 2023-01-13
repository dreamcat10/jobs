package com.yyoung.jobs.common.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.DefaultAttributeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        WebSocketHandler handler = new WebSocketHandler();
        AttributeKey<WebSocketHandler> map = AttributeKey.valueOf("map");
        ch.attr(map).set(handler);

        WebSocketHandler webSocketHandler = ch.attr(map).get();
        // http的支持
        pipeline.addLast(new HttpServerCodec());
        // 返回封块的处理，以块的方式写数据。
        pipeline.addLast(new ChunkedWriteHandler());
        // 拼接消息数据
        pipeline.addLast(new HttpObjectAggregator(8192));
        //获取url参数
        pipeline.addLast(new WebSocketUrlHandler());
        // websocket的处理器
        pipeline.addLast(new WebSocketServerProtocolHandler("/imServer"));
        // 自定义入站处理器
        pipeline.addLast("myServerInboundHandler", new MyServerInboundHandler());
        // 自定义出站处理器
        pipeline.addLast("myServerOutboundHandler",new MyServerOutboundHandler());
    }
}
