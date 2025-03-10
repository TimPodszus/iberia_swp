package de.uol.swp.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingHandler extends ChannelOutboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(LoggingHandler.class);


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            int frameSize = byteBuf.readableBytes();
            logger.debug("Writing data: {}, Frame size: {} bytes", msg, frameSize);
        }
        super.write(ctx, msg, promise);
    }
}