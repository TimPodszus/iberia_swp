
package de.uol.swp.common.game;

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