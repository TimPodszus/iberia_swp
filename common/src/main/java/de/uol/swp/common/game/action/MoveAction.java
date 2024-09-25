
package de.uol.swp.common.game.action;

import de.uol.swp.common.city.ICityDTO;
import lombok.Getter;

@Getter
public class MoveAction extends Action {

    ICityDTO destination;

    public MoveAction(ICityDTO destination) {
        super(ActionType.MOVE);
        this.destination = destination;
    }
}