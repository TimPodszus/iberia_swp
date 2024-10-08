package de.uol.swp.common.cards;


import lombok.Getter;


@Getter

public class EventCardDTO extends CardDTO {
    private final String action;

    protected EventCardDTO(int id, String title, String type, String action)
    {
        super(id, title, type);
        this.action = action;
    }

    public void executeAction(){
        //not implemented
    }
}
