package com.wangzai.nettywebsocket.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyWebsocketHandler extends ChannelInboundHandlerAdapter {


    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {

            FullHttpRequest request = (FullHttpRequest) msg;

            //检验请求
            if (!request.decoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
                //返回错误信息
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                if (response.status().code() != 200) {
                    ByteBuf byteBuf = Unpooled.copiedBuffer("请求异常", CharsetUtil.UTF_8);
                    response.content().writeBytes(byteBuf);
                    byteBuf.release();
                }
                ctx.writeAndFlush(response);
                return;
            }

            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws://localhost:8888/websocket", null, false);

            handshaker = webSocketServerHandshakerFactory.newHandshaker(request);

            if (handshaker == null) {
                webSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), request);
            }

        } else if (msg instanceof WebSocketFrame) {

            //判断是否是关闭链路命令
            if (msg instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg);
            }

            //我们只处理文本内容
            if(msg instanceof TextWebSocketFrame){
                //获取文本
                String request = ((TextWebSocketFrame)msg).text();

                //回写
                ctx.writeAndFlush(new TextWebSocketFrame("返回数据:" + request));


            }
        }
    }


}
