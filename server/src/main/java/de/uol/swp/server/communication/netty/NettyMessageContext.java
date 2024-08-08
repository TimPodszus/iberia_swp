package de.uol.swp.server.communication.netty;

import de.uol.swp.common.message.MessageContext;
import de.uol.swp.common.message.ResponseMessage;
import de.uol.swp.common.message.ServerMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * This class is used to encapsulate a netty channel handler context
 *
 * @see de.uol.swp.common.message.MessageContext
 * @author Marco Grawunder
 * @since 2019-11-20
 */
class NettyMessageContext implements MessageContext {

    private final transient ChannelHandlerContext ctx;

    /**
     * Constructor
     *
     * @param ctx the ChannelHandlerContext encapsulated by this
     * @since 2019-11-20
     */
    public NettyMessageContext(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Getter for the ChannelHandlerContext encapsulated by this class
     *
     * @return the ChannelHandlerContext
     * @see io.netty.channel.ChannelHandlerContext
     * @since 2019-11-20
     */
    ChannelHandlerContext getCtx() {
        return ctx;
    }

    @Override
    public void writeAndFlush(ResponseMessage message) {
        ctx.writeAndFlush(message);
    }

    @Override
    public void writeAndFlush(ServerMessage message) {
        ctx.writeAndFlush(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NettyMessageContext that = (NettyMessageContext) o;
        return Objects.equals(ctx, that.ctx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ctx);
    }
}
