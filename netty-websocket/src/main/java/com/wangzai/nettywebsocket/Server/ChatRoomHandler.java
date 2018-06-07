package com.wangzai.nettywebsocket.Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ChatRoomHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    //广播
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //获取内容
        String content = msg.text();

        //获取当前的Channel
        Channel incoming = ctx.channel();

        for (Channel channel : channels) {
            //不是刚刚进来的
            if (channel != incoming ){
                channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + ":" + content));
            }else {
                channel.writeAndFlush(new TextWebSocketFrame("我自己:" + content));
            }
        }

    }


    //进入:,提示所有人,有人进来了
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "已经进入聊天室"));
        }

        channels.add(ctx.channel());
    }



    //离开,提示
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "离开了聊天室"));
        }

    }
}

