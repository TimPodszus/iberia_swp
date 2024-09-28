package de.uol.swp.common.game.action;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.ActionType;
import lombok.Getter;

@Getter
public class MoveAction extends Action {

    ICityDTO destination;

    public MoveAction(ICityDTO destination) {
        super(ActionType.MOVE);
        this.destination = destination;
    }
}
