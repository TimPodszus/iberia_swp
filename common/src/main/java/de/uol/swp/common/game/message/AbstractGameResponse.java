package de.uol.swp.common.game.message;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import java.util.Objects;

public class AbstractGameResponse extends AbstractResponseMessage {
    private boolean success;
    private String description;

    public AbstractGameResponse(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

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
        AbstractGameResponse that = (AbstractGameResponse) o;
        return success == that.success && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), success, description);
    }
}
