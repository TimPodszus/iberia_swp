package de.uol.swp.server.communication;


import com.google.inject.Inject;
import de.uol.swp.common.game.message.ActionRequest;
import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.MessageContext;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.message.request.RequestMessage;
import de.uol.swp.common.message.response.ExceptionMessage;
import de.uol.swp.common.message.response.ResponseMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import de.uol.swp.server.GameManager;
import de.uol.swp.server.game.GameTurnException;
import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.message.ClientDisconnectedMessage;
import de.uol.swp.server.message.ServerExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class handles all client/server communication
 *
 * @author Marco Grawunder
 * @see de.uol.swp.server.communication.ServerHandlerDelegate
 * @since 2017-03-17
 */


public class ServerHandler implements ServerHandlerDelegate {

    private static final Logger LOG = LogManager.getLogger(ServerHandler.class);

    /**
     * Clients that are connected
     */
    private final List<MessageContext> connectedClients = new CopyOnWriteArrayList<>();

    /**
     * Clients with logged in sessions
     */
    private final Map<MessageContext, Session> activeSessions = new HashMap<>();

    /**
     * Event bus (injected)
     */
    private final EventBus eventBus;

    private final GameManager gameManager;

    /**
     * Constructor
     *
     * @param eventBus the EventBus used throughout the entire server
     * @see EventBus
     */
    @Inject
    public ServerHandler(EventBus eventBus, GameManager gameManager) {
        this.eventBus = eventBus;
        this.gameManager = gameManager;
        eventBus.register(this);
    }

