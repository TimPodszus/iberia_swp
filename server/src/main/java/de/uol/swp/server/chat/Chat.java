package de.uol.swp.server.chat;

import de.uol.swp.common.chat.AbstractChatMessage;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<AbstractChatMessage> messages = new ArrayList<>();

    /**
     * Fügt eine neue Nachricht hinzu.
     *
     * @param message Die Nachricht, die hinzugefügt werden soll.
     */
    public void addMessage(AbstractChatMessage message) {
        messages.add(message);
    }

    /**
     * Gibt alle Nachrichten zurück.
     *
     * @return Eine Liste aller Nachrichten.
     */
    public List<AbstractChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }
}
