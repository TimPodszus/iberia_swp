package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.user.User;
import lombok.Getter;
import java.util.List;
import java.util.Objects;

@Getter
public class CreateGameRequest extends AbstractGameRequest {
    int difficulty;
    private List<User> users;
    public CreateGameRequest(String lobbyCode, int difficulty, List<User> users) {
        super(lobbyCode);
        this.difficulty = difficulty;
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateGameRequest that = (CreateGameRequest) o;
        return difficulty == that.difficulty && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(difficulty, users);
    }
}