    @Override
    public void process(RequestMessage msg) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received new message from client {}", msg);
        }

        final Optional<MessageContext> messageContext = msg.getMessageContext();
        if (messageContext.isEmpty()) {
            LOG.error("No message context for {}", msg);
            return;
        }

        try {
            checkIfMessageNeedsAuthorization(messageContext.get(), msg);

            if (msg instanceof ActionRequest actionRequest) {
                handleMessageAction(actionRequest);
            } else {
                eventBus.post(msg);
            }
        } catch (Exception e) {
            LOG.error("ServerException {} {}",
                    e.getClass()
                     .getName(),
                    e.getMessage()
            );
            sendToClient(messageContext.get(), new ExceptionMessage(e.getMessage()));
        }
    }


    /**
     * Helper method that check if a Message has the required authorization
     *
     * @param ctx the MessageContext connected to the message to check
     * @param msg the message to check
     * @throws SecurityException authorization requirement not met
     * @since 2019-11-20
     */
    private void checkIfMessageNeedsAuthorization(MessageContext ctx, RequestMessage msg) {
        if (msg.authorizationNeeded()) {
            final Optional<Session> session = getSession(ctx);
            if (session.isEmpty()) {
                throw new SecurityException("Authorization required. Client not logged in!");
            }
            msg.setSession(session.get());
        }
    }

    /**
     * Handles exceptions on the Server
     *
     * If an ServerExceptionMessage is detected on the EventBus, this method is called.
     * It sends the ServerExceptionMessage to the affiliated client if a client is
     * affiliated.
     *
     * @param msg The ServerExceptionMessage found on the EventBus
     * @since 2019-11-20
     */
    @Subscribe
    public void onServerExceptionMessage(ServerExceptionMessage msg) {
        Optional<MessageContext> ctx = getCtx(msg);
        LOG.error(msg.getException());
        ctx.ifPresent(channelHandlerContext -> sendToClient(channelHandlerContext,
                new ExceptionMessage(msg.getException()
                                        .getMessage())
        ));
    }

    // -------------------------------------------------------------------------------
    // Handling of connected clients
    // -------------------------------------------------------------------------------
    @Override
    public void newClientConnected(MessageContext ctx) {
        LOG.debug("New client {} connected", ctx);
        connectedClients.add(ctx);
    }

    @Override
    public void clientDisconnected(MessageContext ctx) {
        LOG.debug("Client disconnected");
        Session session = this.activeSessions.get(ctx);
        if (session != null) {
            ClientDisconnectedMessage msg = new ClientDisconnectedMessage();
            msg.setSession(session);
            eventBus.post(msg);
            removeSession(ctx);
        }
        connectedClients.remove(ctx);
    }

    // -------------------------------------------------------------------------------
    // User Management Events (from event bus)
    // -------------------------------------------------------------------------------

    /**
     * Handles ClientAuthorizedMessages found on the EventBus
     *
     * If a ClientAuthorizedMessage is detected on the EventBus, this method is called.
     * It gets the MessageContext and then gives it and a new LoginSuccessfulResponse to
     * sendToClient for sending as well as giving a new UserLoggedInMessage to sendMessage
     * for notifying all connected clients.
     *
     * @param msg The ClientAuthorizedMessage found on the EventBus
     * @see de.uol.swp.server.communication.ServerHandler#sendToClient(MessageContext, ResponseMessage)
     * @see de.uol.swp.server.communication.ServerHandler#sendMessage(ServerMessage)
     * @since 2019-11-20
     */
    @Subscribe
    public void onClientAuthorizedMessage(ClientAuthorizedMessage msg) {
        Optional<MessageContext> ctx = getCtx(msg);
        final Optional<Session> session = msg.getSession();
        if (ctx.isPresent() && session.isPresent()) {
            putSession(ctx.get(), session.get());
            sendToClient(ctx.get(), new LoginSuccessfulResponse(msg.getUser()));
            sendMessage(new UserLoggedInMessage(msg.getUser()
                                                   .getUsername()));
        } else {
            LOG.warn("No context for {}", msg);
        }
    }

    /**
     * Handles UserLoggedOutMessages found on the EventBus
     *
     * If an UserLoggedOutMessage is detected on the EventBus, this method is called.
     * It gets the MessageContext and then gives the message to sendMessage in order
     * to send it to the connected client.
     *
     * @param msg The UserLoggedOutMessage found on the EventBus
     * @see de.uol.swp.server.communication.ServerHandler#sendMessage(ServerMessage)
     * @since 2019-11-20
     */
    @Subscribe
    public void onUserLoggedOutMessage(UserLoggedOutMessage msg) {
        Optional<MessageContext> ctx = getCtx(msg);
        ctx.ifPresent(this::removeSession);
        sendMessage(msg);
    }

    // -------------------------------------------------------------------------------
    // ResponseEvents
    // -------------------------------------------------------------------------------

    /**
     * Handles ResponseMessages found on the EventBus
     *
     * If an ResponseMessage is detected on the EventBus, this method is called.
     * It gets the MessageContext and then gives it and the ResponseMessage to
     * sendToClient for sending.
     *
     * @param msg The ResponseMessage found on the EventBus
     * @see de.uol.swp.server.communication.ServerHandler#sendToClient(MessageContext, ResponseMessage)
     * @since 2019-11-20
     */
    @Subscribe
    public void onResponseMessage(ResponseMessage msg) {
        Optional<MessageContext> ctx = getCtx(msg);
        if (ctx.isPresent()) {
            msg.setSession(null);
            msg.setMessageContext(null);
            LOG.debug("Send to client {} message {} ", ctx.get(), msg);
            sendToClient(ctx.get(), msg);
        } else {
            LOG.warn("Got response message without receiver {}", msg);
        }
    }

    // -------------------------------------------------------------------------------
    // ServerMessages
    // -------------------------------------------------------------------------------

    /**
     * Handles ServerMessages found on the EventBus
     *
     * If an ServerMessage is detected on the EventBus, this method is called.
     * It sets the Session and MessageContext to null and then gives the message
     * to sendMessage in order to send it to all connected clients.
     *
     * @param msg The ServerMessage found on the EventBus
     * @see de.uol.swp.server.communication.ServerHandler#sendMessage(ServerMessage)
     * @since 2019-11-20
     */
    @Subscribe
    public void onServerMessage(ServerMessage msg) {
        msg.setSession(null);
        msg.setMessageContext(null);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Send {} to {}",
                    msg,
                    (msg.getReceiver()
                        .isEmpty() || msg.getReceiver() == null ? "all" : msg.getReceiver())
            );
        }
        sendMessage(msg);
    }


    // -------------------------------------------------------------------------------
    // Session Management (helper methods)
    // -------------------------------------------------------------------------------

    /**
     * Adds a new Session to the activeSessions
     *
     * @param ctx        The MessageContext belonging to the Session
     * @param newSession the Session to add
     * @since 2019-11-20
     */
    private void putSession(MessageContext ctx, Session newSession) {

        // TODO: check if session is already bound to connection
        activeSessions.put(ctx, newSession);
    }

    /**
     * Removes a Session specified by MessageContext from the activeSessions
     *
     * @param ctx the MessageContext
     * @since 2019-11-20
     */
    private void removeSession(MessageContext ctx) {
        activeSessions.remove(ctx);
    }

    /**
     * Gets the Session for a given MessageContext
     *
     * @param ctx The MeesageContext
     * @return Optional containing the Session if found
     * @see de.uol.swp.common.user.Session
     * @see de.uol.swp.common.message.MessageContext
     * @since 2019-11-20
     */
    private Optional<Session> getSession(MessageContext ctx) {
        Session session = activeSessions.get(ctx);
        return session != null ? Optional.of(session) : Optional.empty();
    }

    /**
     * Gets MessageContext from Message
     *
     * @param message Message to get the MessageContext from
     * @return Optional containing the MessageContext if there is any
     * @see de.uol.swp.common.message.Message
     * @see de.uol.swp.common.message.MessageContext
     * @since 2019-11-20
     */
    private Optional<MessageContext> getCtx(Message message) {
        if (message.getMessageContext()
                   .isPresent()) {
            return message.getMessageContext();
        }
        final Optional<Session> session = message.getSession();
        if (session.isPresent()) {
            return getCtx(session.get());
        }
        return Optional.empty();
    }

    /**
     * Gets MessageContext for specified receiver
     *
     * @param session Session of the user to search
     * @return Optional containing MessageContext if there is one
     * @see de.uol.swp.common.user.Session
     * @see de.uol.swp.common.message.MessageContext
     * @since 2019-11-20
     */
    private Optional<MessageContext> getCtx(Session session) {
        for (Map.Entry<MessageContext, Session> e : activeSessions.entrySet()) {
            if (e.getValue()
                 .equals(session)) {
                return Optional.of(e.getKey());
            }
        }
        return Optional.empty();
    }

    /**
     * Gets MessageContexts for specified receivers
     *
     * @param receiver A list containing the sessions of the users to search
     * @return List of MessageContexts for the given sessions
     * @see de.uol.swp.common.user.Session
     * @see de.uol.swp.common.message.MessageContext
     * @since 2019-11-20
     */
    private List<MessageContext> getCtx(List<Session> receiver) {
        List<MessageContext> ctxs = new ArrayList<>();
        receiver.forEach(r -> {
            Optional<MessageContext> s = getCtx(r);
            s.ifPresent(ctxs::add);
        });
        return ctxs;
    }


    // -------------------------------------------------------------------------------
    // Help methods: Send only objects of type Message
    // -------------------------------------------------------------------------------

    /**
     * Sends a ResponseMessage to a client specified by a MessageContext
     *
     * @param ctx     The MessageContext containing the specified client
     * @param message The Message to send
     * @see ResponseMessage
     * @see de.uol.swp.common.message.MessageContext
     * @since 2019-11-20
     */
    private void sendToClient(MessageContext ctx, ResponseMessage message) {
        LOG.trace("Trying to sendMessage to client: {} {}", ctx, message);
        ctx.writeAndFlush(message);
    }

    /**
     * Sends a ServerMessage to either a specified receiver or all connected clients
     *
     * @param msg ServerMessage to send
     * @see ServerMessage
     * @since 2019-11-20
     */
    private void sendMessage(ServerMessage msg) {
        if (msg.getReceiver() == null || msg.getReceiver()
                                            .isEmpty()) {
            sendToMany(connectedClients, msg);
        } else {
            sendToMany(getCtx(msg.getReceiver()), msg);
        }
    }

    /**
     * Sends a ServerMessage to multiple users specified by a list of MessageContexts
     *
     * @param sendTo List of MessageContexts to send the message to
     * @param msg    message to send
     * @see de.uol.swp.common.message.MessageContext
     * @see ServerMessage
     * @since 2019-11-20
     */
    private void sendToMany(List<MessageContext> sendTo, ServerMessage msg) {
        for (MessageContext client : sendTo) {
            try {
                client.writeAndFlush(msg);
            } catch (Exception e) {
                // TODO: Handle exception for unreachable clients
                LOG.warn(e);
            }
        }
    }

    private void handleMessageAction(ActionRequest actionRequest) throws GameTurnException {
        Optional<MessageContext> context = actionRequest.getMessageContext();
        if (context.isPresent()) {
            Session session = getSession(context.get()).orElseThrow(() -> new SecurityException("Client not logged in"));
            gameManager.receiveAndForwardActionMessage(session.getUser(),
                    actionRequest.getAction(),
                    actionRequest.getLobbyCode()
            );
        } else {
            LOG.error("ActionMessage received without a valid context");
        }
    }

}
