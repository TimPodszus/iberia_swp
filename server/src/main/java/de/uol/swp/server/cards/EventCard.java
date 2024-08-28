package de.uol.swp.server.cards;


import lombok.Getter;


@Getter

public class EventCard extends Card {
    private final String action;

    protected EventCard(int id, String title, String type, String action)
    {
        super(id, title, type);
        this.action = action;
    }

    public void executeAction(){
        //not implemented
    }
}
