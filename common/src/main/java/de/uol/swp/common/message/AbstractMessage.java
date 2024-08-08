package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class of all messages. Basic handling of session information
 *
 * @see de.uol.swp.common.message.Message
 * @author Marco Grawunder
 * @since 2017-03-17
 */
public abstract class AbstractMessage implements Message {

    private transient MessageContext messageContext;
    private transient Session session = null;

	@Override
	public Optional<MessageContext> getMessageContext() {
		return messageContext!=null? Optional.of(messageContext):Optional.empty();
	}

	@Override
	public void setMessageContext(MessageContext messageContext) {
		this.messageContext = messageContext;
	}

	@Override
	public void setSession(Session session){
		this.session = session;
	}

	@Override
	public Optional<Session> getSession(){
		return session!=null?Optional.of(session):Optional.empty();
	}

	@Override
	public void initWithMessage(Message otherMessage) {
		otherMessage.getMessageContext().ifPresent(this::setMessageContext);
		otherMessage.getSession().ifPresent(this::setSession);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractMessage that = (AbstractMessage) o;
		return Objects.equals(messageContext, that.messageContext) &&
				Objects.equals(session, that.session);
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageContext, session);
	}
}
