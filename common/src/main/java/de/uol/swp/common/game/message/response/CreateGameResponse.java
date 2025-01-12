package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;

import java.util.Objects;

/**
 * Response message for creating a game.
 * <p>
 * This class represents a response message that is sent when a game is created.
 * It extends the AbstractGameResponse class and includes additional information about the game.
 */
public class CreateGameResponse extends AbstractGameResponse {
    private final transient IGameDTO gameDTO;

    /**
     * Constructs a new CreateGameResponse.
     *
     * @param success     indicates whether the game creation was successful
     * @param description a description of the game creation result
     * @param gameDTO     the game data transfer object associated with the created game
     */
    public CreateGameResponse(boolean success, String description, IGameDTO gameDTO) {
        super(success, description);
        this.gameDTO = gameDTO;
    }

    /**
     * Checks if this object is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CreateGameResponse that = (CreateGameResponse) o;
        return Objects.equals(gameDTO, that.gameDTO);
    }

    /**
     * Computes the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameDTO);
    }
}